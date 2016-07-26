package org.yaen.starter.core.model.wechat.models;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.ParseUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.ComponentMessageEntity;
import org.yaen.starter.core.model.wechat.enums.InfoTypes;
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
	 */
	public ComponentMessageModel(ProxyService proxy) {
		super(proxy, new ComponentMessageEntity());
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
			String xml = StringUtil.readString(reader);

			// decrypt
			String resp = this.decryptXml(xml, WechatPropertiesUtil.getComponentAppid(),
					WechatPropertiesUtil.getComponentToken(), WechatPropertiesUtil.getComponentAesKey(), msgSignature,
					timeStamp, nonce);

			// get map
			map = this.getMapFormXml(new StringReader(resp));

		} catch (AesException ex) {
			throw new CoreException("aes error", ex);
		} catch (IOException ex) {
			throw new CoreException("read request body error", ex);
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
			msg.setAuthorizationCode(map.get("AuthorizationCode"));
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
	 * @see org.yaen.starter.core.model.wechat.models.BaseMessageModel#processMessage(java.lang.String)
	 */
	@Override
	public void processMessage(String appid) throws DataException, CommonException, CoreException {

		ComponentMessageEntity msg = this.getEntity();

		if (msg.getInfoType() != null) {
			switch (msg.getInfoType()) {
			case InfoTypes.COMPONENT_VERIFY_TICKET:
			// verify ticket, update component
			{
				ComponentModel component = new ComponentModel(this.proxy);

				// try get one, if not exists, create one
				component.loadOrCreateById(WechatPropertiesUtil.getComponentAppid());

				// set ticket
				component.getEntity().setComponentVerifyTicket(msg.getComponentVerifyTicket());
				component.getEntity().setComponentVerifyTicketCreate(msg.getCreateTime());

				// save
				component.saveById();
			}
				break;
			case InfoTypes.AUTHORIZED:
			case InfoTypes.UPDATEAUTHORIZED:
			// platform auth with auth code
			{
				PlatformModel platform = new PlatformModel(this.proxy);

				// try get one, if not exists, create one
				platform.loadOrCreateById(msg.getAuthorizerAppid());

				// set ticket
				platform.getEntity().setAuthorizationCode(msg.getAuthorizationCode());
				platform.getEntity().setAuthorizationCodeExpiredTime(msg.getAuthorizationCodeExpiredTime());

				// save
				platform.saveById();
			}
				break;
			default:
				// ignore others
				break;
			}
		}
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.models.BaseMessageModel#makeResponse()
	 */
	@Override
	public String makeResponse() throws DataException, CommonException, CoreException {
		return "success";
	}

}
