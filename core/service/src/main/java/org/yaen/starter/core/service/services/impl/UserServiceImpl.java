package org.yaen.starter.core.service.services.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.entities.user.RoleAuthEntity;
import org.yaen.starter.core.model.entities.user.RoleEntity;
import org.yaen.starter.core.model.entities.user.UserRoleEntity;
import org.yaen.starter.core.model.services.UserService;

/**
 * user service to handle user model
 * 
 * @author Yaen 2016年7月3日下午1:02:08
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private EntityService entityService;

	@Autowired
	private QueryService queryService;

	/**
	 * calculate password hash with given salt
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	protected String calculatePasswordHash(String password, String salt) {
		return password + salt;
	}

	/**
	 * @see org.yaen.starter.core.model.services.UserService#checkUserCredentials(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean checkUserCredentials(String password, String passwordHash, String passwordSalt) {

		// TODO here may set the last login time

		// calculate hash
		String hash = this.calculatePasswordHash(password, passwordSalt);

		// check hash, just same
		return StringUtil.equals(hash, passwordHash);
	}

	/**
	 * @see org.yaen.starter.core.model.services.UserService#createNewRole(org.yaen.starter.core.model.entities.user.RoleEntity)
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void createNewRole(RoleEntity role) throws CoreException, DataException {
		AssertUtil.notNull(role);
		AssertUtil.notBlank(role.getId());

		try {

			// try get user by given username to check duplicate
			try {
				queryService.selectOneById(new RoleEntity(), role.getId());
			} catch (DuplicateDataException ex) {
				throw ex;
			} catch (DataNotExistsException e) {
				// demanded result, should be ok
			}

			// do insert
			entityService.insertEntityByRowid(role);

		} catch (NoDataAffectedException ex) {
			throw new CoreException(ex);
		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#getRoleListAll()
	 */
	@Override
	public List<RoleEntity> getRoleListAll() throws CoreException, DataException {

		try {
			RoleEntity role = new RoleEntity();

			// select all
			List<Long> rowids = queryService.selectRowidsByAll(role);

			return queryService.selectListByRowids(role, rowids);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#getUserRoles(java.lang.String)
	 */
	@Override
	public Set<String> getUserRoles(String username) throws CoreException, DataException {
		AssertUtil.notBlank(username);

		Set<String> role_set = new HashSet<String>();
		try {
			// get value list
			List<Object> list = queryService.selectValueListById(new UserRoleEntity(), username, "roleId");

			// convert to role list
			for (Object o : list) {
				role_set.add((String) o);
			}
		} catch (CommonException ex) {
			throw new CoreException(ex);
		}

		return role_set;
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#assignUserWithNewRoles(java.lang.String,
	 *      java.util.Collection)
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void assignUserWithNewRoles(String username, Collection<String> roles) throws CoreException, DataException {
		AssertUtil.notBlank(username);
		AssertUtil.notNull(roles);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(roles);

		// get user
		UserRoleEntity userrole = new UserRoleEntity();

		try {
			// get current user roles
			List<UserRoleEntity> urs = queryService.selectListById(userrole, username);

			for (UserRoleEntity u : urs) {
				if (set.contains(u.getId())) {
					// has, remove from set, as is no change
					set.remove(u.getId());
				} else {
					// delete if not in set
					entityService.deleteEntityByRowid(u);
				}
			}

			// add new if still in set
			if (!set.isEmpty()) {
				for (String roleid : set) {
					UserRoleEntity ur = new UserRoleEntity();
					ur.setId(username);
					ur.setRoleId(roleid);
					entityService.insertEntityByRowid(ur);
				}
			}
		} catch (NoDataAffectedException ex) {
			throw new CoreException(ex);
		} catch (CommonException ex) {
			throw new CoreException(ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#getRoleAuths(java.lang.String)
	 */
	@Override
	public Set<String> getRoleAuths(String roleId) throws CoreException, DataException {
		AssertUtil.notBlank(roleId);

		Set<String> auth_set = new HashSet<String>();
		try {
			// get value list
			List<Object> list = queryService.selectValueListById(new RoleAuthEntity(), roleId, "authId");

			// convert to role list
			for (Object o : list) {
				auth_set.add((String) o);
			}
		} catch (CommonException ex) {
			throw new CoreException(ex);
		}

		return auth_set;
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#assignRoleWithNewAuths(java.lang.String,
	 *      java.util.Collection)
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void assignRoleWithNewAuths(String roleId, Collection<String> auths) throws CoreException, DataException {
		AssertUtil.notBlank(roleId);
		AssertUtil.notNull(auths);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(auths);

		// get auth
		RoleAuthEntity roleauth = new RoleAuthEntity();

		try {
			// get current role auths
			List<RoleAuthEntity> ras = queryService.selectListById(roleauth, roleId);

			for (RoleAuthEntity e : ras) {
				if (set.contains(e.getId())) {
					// has, remove from set, as is no change
					set.remove(e.getId());
				} else {
					// delete if not in set
					entityService.deleteEntityByRowid(e);
				}
			}

			// add new if still in set
			if (!set.isEmpty()) {
				for (String authid : set) {
					RoleAuthEntity ra = new RoleAuthEntity();
					ra.setId(roleId);
					ra.setAuthId(authid);
					entityService.insertEntityByRowid(ra);
				}
			}
		} catch (NoDataAffectedException ex) {
			throw new CoreException(ex);
		} catch (CommonException ex) {
			throw new CoreException(ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * @throws DataException 
	 * @see org.yaen.starter.core.model.services.UserService#getUserAuths(java.lang.String)
	 */
	@Override
	public Set<String> getUserAuths(String username) throws CoreException, DataException {
		AssertUtil.notBlank(username);

		// get all roles
		Set<String> role_set = this.getUserRoles(username);
		Set<String> auth_set = new HashSet<String>();

		// add each role auths
		for (String roleId : role_set) {
			auth_set.addAll(this.getRoleAuths(roleId));
		}

		return auth_set;
	}

}
