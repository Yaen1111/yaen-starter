package org.yaen.starter.test.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.integration.clients.impl.HttpClientImpl;
import org.yaen.starter.common.integration.clients.impl.WechatClientImpl;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class SimpleTest {

	public static void main(String[] args) {

		String token = "HwIfAnIsToken";
		String timestamp = "1469026233";
		String nonce = "369228118";

		// add token, timestamp, nonce to array, and sort
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);

		// combine together
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}

		// do sha-1
		String tmpStr = DigestUtils.sha1Hex(content.toString());

	}
}
