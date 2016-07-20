package org.yaen.starter.common.integration.clients.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.integration.clients.WechatClient;

import com.alibaba.fastjson.JSONObject;

/**
 * wechat client implement
 * 
 * @author Yaen 2015年12月2日下午10:58:19
 */
@Service
public class WechatClientImpl implements WechatClient {

	@Autowired
	private HttpClient httpClient;

	// documents: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319&token=&lang=zh_CN

	/** get access token url (GET), limited to 200/day */
	public final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/** get user info (GET) */
	public final static String USER_INFO_API = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/** get user info (POST) */
	public final static String USER_INFO_BATCHGET_API = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";

	/** create menu url (POST), limited to 100/day */
	public final static String MENU_CREATE_API = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/** picture response url (POST), limited to 100/day */
	public final static String PIC_RESPONSE_API = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#getAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public String getAccessToken(String appid, String appsecret) throws Exception {

		// make url
		String url = ACCESS_TOKEN_API.replace("APPID", appid).replace("APPSECRET", appsecret);

		// do https get
		return this.httpClient.httpsGet(url);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#getUserInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public String getUserInfo(String accessToken, String openId) throws Exception {

		// make url
		String url = USER_INFO_API.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);

		// do https get
		return this.httpClient.httpsGet(url);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#getUserInfoBatch(java.lang.String, java.util.List)
	 */
	@Override
	public JSONObject getUserInfoBatch(String accessToken, List<String> openIdList) throws Exception {

		// make url
		String url = USER_INFO_API.replace("ACCESS_TOKEN", accessToken);

		// make param
		Map<String, Object> param = new HashMap<String, Object>();

		// TODO

		// do https get
		return this.httpClient.httpsPostAsJson(url, param);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#createMenu(java.lang.String, java.lang.String)
	 */
	@Override
	public String createMenu(String accessToken, String menuJSONString) throws Exception {

		// make url
		String url = MENU_CREATE_API.replace("ACCESS_TOKEN", accessToken);

		// do https post
		return this.httpClient.httpsPost(url, menuJSONString);
	}

}
