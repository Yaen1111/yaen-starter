package org.yaen.starter.biz.shared.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.biz.shared.objects.RoleDTO;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsBizException;
import org.yaen.starter.common.data.exceptions.DuplicateDataBizException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.user.Role;
import org.yaen.starter.core.model.models.user.RoleAuth;
import org.yaen.starter.core.model.models.user.User;
import org.yaen.starter.core.model.models.user.UserRole;

/**
 * user based actions
 * 
 * @author Yaen 2016年5月17日下午4:08:26
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelService modelService;

	@Autowired
	private QueryService queryService;

	/**
	 * inner get user by id(username)
	 * 
	 * @param username
	 * @return
	 * @throws BizException
	 */
	protected User innerGetUserByName(String username) throws BizException {
		AssertUtil.notNull(username);

		// find user
		User user = new User();

		try {

			List<User> list = queryService.selectModelListById(user, username);

			if (list == null || list.isEmpty()) {
				throw new DataNotExistsBizException("user not exists");
			}

			if (list.size() > 1) {
				throw new DuplicateDataBizException("duplicate usernames");
			}

			return list.get(0);

		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * inner get role by id(rolename)
	 * 
	 * @param rolename
	 * @return
	 * @throws BizException
	 */
	protected Role innerGetRoleByName(String rolename) throws BizException {
		AssertUtil.notNull(rolename);

		// find role
		Role role = new Role();

		try {

			List<Role> list = queryService.selectModelListById(role, rolename);

			if (list == null || list.isEmpty()) {
				throw new DataNotExistsBizException("role not exists");
			}

			if (list.size() > 1) {
				throw new DuplicateDataBizException("duplicate rolenames");
			}

			return list.get(0);

		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#registerNewUser(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void registerNewUser(UserDTO userdto) throws BizException {
		AssertUtil.notNull(userdto);

		// create new user
		User user = new User();

		// set basic info
		user.setId(userdto.getUserId());
		user.setPasswordHash(userdto.getPassword());

		// gen salt, set hash
		user.setPasswordSalt(userdto.getPasswordSalt());
		user.setPasswordHash(userdto.getPasswordHash());

		try {
			modelService.insertModelByRowid(user);
		} catch (CoreException ex) {
			throw new BizException(ex);
		}

	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#getUserByName(java.lang.String)
	 */
	@Override
	public UserDTO getUserByName(String username) throws BizException {
		AssertUtil.notNull(username);

		// find user
		User user = this.innerGetUserByName(username);

		// user ok, set info
		UserDTO userdto = new UserDTO();
		userdto.setUserId(user.getId());
		userdto.setPasswordHash(user.getPasswordHash());
		userdto.setPasswordSalt(user.getPasswordSalt());

		return userdto;
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#Logout(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public void Logout(UserDTO user) throws BizException {
		// TODO how to set last logout time?
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#checkUserCredentials(org.yaen.starter.biz.shared.objects.UserDTO)
	 */
	@Override
	public boolean checkUserCredentials(String password, String passwordHash, String passwordSalt) {

		// TODO here may set the last login time

		// check password, just same
		if (StringUtil.equals(password, passwordHash)) {
			return true;
		}
		return false;
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#createNewRole(org.yaen.starter.biz.shared.objects.RoleDTO)
	 */
	@Override
	public void createNewRole(RoleDTO roledto) throws BizException {
		AssertUtil.notNull(roledto);

		// create new role
		Role role = new Role();

		role.setId(roledto.getRoleId());
		role.setGroupName(roledto.getGroupName());
		role.setTitle(roledto.getTitle());
		role.setDescription(roledto.getDescription());

		try {
			modelService.insertModelByRowid(role);
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#getRoleListAll()
	 */
	@Override
	public List<RoleDTO> getRoleListAll() throws BizException {

		List<RoleDTO> list = new ArrayList<RoleDTO>();

		Role role = new Role();

		try {
			// try get one to create table
			modelService.trySelectModelByRowid(role, 0L);

			// get all id list
			List<Long> ids = queryService.selectRowidsByAll(role);
			List<Role> roles = queryService.selectModelListByRowids(role, ids);

			// make dto
			for (Role r : roles) {
				RoleDTO roledto = new RoleDTO();
				roledto.setRoleId(r.getId());
				roledto.setGroupName(r.getGroupName());
				roledto.setTitle(r.getTitle());
				roledto.setDescription(r.getDescription());
				list.add(roledto);
			}
		} catch (CoreException ex) {
			throw new BizException(ex);
		}

		return list;
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#getUserRoles(java.lang.String)
	 */
	@Override
	public List<String> getUserRoles(String username) throws BizException {
		// find user
		User user = this.innerGetUserByName(username);
		try {
			return user.getRoleIds();
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#assignUserRoles(java.lang.String, java.util.List)
	 */
	@Override
	public void assignUserRoles(String username, List<String> roles) throws BizException {
		AssertUtil.notNull(roles);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(roles);

		// get user
		User user = this.innerGetUserByName(username);
		UserRole userrole = new UserRole();

		try {
			// get current user roles
			List<UserRole> urs = queryService.selectModelListById(userrole, user.getId());

			for (UserRole u : urs) {
				if (set.contains(u.getId())) {
					// has, remove from set, as is no change
					set.remove(u.getId());
				} else {
					// delete if not in set
					modelService.deleteModelByRowid(u);
				}
			}

			// add new if still in set
			if (!set.isEmpty()) {
				for (String roleid : set) {
					UserRole ur = new UserRole();
					ur.setId(user.getId());
					ur.setRoleId(roleid);
					modelService.insertModelByRowid(ur);
				}
			}
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#getRoleAuths(java.lang.String)
	 */
	@Override
	public List<String> getRoleAuths(String rolename) throws BizException {

		// find role
		Role role = this.innerGetRoleByName(rolename);
		try {
			return role.getAuthIds();
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}

	/**
	 * @see org.yaen.starter.biz.shared.services.UserService#assignRoleAuths(java.lang.String, java.util.List)
	 */
	@Override
	public void assignRoleAuths(String rolename, List<String> auths) throws BizException {
		AssertUtil.notNull(auths);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(auths);

		// get user
		Role role = this.innerGetRoleByName(rolename);
		RoleAuth roleauth = new RoleAuth();

		try {
			// get current user roles
			List<RoleAuth> ras = queryService.selectModelListById(roleauth, role.getId());

			for (RoleAuth ra : ras) {
				if (set.contains(ra.getId())) {
					// has, remove from set, as is no change
					set.remove(ra.getId());
				} else {
					// delete if not in set
					modelService.deleteModelByRowid(ra);
				}
			}

			// add new if still in set
			if (!set.isEmpty()) {
				for (String authid : set) {
					RoleAuth ra = new RoleAuth();
					ra.setId(role.getId());
					ra.setAuthId(authid);
					modelService.insertModelByRowid(ra);
				}
			}
		} catch (CoreException ex) {
			throw new BizException(ex);
		}
	}
}
