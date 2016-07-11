package org.yaen.starter.common.integration.clients;

import com.alibaba.fastjson.JSONObject;

/**
 * wechat client
 * 
 * @author Yaen 2016年6月11日下午7:59:58
 */
public interface WechatClient {

	/**
	 * check signature
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	boolean checkSignature(String token, String signature, String timestamp, String nonce);

	/**
	 * get access token from wechat server, return json object
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	JSONObject getAccessToken(String appid, String appsecret) throws Exception;

	/**
	 * create menu, return the result json object
	 * 
	 * @param accessToken
	 * @param menuJsonString
	 * @return
	 * @throws Exception
	 */
	JSONObject createMenu(String accessToken, String menuJsonString) throws Exception;
}
