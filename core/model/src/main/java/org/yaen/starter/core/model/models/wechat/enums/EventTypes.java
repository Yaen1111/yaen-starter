package org.yaen.starter.core.model.models.wechat.enums;

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

	/** qrcode is scanned(the user is already subscribed, otherwise will fire subscribe event) */
	public static final String EVENT_TYPE_SCAN = "SCAN";

	/** report location */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";

	/** wifi connected */
	public static final String EVENT_TYPE_WIFI_CONNECTED = "WifiConnected";

}
