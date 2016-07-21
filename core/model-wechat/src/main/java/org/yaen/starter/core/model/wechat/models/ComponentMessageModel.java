package org.yaen.starter.core.model.wechat.models;

import java.io.Reader;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentMessageEntity;
import org.yaen.starter.core.model.wechat.enums.InfoTypes;
import org.yaen.starter.core.model.wechat.services.WechatService;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.qq.weixin.mp.aes.AesException;

import lombok.extern.slf4j.Slf4j;

/**
 * the wechat component message model, mostly for verify ticket and message
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
@Slf4j
public class ComponentMessageModel extends BaseMessageModel {

	@Override
	public ComponentMessageEntity getEntity() {
		return (ComponentMessageEntity) super.getEntity();
	}

	/**
	 * constructor
	 * 
	 * @param proxy
	 * @param service
	 */
	public ComponentMessageModel(ProxyService proxy, WechatService service) {
		super(proxy, service, new ComponentMessageEntity());
	}

	/**
	 * load from xml with decrpyt
	 * 
	 * @param reader
	 * @param msgSignature
	 * @param timeStamp
	 * @param nonce
	 * @throws CoreException
	 */
	public void loadFromXml(Reader reader, String msgSignature, String timeStamp, String nonce) throws CoreException {
		AssertUtil.notNull(reader);

		// the value map
		Map<String, String> map = null;

		try {
			// decrypt
			Reader reader2 = this.decryptXmlReader(reader, WechatPropertiesUtil.getComponentAppid(),
					WechatPropertiesUtil.getComponentToken(), WechatPropertiesUtil.getComponentAesKey(), msgSignature,
					timeStamp, nonce);

			// get map
			map = this.getMapFormXml(reader2);

		} catch (AesException ex) {
			throw new CoreException("aes error", ex);
		}

		// get entity and fill data
		ComponentMessageEntity msg = this.getEntity();

		// main info
		msg.setAppid(map.get("AppId"));
		msg.setCreateTime(ParseUtil.tryParseLong(map.get("CreateTime"), 0L));
		msg.setInfoType(map.get("InfoType"));

		// make switch not null
		if (msg.getInfoType() == null) {
			msg.setInfoType("");
		}

		switch (msg.getInfoType()) {
		case InfoTypes.COMPONENT_VERIFY_TICKET:
			// ticket every 10 minutes
			msg.setComponentVerifyTicket(map.get("ComponentVerifyTicket"));
			break;
		case InfoTypes.AUTHORIZED:
		case InfoTypes.UPDATEAUTHORIZED:
			// authorized ok
			msg.setAuthorizerAppid(map.get("AuthorizerAppid"));
			msg.setComponentVerifyTicket(map.get("AuthorizationCode"));
			msg.setAuthorizationCodeExpiredTime(ParseUtil.tryParseLong(map.get("AuthorizationCodeExpiredTime"), 0L));
			break;
		case InfoTypes.UNAUTHORIZED:
			// authorize canceled/expired
			msg.setAuthorizerAppid(map.get("AuthorizerAppid"));
			break;
		default:
			log.error("unknown info type: {}", msg.getInfoType());
			break;
		}

		// done
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.models.BaseMessageModel#makeResponse()
	 */
	@Override
	public String makeResponse() throws CoreException {
		// return success anyway
		return "success";
	}

}
