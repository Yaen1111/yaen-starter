package org.yaen.starter.core.model.wechat.services;

import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;

/**
 * wechat service
 * 
 * @author Yaen 2016年6月11日下午8:07:55
 */
public interface WechatService {

	/**
	 * check signature, return echostr if ok
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	String checkSignature(String token, String signature, String timestamp, String nonce, String echostr);

	/**
	 * make response string according to the request
	 * 
	 * @param requestMessage
	 * @return
	 */
	String makeResponse(PlatformMessageModel requestMessage);

}
