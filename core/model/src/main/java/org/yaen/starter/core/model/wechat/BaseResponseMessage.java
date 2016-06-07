package org.yaen.starter.core.model.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * base response message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:43:22
 */
@Getter
@Setter
public class BaseResponseMessage {

	/** to open id */
	private String toUserName;

	/** from wechat platform */
	private String fromUserName;

	/** message create time */
	private long createTime;

	/** message type (text/music/news) */
	private String msgType;

	/** if flag 0x0001 is set, the received message will be marked */
	private int funcFlag;

}
