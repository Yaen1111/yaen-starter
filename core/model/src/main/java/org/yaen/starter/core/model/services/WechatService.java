package org.yaen.starter.core.model.services;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.dal.entities.wechat.MenuEntity;
import org.yaen.starter.common.data.exceptions.CoreException;
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
	 * get access token, can be cached, typically 2 hours
	 * 
	 * @return
	 * @throws CoreException
	 */
	AccessToken getAccessToken() throws CoreException;

	/**
	 * create menu
	 * 
	 * @param menu
	 * @param accessToken
	 * @throws CoreException
	 */
	void createMenu(String menu, AccessToken accessToken) throws CoreException;

	/**
	 * get menu entity list by group name
	 * 
	 * @param groupName
	 * @return
	 * @throws CoreException
	 */
	List<MenuEntity> getMenuEntityList(String groupName) throws CoreException;

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
}
