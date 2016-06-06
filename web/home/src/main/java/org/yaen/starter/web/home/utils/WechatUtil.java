package org.yaen.starter.web.home.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

/**
 * wechat util for signatures
 * 
 * @author Yaen 2016年6月6日下午10:17:52
 */
public class WechatUtil {

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
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce)
			throws NoSuchAlgorithmException {
		AssertUtil.notBlank(token);
		AssertUtil.notBlank(signature);
		AssertUtil.notBlank(timestamp);
		AssertUtil.notBlank(nonce);

		// add token, timestamp, nonce to array, and sourt
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
}
