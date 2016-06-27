package org.yaen.starter.core.model.models.wechat.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * base request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:33:25
 */
@Getter
@Setter
public class BaseRequestMessage {

	/** to the wechat platform */
	private String toUserName;

	/** from open id */
	private String fromUserName;

	/** message create time */
	private long createTime;

	/** message type (text/image/location/link/event) */
	private String msgType;

	/** message id */
	private long msgId;

}
