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
	 * @return
	 */
	long RegisterNewUser(UserDTO dto) throws BizException;

	void Login(UserDTO dto) throws BizException;

	void Logout(UserDTO dto) throws BizException;
}
