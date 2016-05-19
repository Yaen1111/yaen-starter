package org.yaen.starter.biz.wechat.services;

import org.yaen.starter.biz.shared.objects.UserDTO;

/**
 * wechat service
 * 
 * @author Yaen 2016年5月17日下午3:58:01
 */
public interface WechatService {

	/**
	 * register new user
	 * 
	 * @param user
	 * @return
	 */
	long RegisterNewUser(UserDTO dto) throws Exception;
}
