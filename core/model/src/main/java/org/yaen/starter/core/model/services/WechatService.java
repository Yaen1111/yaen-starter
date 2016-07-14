package org.yaen.starter.core.model.services;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.models.wechat.MenuModel;
import org.yaen.starter.core.model.models.wechat.objects.AccessToken;
import org.yaen.starter.core.model.models.wechat.objects.MusicResponseMessage;
import org.yaen.starter.core.model.models.wechat.objects.NewsResponseMessage;
import org.yaen.starter.core.model.models.wechat.objects.TextResponseMessage;

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
	 * parse xml from input stream
	 * 
	 * @param is
	 * @return
	 * @throws CoreException
	 */
	Map<String, String> parseXml(InputStream is) throws CoreException;

	/**
	 * convert message to xml
	 * 
	 * @return
	 */
	String textMessageToXml(TextResponseMessage textResponseMessage);

	/**
	 * convert music message to xml
	 * 
	 * @param musicResponseMessage
	 * @return
	 */
	String musicMessageToXml(MusicResponseMessage musicResponseMessage);

	/**
	 * convert news message to xml
	 * 
	 * @param newsResponseMessage
	 * @return
	 */
	String newsMessageToXml(NewsResponseMessage newsResponseMessage);

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
	 * handle request
	 * 
	 * @param requestMap
	 * @return
	 */
	String handleRequest(Map<String, String> requestMap);

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
