package org.yaen.starter.test.test;

import java.util.HashMap;
import java.util.Map;

import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.integration.clients.impl.HttpClientImpl;
import org.yaen.starter.common.integration.clients.impl.WechatClientImpl;

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

		WechatClientImpl s = new WechatClientImpl();
		try {
			System.out.println(s.getAccessToken("wx67be379381b004de", "af8e086dee051827251454a3e7dc7069"));
			// sprPi802pRK4-OF5j1qryAvzrXgTE9MztOJrP88eUiZMBssLkVbM5Uknj48UPh0NrGUfYWGPo31AJNI49sRoornebS17_jD3CRvvDqUmS4tW2vZ7SC4q4MJ6N7qGkl8YGRUgAJALWZ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
