package org.yaen.starter.common.integration.clients.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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

	// 获取access_token的接口地址（GET） 限200（次/天）
	/** get access token url (GET), limited to 200/day */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 菜单创建（POST） 限100（次/天）
	/** create menu url (POST), limited to 100/day */
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 图文响应接口（POST） 限100（次/天）
	/** picture response url (POST), limited to 100/day */
	public final static String PIC_RESPONSE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * verify signature
	 * 
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException {
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

		// do sha-1
		MessageDigest md = MessageDigest.getInstance("SHA-1");

		byte[] digest = md.digest(content.toString().getBytes());
		String tmpStr = StringUtil.byteToStr(digest);

		// should be same
		return StringUtil.equals(tmpStr, signature.toUpperCase());
	}

	/**
	 * get access token, the token will expire in 2 hours typically, return json object
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getAccessToken(String appid, String appsecret) throws Exception {

		// make url
		String url = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);

		// do https get
		return HttpUtil.httpsGet(url);
	}

	/**
	 * create menu
	 * 
	 * @param accessToken
	 * @param menuJsonString
	 * @throws Exception
	 */
	@Override
	public JSONObject createMenu(String accessToken, String menuJsonString) throws Exception {

		// make url
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);

		// replace null, as we no not need it
		String menu = menuJsonString.replaceAll(",null", "");

		// do https post
		return HttpUtil.httpsPost(url, menu);
	}

}
