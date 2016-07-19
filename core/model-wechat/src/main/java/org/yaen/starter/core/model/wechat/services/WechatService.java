package org.yaen.starter.core.model.wechat.services;

import java.security.NoSuchAlgorithmException;

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
	 * check signature
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException;

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
	 * make response according to the request
	 * 
	 * @param requestMessage
	 * @return
	 */
	PlatformMessageModel makeResponse(PlatformMessageModel requestMessage);

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
