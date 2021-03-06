package org.yaen.starter.core.model.wechat.utils;

import org.yaen.starter.common.util.utils.PropertiesUtil;

/**
 * wechat properties util, for some easy access, return null if not exists
 * 
 * @author Yaen 2016年7月14日下午5:52:30
 */
public class WechatPropertiesUtil {

	public static String getComponentAppid() {
		return PropertiesUtil.getProperty("component.appid");
	}

	public static String getComponentSecret() {
		return PropertiesUtil.getProperty("component.secret");
	}

	public static String getComponentToken() {
		return PropertiesUtil.getProperty("component.token");
	}

	public static String getComponentAesKey() {
		return PropertiesUtil.getProperty("component.aeskey");
	}

	public static String getAppid() {
		return PropertiesUtil.getProperty("platform.appid");
	}

	public static String getSecret() {
		return PropertiesUtil.getProperty("platform.secret");
	}

	public static String getToken() {
		return PropertiesUtil.getProperty("platform.token");
	}

	public static String getAuthUrl() {
		return PropertiesUtil.getProperty("platform.authurl");
	}

	public static String getWifiShopId() {
		return PropertiesUtil.getProperty("platform.wifi.shopid");
	}

	public static String getWifiSecret() {
		return PropertiesUtil.getProperty("platform.wifi.secret");
	}

	public static String getWifiSSID() {
		return PropertiesUtil.getProperty("platform.wifi.ssid");
	}

}
