package org.yaen.starter.core.model.wechat.services;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.wechat.models.PlatformMessageModel;
import org.yaen.starter.core.model.wechat.objects.AccessToken;

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
	 * check the content text is qq face
	 * 
	 * @param content
	 * @return
	 */
	boolean isQqFace(String content);

	/**
	 * format create time of wechat message to readable
	 * 
	 * @param createTime
	 * @return
	 */
	String formatTime(String createTime);

	/**
	 * make response string according to the request
	 * 
	 * @param requestMessage
	 * @return
	 */
	String makeResponse(PlatformMessageModel requestMessage);

	/**
	 * get access token of given appid
	 * 
	 * @param appId
	 * @return
	 * @throws CoreException
	 */
	AccessToken getAccessToken(String appId) throws CoreException;

	/**
	 * push menu to wechat server by appid
	 * 
	 * @param menu
	 * @param appId
	 * @throws CoreException
	 */
	void pushMenu(String menu, String appId) throws CoreException;

}
