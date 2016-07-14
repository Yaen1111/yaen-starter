package org.yaen.starter.core.model.models.wechat;

import org.yaen.starter.common.dal.entities.wechat.MessageEntity;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;

/**
 * the wechat message model, mostly for user request and server response
 * 
 * @author Yaen 2016年7月14日下午2:07:17
 */
public class MessageModel extends TwoModel<MessageEntity> {

	/**
	 * @param proxy
	 * @param sample
	 */
	public MessageModel(ProxyService proxy, MessageEntity sample) {
		super(proxy, sample);
	}

}
