package org.yaen.starter.core.model.services;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.yaen.starter.core.model.models.wechat.AccessToken;
import org.yaen.starter.core.model.models.wechat.menus.Menu;
import org.yaen.starter.core.model.models.wechat.responses.MusicResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.NewsResponseMessage;
import org.yaen.starter.core.model.models.wechat.responses.TextResponseMessage;

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
	 * get access token, can be cached
	 * 
	 * @return
	 * @throws Exception
	 */
	AccessToken getAccessToken() throws Exception;

	/**
	 * create menu
	 * 
	 * @param menu
	 * @param accessToken
	 * @throws Exception
	 */
	void createMenu(Menu menu, AccessToken accessToken) throws Exception;

	/**
	 * parse xml from input stream
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	Map<String, String> parseXml(InputStream is) throws Exception;

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
}
