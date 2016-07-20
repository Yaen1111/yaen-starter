package org.yaen.starter.core.model.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.core.model.entities.user.RoleEntity;

/**
 * user service to handle user model
 * 
 * @author Yaen 2016年7月3日下午1:02:08
 */
public interface UserService {

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
	 * @throws DataException 
	 */
	void createNewRole(RoleEntity role) throws CoreException, DataException;

	/**
	 * select all role list
	 * 
	 * @return
	 * @throws CoreException
	 * @throws DataException 
	 */
	List<RoleEntity> getRoleListAll() throws CoreException, DataException;

	/**
	 * get user role ids
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 * @throws DataException 
	 */
	Set<String> getUserRoles(String username) throws CoreException, DataException;

	/**
	 * assign new roles to user
	 * 
	 * @param username
	 * @param roles
	 * @throws CoreException
	 * @throws DataException 
	 */
	void assignUserWithNewRoles(String username, Collection<String> roles) throws CoreException, DataException;

	/**
	 * get auth ids for role
	 * 
	 * @param roleId
	 * @return
	 * @throws CoreException
	 * @throws DataException 
	 */
	Set<String> getRoleAuths(String roleId) throws CoreException, DataException;

	/**
	 * assign new auths to role
	 * 
	 * @param roleId
	 * @param auths
	 * @throws CoreException
	 * @throws DataException 
	 */
	void assignRoleWithNewAuths(String roleId, Collection<String> auths) throws CoreException, DataException;

	/**
	 * get user auth ids
	 * <p>
	 * get user roles, then get all auths
	 * 
	 * @param username
	 * @return
	 * @throws CoreException
	 * @throws DataException 
	 */
	Set<String> getUserAuths(String username) throws CoreException, DataException;

}
