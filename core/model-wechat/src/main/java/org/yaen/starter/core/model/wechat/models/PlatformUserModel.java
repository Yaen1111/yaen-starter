package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformUserEntity;

import com.alibaba.fastjson.JSONObject;

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
	 * load user by openid + appid, if not exits, create one
	 * 
	 * @param openId
	 * @param appId
	 * @throws CommonException
	 * @throws DataException
	 */
	public void loadOrCreateByOpenId(String openId, String appId) throws DataException, CommonException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		try {
			// set info
			this.getEntity().setOpenId(openId);
			this.getEntity().setAppId(appId);
			this.fillEntityByUniqueFields(this.getEntity(), new String[] { "openId", "appId" });
		} catch (DataNotExistsException ex) {
			// set info again
			this.getEntity().setOpenId(openId);
			this.getEntity().setAppId(appId);
			this.insertEntity(this.getEntity());
		}
	}

	/**
	 * read user info from json, which is from api
	 * 
	 * @param json
	 */
	public void readUserInfo(JSONObject json) {
		AssertUtil.notNull(json);

		PlatformUserEntity user = this.getEntity();

		user.setSubscribe(ParseUtil.tryParseInt(json.getString("subscribe"), 0));
		user.setNickname(json.getString("nickname"));
		user.setSex(ParseUtil.tryParseInt(json.getString("sex"), 0));
		user.setLanguage(json.getString("language"));
		user.setCity(json.getString("city"));
		user.setProvince(json.getString("province"));
		user.setCountry(json.getString("country"));
		user.setHeadimgurl(json.getString("headimgurl"));
		user.setSubscribeTime(ParseUtil.tryParseLong(json.getString("subscribe_time"), 0L));
		user.setUnionId(json.getString("unionid"));
		user.setRemark(json.getString("remark"));
		user.setGroupId(ParseUtil.tryParseInt(json.getString("groupid"), 0));
		user.setTagIdList(json.getString("tagid_list"));
	}

}
