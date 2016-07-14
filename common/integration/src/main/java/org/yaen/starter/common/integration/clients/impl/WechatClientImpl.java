package org.yaen.starter.common.integration.clients.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.integration.clients.WechatClient;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.HttpUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * wechat client implement
 * 
 * @author Yaen 2015年12月2日下午10:58:19
 */
@Service
public class WechatClientImpl implements WechatClient {

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
	 * @see org.yaen.starter.common.integration.clients.WechatClient#checkSignature(java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		AssertUtil.notBlank(token);
		AssertUtil.notBlank(signature);
		AssertUtil.notBlank(timestamp);
		AssertUtil.notBlank(nonce);

		// add token, timestamp, nonce to array, and sort
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);

		// combine together
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}

		// // do sha-1
		// MessageDigest md = MessageDigest.getInstance("SHA-1");
		//
		// byte[] digest = md.digest(content.toString().getBytes());
		// String tmpStr = StringUtil.byteToStr(digest);

		String tmpStr = DigestUtils.sha1Hex(content.toString());

		// should be same
		return StringUtil.equals(tmpStr, signature.toUpperCase());
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#getAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getAccessToken(String appid, String appsecret) throws Exception {

		// make url
		String url = ACCESS_TOKEN_API.replace("APPID", appid).replace("APPSECRET", appsecret);

		// do https get
		return HttpUtil.httpsGet(url);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#getUserInfo(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getUserInfo(String accessToken, String openId) throws Exception {

		// make url
		String url = USER_INFO_API.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);

		// do https get
		return HttpUtil.httpsGet(url);
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
		return HttpUtil.httpsPost(url, param);
	}

	/**
	 * @see org.yaen.starter.common.integration.clients.WechatClient#createMenu(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject createMenu(String accessToken, String menuJSONString) throws Exception {

		// make url
		String url = MENU_CREATE_API.replace("ACCESS_TOKEN", accessToken);

		// do https post
		return HttpUtil.httpsPost(url, menuJSONString);
	}

	public static void main(String[] args) {
		WechatClientImpl s = new WechatClientImpl();
		try {
			// System.out.println(s.getAccessToken("wx67be379381b004de", "af8e086dee051827251454a3e7dc7069"));
			// sprPi802pRK4-OF5j1qryAvzrXgTE9MztOJrP88eUiZMBssLkVbM5Uknj48UPh0NrGUfYWGPo31AJNI49sRoornebS17_jD3CRvvDqUmS4tW2vZ7SC4q4MJ6N7qGkl8YGRUgAJALWZ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
