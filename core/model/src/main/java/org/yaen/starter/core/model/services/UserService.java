package org.yaen.starter.core.model.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.yaen.starter.common.dal.entities.user.RoleEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.core.model.models.user.UserModel;

/**
 * user service to handle user model
 * 
 * @author Yaen 2016年7月3日下午1:02:08
 */
public interface UserService {

	/**
	 * load user by username(id)
	 * 
	 * @param model
	 * @param username
	 * @throws CoreException
	 * @throws DataNotExistsException
	 * @throws DuplicateDataException
	 */
	void loadModel(UserModel model, String username)
			throws CoreException, DataNotExistsException, DuplicateDataException;

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
	void registerNewUser(String username, String password) throws CoreException, DuplicateDataException;

	/**
	 * check user credentials
	 * 
	 * @param password
	 * @param passwordHash
	 * @param passwordSalt
	 * @return
	 */
	boolean checkUserCredentials(String password, String passwordHash, String passwordSalt);

	/**
	 * create new role with given id
	 * 
	 * @param role
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	void createNewRole(RoleEntity role) throws CoreException, DuplicateDataException;

	/**
	 * select all role list
	 * 
	 * @return
	 * @throws CoreException
	 */
	List<RoleEntity> getRoleListAll() throws CoreException;

	/**
	 * get user role ids
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 */
	Set<String> getUserRoles(String username) throws CoreException;

	/**
	 * assign new roles to user
	 * 
	 * @param username
	 * @param roles
	 * @throws CoreException
	 */
	void assignUserWithNewRoles(String username, Collection<String> roles) throws CoreException;

	/**
	 * get auth ids for role
	 * 
	 * @param roleId
	 * @return
	 * @throws CoreException
	 */
	Set<String> getRoleAuths(String roleId) throws CoreException;

	/**
	 * assign new auths to role
	 * 
	 * @param roleId
	 * @param auths
	 * @throws CoreException
	 */
	void assignRoleWithNewAuths(String roleId, Collection<String> auths) throws CoreException;

	/**
	 * get user auth ids
	 * <p>
	 * get user roles, then get all auths
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 */
	Set<String> getUserAuths(String username) throws CoreException;

}