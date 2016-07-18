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

	/** menu link be clicked */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	/** qrcode is scanned(the user is already subscribed, otherwise will fire subscribe event) */
	public static final String EVENT_TYPE_SCAN = "SCAN";

	/** report location */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";

	/** wifi connected */
	public static final String EVENT_TYPE_WIFI_CONNECTED = "WifiConnected";

	/** qualification verify success, and get all api, but lost 300 rmb !! */
	public static final String EVENT_TYPE_QUALIFICATION_VERIFY_SUCCESS = "qualification_verify_success";

	/** qualification verify fail, get no api, but lost 300 rmb !! */
	public static final String EVENT_TYPE_QUALIFICATION_VERIFY_FAIL = "qualification_verify_fail";

	/** naming verify success */
	public static final String EVENT_TYPE_NAMING_VERIFY_SUCCESS = "naming_verify_success";

	/** naming verify fail */
	public static final String EVENT_TYPE_NAMING_VERIFY_FAIL = "naming_verify_fail";

	/** need renew... */
	public static final String EVENT_TYPE_ANNUAL_RENEW = "annual_renew";

	/** the verify is expired, need another 300 rmb !! */
	public static final String EVENT_TYPE_VERIFY_EXPIRED = "verify_expired";

	/** poi(shop) check notify */
	public static final String EVENT_TYPE_POI_CHECK_NOTIFY = "poi_check_notify";

}
