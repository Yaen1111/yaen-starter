package org.yaen.starter.core.model.models.user;

import java.util.Set;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
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

	public UserEntity getEntity() {
		return (UserEntity) this.getDefaultEntity();
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
	 * get roles
	 * 
	 * @return
	 * @throws CoreException
	 * @throws DataException
	 */
	public Set<String> getRoles() throws CoreException, DataException {
		this.check();

		// call service to get roles if not exists
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = this.service.getUserRoles(this.getEntity().getId());
		}

		return this.roles;
	}

	/**
	 * get auths
	 * 
	 * @return
	 * @throws CoreException
	 * @throws DataException
	 */
	public Set<String> getAuths() throws CoreException, DataException {
		this.check();

		// call rbac model to get roles if not exists
		if (this.auths == null || this.auths.isEmpty()) {
			this.auths = this.service.getUserAuths(this.getEntity().getId());
		}

		return this.auths;
	}

}
