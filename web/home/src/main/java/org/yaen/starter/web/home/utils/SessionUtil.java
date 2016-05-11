/**
 * 
 */
package org.yaen.starter.web.home.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.ExpiringSession;

/**
 * 
 * @author Yaen 2016年2月3日下午5:01:17
 */
public class SessionUtil {

	/**
	 * session name
	 */
	public static final String SESSION_NAME = "session";

	/**
	 * get session id by cookie
	 * 
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request) {
		return WebUtil.getCookieValue(request, SESSION_NAME);
	}

	/**
	 * set session
	 * 
	 * @param response
	 * @param session
	 */
	public static void setSession(HttpServletResponse response, ExpiringSession session) {
		Cookie cookie = new Cookie(SESSION_NAME, session.getId());

		// if (StringUtils.isNotEmpty(sessionCookieDomain)) {
		// cookie.setDomain(sessionCookieDomain);
		// }
		// if (StringUtils.isNotEmpty(sessionCookiePath)) {
		// cookie.setPath(sessionCookiePath);
		// } else {
		// cookie.setPath("/");
		// }
		// if (sessionCookieHttpOnly) {
		// cookie.setHttpOnly(true);
		// } else {
		// cookie.setHttpOnly(false);
		// }
		// if (sessionCookieSecure) {
		// cookie.setSecure(true);
		// } else {
		// cookie.setSecure(false);
		// }

		if (session.isExpired()) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(session.getMaxInactiveIntervalInSeconds());
		}

		response.addCookie(cookie);
	}

}
