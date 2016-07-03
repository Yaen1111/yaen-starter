package org.yaen.starter.core.model.models.user;

import java.util.Set;

import org.yaen.starter.common.dal.entities.user.UserEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.services.UserService;

import lombok.Getter;
import lombok.Setter;

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
public class UserModel extends OneModel {

	/** the service */
	@Getter
	private UserService service;

	/** the inner user */
	@Getter
	@Setter
	private UserEntity user;

	/** the role id list */
	private Set<String> roles;

	/** the auth id list */
	private Set<String> auths;

	/**
	 * construct new model with service
	 * 
	 * @param service
	 */
	public UserModel(UserService service) {
		super("1.0.0");

		this.service = service;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (this.user == null)
			throw new CoreException("user not loaded");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.user = null;
		this.roles = null;
		this.auths = null;
	}

	/**
	 * load by username(id)
	 * 
	 * @param username
	 * @throws DataNotExistsException
	 * @throws DuplicateDataException
	 * @throws CoreException
	 */
	public void load(String username) throws DataNotExistsException, DuplicateDataException, CoreException {
		this.service.loadModel(this, username);
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
			this.roles = this.service.getUserRoles(this.user.getId());
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
			this.auths = this.service.getUserAuths(this.user.getId());
		}

		return this.auths;
	}

}
