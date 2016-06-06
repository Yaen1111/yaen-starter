package org.yaen.starter.common.data.objects.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * event request message
 * 
 * @author Yaen 2016年6月6日下午11:55:30
 */
@Getter
@Setter
public class EventRequestMessage extends BaseRequestMessage {

	/** event type (subscribe/unsubscribe/CLICK) */
	private String event;

	/** event key */
	private String eventKey;

}
