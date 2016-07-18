package org.yaen.starter.core.model.wechat.utils;

import org.yaen.starter.common.util.utils.PropertiesUtil;

/**
 * wechat properties util, for some easy access, return null if not exists
 * 
 * @author Yaen 2016年7月14日下午5:52:30
 */
public class WechatPropertiesUtil {

	public static String getAppId() {
		return PropertiesUtil.getProperty("wechat.appid");
	}

	public static String getSecret() {
		return PropertiesUtil.getProperty("wechat.secret");
	}

	public static String getToken() {
		return PropertiesUtil.getProperty("wechat.token");
	}

	public static String getShopId() {
		return PropertiesUtil.getProperty("wechat.shopid");
	}

	public static String getAuthUrl() {
		return PropertiesUtil.getProperty("wechat.authurl");
	}

	public static String getWifiSSID() {
		return PropertiesUtil.getProperty("wechat.wifi.ssid");
	}

	public static String getWifiBSSID() {
		return PropertiesUtil.getProperty("wechat.wifi.bssid");
	}

}
