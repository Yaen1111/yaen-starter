package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformEntity;

/**
 * the wechat platform binded with component model(single use, will not refresh cache)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformComponentModel extends PlatformModel {

	/**
	 * constructor with access token, mainly used by component platform
	 * 
	 * @param proxy
	 * @param appid
	 * @param accessToken
	 */
	public PlatformComponentModel(ProxyService proxy, PlatformEntity entity, String appid, String accessToken) {
		super(proxy, entity, appid, accessToken);
	}

}
