package org.yaen.starter.core.model.models.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yaen.starter.common.dal.entities.user.RoleAuthEntity;
import org.yaen.starter.common.dal.entities.user.RoleEntity;
import org.yaen.starter.common.dal.entities.user.UserRoleEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.contexts.ServiceManager;
import org.yaen.starter.core.model.models.OneModel;

/**
 * rbac model
 * <p>
 * deal with rbac(Role-Based Access Control).
 * <p>
 * is made up with user, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class RbacModel extends OneModel {

	/** entity service */
	private EntityService entityService = ServiceManager.getEntityService();

	/** query service */
	private QueryService queryService = ServiceManager.getQueryService();

	/**
	 * empty constructor
	 */
	public RbacModel() {
		super("1.0.0");
	}

	/**
	 * create new role with given id
	 * 
	 * @param role
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	public void createNewRole(RoleEntity role) throws CoreException, DuplicateDataException {
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

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * select all role list
	 * 
	 * @return
	 * @throws CoreException
	 */
	public List<RoleEntity> getRoleListAll() throws CoreException {

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
	 * get user role ids
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getUserRoles(String username) throws CoreException {
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
	 * assign new roles to user
	 * 
	 * @param username
	 * @param roles
	 * @throws CoreException
	 */
	public void assignUserWithNewRoles(String username, Collection<String> roles) throws CoreException {
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
		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * get auth ids for role
	 * 
	 * @param roleId
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getRoleAuths(String roleId) throws CoreException {
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
	 * assign new auths to role
	 * 
	 * @param roleId
	 * @param auths
	 * @throws CoreException
	 */
	public void assignRoleWithNewAuths(String roleId, Collection<String> auths) throws CoreException {
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
		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * get user auth ids
	 * <p>
	 * get user roles, then get all auths
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getUserAuths(String username) throws CoreException {
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
