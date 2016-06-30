package org.yaen.starter.core.model.models.user;

import java.util.List;
import java.util.Set;

import org.yaen.starter.common.dal.entities.user.UserEntity;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.services.EntityService;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.contexts.ServiceManager;
import org.yaen.starter.core.model.models.OneModel;

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
public class UserModel extends OneModel {

	/** the inner user */
	@Getter
	private UserEntity user;

	/** the role id list */
	private Set<String> roles;

	/** the auth id list */
	private Set<String> auths;

	/** entity service */
	private EntityService entityService = ServiceManager.getEntityService();

	/** query service */
	private QueryService queryService = ServiceManager.getQueryService();

	/**
	 * empty constructor
	 */
	public UserModel() {
		super("1.0.0");
	}

	/**
	 * check user model
	 * 
	 * @throws CoreException
	 */
	protected void checkUser() throws CoreException {
		if (this.user == null)
			throw new CoreException("user not loaded");
	}

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
	 * load user by username(id)
	 * 
	 * @param id
	 * @throws CoreException
	 * @throws DuplicateDataException
	 * @throws DataNotExistsException
	 */
	public void load(String username) throws CoreException, DataNotExistsException, DuplicateDataException {
		AssertUtil.notBlank(username);

		// clear user
		this.user = null;

		try {
			// get user by id, only one
			this.user = queryService.selectOneById(new UserEntity(), username);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}

		// clear all other
		this.roles = null;
	}

	/**
	 * register new user by username(id) and password
	 * <p>
	 * the password is plain text, the model will hash it
	 * 
	 * @param username
	 * @param password
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	public void registerNewUser(String username, String password) throws CoreException, DuplicateDataException {
		AssertUtil.notBlank(username);
		AssertUtil.notBlank(password);

		try {

			// try get user by given username to check duplicate
			try {
				queryService.selectOneById(new UserEntity(), username);
			} catch (DuplicateDataException ex) {
				throw ex;
			} catch (DataNotExistsException e) {
				// demanded result, should be ok
			}

			// create entity
			UserEntity newuser = new UserEntity();
			newuser.setId(username);
			newuser.setPasswordHash(this.calculatePasswordHash(password, "salt"));

			// do insert
			entityService.insertEntityByRowid(newuser);

		} catch (CommonException ex) {
			throw new CoreException(ex);
		}
	}

	/**
	 * check user credentials
	 * 
	 * @param password
	 * @param passwordHash
	 * @param passwordSalt
	 * @return
	 */
	public boolean checkUserCredentials(String password, String passwordHash, String passwordSalt) {

		// TODO here may set the last login time

		// calculate hash
		String hash = this.calculatePasswordHash(password, passwordSalt);

		// check hash, just same
		return StringUtil.equals(hash, passwordHash);
	}

	/**
	 * get roles
	 * 
	 * @return
	 * @throws CoreException
	 */
	public Set<String> getRoles() throws CoreException {
		this.checkUser();

		// call rbac model to get roles if not exists
		if (this.roles == null || this.roles.isEmpty()) {
			this.roles = new RbacModel().getUserRoles(this.user.getId());
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
		this.checkUser();

		// call rbac model to get roles if not exists
		if (this.auths == null || this.auths.isEmpty()) {
			this.auths = new RbacModel().getUserAuths(this.user.getId());
		}

		return this.auths;
	}

	/**
	 * assign new roles to current user
	 * 
	 * @param roles
	 * @throws CoreException
	 */
	public void assignNewRoles(List<String> roles) throws CoreException {
		this.checkUser();

		// call rbac model
		new RbacModel().assignUserWithNewRoles(this.user.getId(), roles);
	}
}
