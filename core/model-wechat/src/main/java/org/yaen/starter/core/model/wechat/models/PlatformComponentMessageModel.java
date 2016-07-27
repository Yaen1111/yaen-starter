package org.yaen.starter.core.model.wechat.models;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformComponentMessageEntity;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.qq.weixin.mp.aes.AesException;

/**
 * the wechat platform component message model, mostly for user request and server response
 * <p>
 * with component, message is exactly the same
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
public class PlatformComponentMessageModel extends PlatformMessageModel {

	@Override
	public PlatformComponentMessageEntity getEntity() {
		return (PlatformComponentMessageEntity) super.getEntity();
	}

	/**
	 * @param proxy
	 * @param appid
	 */
	public PlatformComponentMessageModel(ProxyService proxy, String appid) {
		super(proxy, new PlatformComponentMessageEntity());

		// set appid to platform
		this.appid = appid;
	}

	/**
	 * load from xml with decrypt
	 * 
	 * @param reader
	 * @param msgSignature
	 * @param timeStamp
	 * @param nonce
	 * @throws CoreException
	 * @throws AesException
	 */
	public void loadFromXml(Reader reader, String msgSignature, String timeStamp, String nonce) throws CoreException {
		AssertUtil.notNull(reader);

		try {
			String xml = StringUtil.readString(reader);

			// decrypt
			String resp = this.decryptXml(xml, WechatPropertiesUtil.getComponentAppid(),
					WechatPropertiesUtil.getComponentToken(), WechatPropertiesUtil.getComponentAesKey(), msgSignature,
					timeStamp, nonce);

			// load as normal
			this.loadFromXml(new StringReader(resp));

		} catch (AesException ex) {
			throw new CoreException("aes error", ex);
		} catch (IOException ex) {
			throw new CoreException("read from request body error", ex);
		}
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.models.PlatformMessageModel#makeResponse()
	 */
	@Override
	public String makeResponse() throws CoreException {

		String resp = super.makeResponse();

		// need encrypt
		try {
			String resp2 = this.encryptXml(resp, WechatPropertiesUtil.getComponentAppid(),
					WechatPropertiesUtil.getComponentToken(), WechatPropertiesUtil.getComponentAesKey());

			return resp2;
		} catch (AesException ex) {
			throw new CoreException("aes error", ex);
		}
	}

}
