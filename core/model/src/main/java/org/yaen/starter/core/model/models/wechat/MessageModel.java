package org.yaen.starter.core.model.models.wechat;

import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.models.wechat.objects.BaseRequestMessage;
import org.yaen.starter.core.model.models.wechat.objects.BaseResponseMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * the message model, with request and response
 * 
 * @author Yaen 2016年6月26日下午3:35:06
 */
public class MessageModel extends OneModel {

	/** the request */
	@Getter
	@Setter
	private BaseRequestMessage request;

	/** the response */
	@Getter
	@Setter
	private BaseResponseMessage response;

	/**
	 * empty constructor
	 */
	public MessageModel(){
		super("1.0.0");
	}
}
