package org.yaen.starter.common.integration.clients;

import java.util.List;

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
	String getAccessToken(String appid, String appsecret) throws Exception;

	/**
	 * create menu, return the result json string
	 * 
	 * @param accessToken
	 * @param menuJsonString
	 * @return
	 * @throws Exception
	 */
	String createMenu(String accessToken, String menuJsonString) throws Exception;

	/**
	 * get user info by openid
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	String getUserInfo(String accessToken, String openId) throws Exception;

	/**
	 * get user info by batch
	 * 
	 * @param accessToken
	 * @param openIdList
	 * @return
	 * @throws Exception
	 */
	JSONObject getUserInfoBatch(String accessToken, List<String> openIdList) throws Exception;
}
