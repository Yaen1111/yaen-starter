package org.yaen.starter.core.model.wechat.enums;

/**
 * wechat event types definition, message type should be EVENT
 * 
 * @author Yaen 2016年1月13日上午11:52:48
 */
public final class EventTypes {

	/** subscribe(be user) */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/** unsubscribe(cancel user) */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/** menu be clicked */
	public static final String EVENT_TYPE_CLICK = "CLICK";

}
