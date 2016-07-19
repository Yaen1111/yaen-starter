package org.yaen.starter.core.model.models.user;

import java.util.Set;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.entities.user.UserEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.services.UserService;

import lombok.Getter;

/**
 * user model
 * <p>
 * user with rbac(Role-Based Access Control).
 * <p>
 * is made up with user, role, auth, user-role, role-auth, extends user-auth and groups
 * <p>
 * especially suitable for authorization
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public class UserModel extends TwoModel {

	/** the typed entity, overrides default entity */
	@Getter
	private UserEntity entity;

	@Override
	public OneEntity getDefaultEntity() {
		return this.entity;
	}

	@Override
	public void setDefaultEntity(OneEntity defaultEntity) {
		this.entity = (UserEntity) defaultEntity;
		super.setDefaultEntity(defaultEntity);
	}

	/** the service */
	@Getter
	private UserService service;

	/** the role id list */
	private Set<String> roles;

	/** the auth id list */
	private Set<String> auths;

	/**
	 * @param proxy
	 * @param service
	 */
	public UserModel(ProxyService proxy, UserService service) {
		super(proxy, new UserEntity());

		this.service = service;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		this.roles = null;
		this.auths = null;
	}

	/**
	 * get roles
	 * 
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getRoles() throws CoreException {
		this.check();

		// call service to get roles if not exists
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = this.service.getUserRoles(this.entity.getId());
		}

		return this.roles;
	}

	/**
	 * get auths
	 * 
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getAuths() throws CoreException {
		this.check();

		// call rbac model to get roles if not exists
		if (this.auths == null || this.auths.isEmpty()) {
			this.auths = this.service.getUserAuths(this.entity.getId());
		}

		return this.auths;
	}

}
