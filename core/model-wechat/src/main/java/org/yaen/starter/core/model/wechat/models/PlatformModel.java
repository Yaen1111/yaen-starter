package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import lombok.Getter;

/**
 * the wechat platform model(single use, will not refresh cache)
 * <p>
 * deals with most platform operations, only need access_token(both for platform or component-platform)
 * 
 * @author Yaen 2016年7月18日下午9:28:01
 */
public class PlatformModel extends OneModel {

	/** the current appid */
	@Getter
	private String appid;

	/** the current access token */
	@Getter
	private String accessToken;

	/**
	 * empty constructor for main platform
	 */
	public PlatformModel() {
		this.appid = WechatPropertiesUtil.getAppid();
		this.accessToken = null; // TODO
	}

	/**
	 * constructor with access token, mainly used by component platform
	 * 
	 * @param appid
	 * @param accessToken
	 */
	public PlatformModel(String appid, String accessToken) {
		this.appid = appid;
		this.accessToken = accessToken;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#check()
	 */
	@Override
	public void check() throws CoreException {
		if (StringUtil.isBlank(this.accessToken))
			throw new CoreException("accessToken is empty");
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#clear()
	 */
	@Override
	public void clear() {
		this.appid = null;
		this.accessToken = null;
	}

}
