package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformEntity;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import lombok.Getter;

/**
 * the wechat platform model(single use, will not refresh cache)
 * <p>
 * deals with most platform operations, only need access_token(both for platform or component-platform)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformModel extends TwoModel {

	@Override
	public PlatformEntity getEntity() {
		return (PlatformEntity) super.getEntity();
	}

	/** the current appid */
	@Getter
	protected String appid;

	/** the current access token */
	// @Getter
	protected String accessToken;

	/**
	 * constructor for normal platform
	 * 
	 * @param proxy
	 */
	public PlatformModel(ProxyService proxy) {
		super(proxy, new PlatformEntity());

		this.appid = WechatPropertiesUtil.getAppid();
	}

	/**
	 * getter for accessToken
	 * 
	 * @return
	 */
	public String getAccessToken() {
		if (this.accessToken == null) {
			// get access token
		}
		return this.accessToken;
	}

}
