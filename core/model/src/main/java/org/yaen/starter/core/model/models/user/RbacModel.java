package org.yaen.starter.core.model.models.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DataOperationCancelledException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.entities.user.RoleAuthEntity;
import org.yaen.starter.core.model.entities.user.RoleEntity;
import org.yaen.starter.core.model.entities.user.UserEntity;
import org.yaen.starter.core.model.entities.user.UserRoleEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * rbac(Role-Based Access Control) model, can be used on any type of entity
 * <p>
 * the id of entity is used to determine the rbac
 * <p>
 * is made up with base entity, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年8月4日下午2:28:32
 */
public class RbacModel extends TwoModel {

	/** the role id list */
	private Set<String> roles;

	/** the auth id list */
	private Set<String> auths;

	/**
	 * @param proxy
	 * @param entity
	 */
	public RbacModel(ProxyService proxy, OneEntity entity) {
		super(proxy, entity);
	}

	/**
	 * default rbac model with user entity
	 * 
	 * @param proxy
	 */
	public RbacModel(ProxyService proxy) {
		super(proxy, new UserEntity());
	}

	/**
	 * get roles
	 * 
	 * @return
	 * @throws CommonException
	 * @throws DataOperationCancelledException
	 */
	public Set<String> getRoles() throws DataOperationCancelledException, CommonException {
		// call service to get roles if not exists
		if (this.roles == null || this.roles.isEmpty()) {

			Set<String> role_set = new HashSet<String>();

			// get value list
			List<Object> list = this.proxy.getQueryService().selectValueListById(new UserRoleEntity(),
					this.getEntity().getId(), "roleId");

			// convert to role list
			for (Object o : list) {
				role_set.add((String) o);
			}

			// set to member
			this.roles = role_set;
		}

		return this.roles;
	}

	/**
	 * get auths
	 * 
	 * @return
	 * @throws CommonException
	 * @throws DataOperationCancelledException
	 */
	public Set<String> getAuths() throws DataOperationCancelledException, CommonException {
		// call rbac model to get roles if not exists
		if (this.auths == null || this.auths.isEmpty()) {

			// get all roles
			Set<String> role_set = this.getRoles();
			Set<String> auth_set = new HashSet<String>();

			// add each role auths
			for (String roleId : role_set) {
				auth_set.addAll(this.getRoleAuths(roleId));
			}

			// set to member
			this.auths = auth_set;
		}

		return this.auths;
	}

	/**
	 * get auth list of role
	 * 
	 * @param roleId
	 * @return
	 * @throws DataOperationCancelledException
	 * @throws CommonException
	 */
	public Set<String> getRoleAuths(String roleId) throws DataOperationCancelledException, CommonException {
		AssertUtil.notBlank(roleId);

		Set<String> auth_set = new HashSet<String>();

		// get value list
		List<Object> list = this.proxy.getQueryService().selectValueListById(new RoleAuthEntity(), roleId, "authId");

		// convert to role list
		for (Object o : list) {
			auth_set.add((String) o);
		}

		return auth_set;
	}

	/**
	 * get all role list
	 * 
	 * @return
	 * @throws CommonException
	 * @throws DataOperationCancelledException
	 */
	public List<RoleEntity> getRoleListAll() throws CommonException, DataOperationCancelledException {

		RoleEntity role = new RoleEntity();

		// select all
		List<Long> rowids = this.proxy.getQueryService().selectRowidsByAll(role);

		return this.proxy.getQueryService().selectListByRowids(role, rowids);
	}

	/**
	 * create new role by given role entity
	 * 
	 * @param role
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createNewRole(RoleEntity role) throws DataException, CommonException {
		AssertUtil.notNull(role);
		AssertUtil.notBlank(role.getId());

		// check role exists
		try {
			RoleEntity temp_role = new RoleEntity();
			temp_role.setId(role.getId());
			this.fillEntityById(temp_role);

			// here data exists, throw
			throw new DuplicateDataException("data already exists");
		} catch (DataNotExistsException ex) {
			// demanded result
		}

		// here is ok, insert
		this.insertEntity(role);
	}

	/**
	 * assign new roles to current user
	 * 
	 * @param roles
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void assignNewRoles(Collection<String> roles) throws CommonException, DataException {
		AssertUtil.notNull(roles);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(roles);

		// get user
		UserRoleEntity userrole = new UserRoleEntity();

		String userId = this.getEntity().getId();

		// get current user roles
		List<UserRoleEntity> urs = this.proxy.getQueryService().selectListById(userrole, userId);

		for (UserRoleEntity u : urs) {
			if (set.contains(u.getId())) {
				// has, remove from set, as is no change
				set.remove(u.getId());
			} else {
				// delete if not in set
				this.proxy.getEntityService().deleteEntityByRowid(u);
			}
		}

		// add new if still in set
		if (!set.isEmpty()) {
			for (String roleid : set) {
				UserRoleEntity ur = new UserRoleEntity();
				ur.setId(userId);
				ur.setRoleId(roleid);
				this.proxy.getEntityService().insertEntityByRowid(ur);
			}
		}
	}

	/**
	 * assign auth to given role
	 * 
	 * @param roleId
	 * @param auths
	 * @throws CommonException
	 * @throws DataException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void assignRoleWithNewAuths(String roleId, Collection<String> auths) throws CommonException, DataException {
		AssertUtil.notBlank(roleId);
		AssertUtil.notNull(auths);

		// copy roles and put in set
		Set<String> set = new HashSet<String>(auths);

		// get auth
		RoleAuthEntity roleauth = new RoleAuthEntity();

		// get current role auths
		List<RoleAuthEntity> ras = this.proxy.getQueryService().selectListById(roleauth, roleId);

		for (RoleAuthEntity e : ras) {
			if (set.contains(e.getId())) {
				// has, remove from set, as is no change
				set.remove(e.getId());
			} else {
				// delete if not in set
				this.proxy.getEntityService().deleteEntityByRowid(e);
			}
		}

		// add new if still in set
		if (!set.isEmpty()) {
			for (String authid : set) {
				RoleAuthEntity ra = new RoleAuthEntity();
				ra.setId(roleId);
				ra.setAuthId(authid);
				this.proxy.getEntityService().insertEntityByRowid(ra);
			}
		}
	}

}
