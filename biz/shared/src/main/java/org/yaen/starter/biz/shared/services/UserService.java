package org.yaen.starter.biz.shared.services;

import org.yaen.starter.biz.shared.objects.UserDTO;

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
	long RegisterNewUser(UserDTO dto) throws Exception;
}
