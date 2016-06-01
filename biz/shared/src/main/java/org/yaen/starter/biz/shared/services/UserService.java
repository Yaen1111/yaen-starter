package org.yaen.starter.biz.shared.services;

import java.util.List;

import org.yaen.starter.biz.shared.objects.RoleDTO;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.common.data.exceptions.BizException;

/**
 * user service
 * 
 * @author Yaen 2016年5月17日下午3:58:01
 */
public interface UserService {

	/**
	 * register new user
	 * 
	 * @param user
	 */
	void registerNewUser(UserDTO user) throws BizException;

	/**
	 * get user by name, no password check
	 * 
	 * @param username
	 * @return
	 * @throws BizException
	 */
	UserDTO getUserByName(String username) throws BizException;

	/**
	 * check user login, use plain password/hash/salt to check, no db access
	 * 
	 * @param password
	 * @param passwordHash
	 * @param passwordSalt
	 * @return
	 */
	boolean checkUserCredentials(String password, String passwordHash, String passwordSalt);

	void Logout(UserDTO user) throws BizException;

	/**
	 * create new role
	 * 
	 * @param role
	 * @throws BizException
	 */
	void createNewRole(RoleDTO role) throws BizException;

	/**
	 * get all role list
	 * 
	 * @return
	 * @throws BizException
	 */
	List<RoleDTO> getRoleList() throws BizException;

	/**
	 * get user role names
	 * 
	 * @param username
	 * @return
	 * @throws BizException
	 */
	List<String> getUserRoles(String username) throws BizException;

	/**
	 * assign user roles, may add and remove
	 * 
	 * @param username
	 * @param roles
	 * @throws BizException
	 */
	void assignUserRoles(String username, List<String> roles) throws BizException;

}
