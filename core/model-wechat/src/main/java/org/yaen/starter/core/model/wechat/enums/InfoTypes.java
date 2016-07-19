package org.yaen.starter.core.model.wechat.enums;

/**
 * wechat component info types definition
 * 
 * @author Yaen 2016年7月19日下午1:55:59
 */
public final class InfoTypes {

	/** verify ticket, send every 10 minutes */
	public static final String COMPONENT_VERIFY_TICKET = "component_verify_ticket";

	/** authorize canceled/expired */
	public static final String UNAUTHORIZED = "unauthorized";

	/** authorize ok */
	public static final String AUTHORIZED = "authorized";

	/** authorize updated (annually) */
	public static final String UPDATEAUTHORIZED = "updateauthorized";

}
