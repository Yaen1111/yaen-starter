package org.yaen.starter.core.model.wechat.models;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformComponentMessageEntity;
import org.yaen.starter.core.model.wechat.services.WechatService;
import org.yaen.starter.core.model.wechat.utils.WechatPropertiesUtil;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import lombok.Getter;

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

	/** if has appid, this is for component-binded platform, otherwise is self */
	@Getter
	private String appid;

	/**
	 * @param proxy
	 * @param service
	 * @param appid
	 */
	public PlatformComponentMessageModel(ProxyService proxy, WechatService service, String appid) {
		super(proxy, service, new PlatformComponentMessageEntity());

		this.appid = appid;
	}

	/**
	 * load from xml with encrpyt
	 * 
	 * @param reader
	 * @param msgSignature
	 * @param timeStamp
	 * @param nonce
	 * @throws CoreException
	 * @throws AesException
	 */
	public void loadFromXml(Reader reader, String msgSignature, String timeStamp, String nonce)
			throws CoreException, AesException {
		// need decrypt
		String postData;
		try {
			postData = StringUtil.readString(reader);
		} catch (IOException ex) {
			throw new CoreException("read post data error", ex);
		}

		// make crypt
		WXBizMsgCrypt pc = new WXBizMsgCrypt(WechatPropertiesUtil.getComponentToken(),
				WechatPropertiesUtil.getComponentSymmetricKey(), this.appid);

		// decrypt entire xml
		String xml = pc.decryptMsg(msgSignature, timeStamp, nonce, postData);

		// load as normal
		this.loadFromXml(new StringReader(xml));
	}

	/**
	 * @see org.yaen.starter.core.model.wechat.models.PlatformMessageModel#makeResponse()
	 */
	@Override
	public PlatformMessageModel makeResponse() throws CoreException {
		// TODO Auto-generated method stub
		return super.makeResponse();
	}

}
