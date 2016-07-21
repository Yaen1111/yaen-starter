package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformUserEntity;

/**
 * the wechat user model, mostly for openid + appid
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class PlatformUserModel extends TwoModel {

	@Override
	public PlatformUserEntity getEntity() {
		return (PlatformUserEntity) super.getEntity();
	}

	/**
	 * constructor
	 * 
	 * @param proxy
	 */
	public PlatformUserModel(ProxyService proxy) {
		super(proxy, new PlatformUserEntity());
	}

	/**
	 * load user by openid + appid
	 * 
	 * @param openId
	 * @param appId
	 * @throws CommonException
	 * @throws DataException
	 */
	public void loadByOpenId(String openId, String appId) throws DataException, CommonException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		// set
		this.getEntity().setOpenId(openId);
		this.getEntity().setAppId(appId);

		this.fillEntityByUniqueFields(this.getEntity(), new String[] { "openId", "appId" });
	}

	public void readUserInfo() throws CoreException {
		this.check();

	}

}
