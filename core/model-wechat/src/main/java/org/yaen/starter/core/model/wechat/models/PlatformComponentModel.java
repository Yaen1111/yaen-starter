package org.yaen.starter.core.model.wechat.models;

/**
 * the wechat platform binded with component model(single use, will not refresh cache)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformComponentModel extends PlatformModel {

	/**
	 * constructor with access token, mainly used by component platform
	 * 
	 * @param appid
	 * @param accessToken
	 */
	public PlatformComponentModel(String appid, String accessToken) {
		super();

		this.appid = appid;
		this.accessToken = accessToken;
	}

}
