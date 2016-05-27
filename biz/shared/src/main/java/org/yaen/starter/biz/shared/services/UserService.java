package org.yaen.starter.biz.shared.services;

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
	 * get user by name, fill user dto by info, no password check
	 * 
	 * @param user
	 * @throws BizException
	 */
	void getUserByName(UserDTO user) throws BizException;

	/**
	 * fast check user login, use username/hash/salt to check, no db access
	 * 
	 * @param user
	 * @return
	 * @throws BizException
	 */
	boolean checkUserCredentials(UserDTO user);

	void Logout(UserDTO user) throws BizException;
}
