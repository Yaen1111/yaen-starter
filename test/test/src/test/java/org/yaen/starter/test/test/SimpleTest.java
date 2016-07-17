package org.yaen.starter.test.test;

import java.util.HashMap;
import java.util.Map;

import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.integration.clients.impl.HttpClientImpl;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class SimpleTest {

	public static void main(String[] args) {
		// String apiurl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";

		HttpClient httpClient = new HttpClientImpl();

		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "sendOnce");
		map.put("ac", "1001@501308820001");
		map.put("authkey", "7ED1251EA7D481F70DB1C700B07AE325");
		map.put("m", "15858147890");
		map.put("c", "test sms");
		map.put("encode", "utf-8");

		String url = "http://smsapi.c123.cn/OpenPlatform/OpenApi?action=sendOnce&ac=1001@501308820001&authkey=7ED1251EA7D481F70DB1C700B07AE325&cgid=52&c=testsms&m=13750835052";

		try {
			String content = httpClient.httpGet(url);
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
