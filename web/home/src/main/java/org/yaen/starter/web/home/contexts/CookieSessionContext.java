package org.yaen.starter.web.home.contexts;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * session context using cookie, used for save session id
 * 
 * @author Yaen 2016年5月19日下午4:32:07
 */
public class CookieSessionContext {

	/** cookie name, default to SESSIONID */
	@Getter
	@Setter
	private String cookieName = "SESSIONID";

	/** cookie domain */
	@Getter
	@Setter
	private String cookieDomain = "";

	/** cookie path, / for all */
	@Getter
	@Setter
	private String cookiePath = "/";

	/** cookie http only */
	@Getter
	@Setter
	private boolean cookieHttpOnly = true;

	/** cookie use secure */
	@Getter
	@Setter
	private boolean cookieSecure = true;

	/** cookie max age, -1 for infinite */
	@Getter
	@Setter
	private int cookieMaxAge = -1;

	/**
	 * get session id from cookie
	 * 
	 * @param request
	 * @return
	 */
	public String getSessionId(HttpServletRequest request) {
		return WebUtil.getCookieValue(request, cookieName, null);
	}

	/**
	 * set session into cookie
	 * 
	 * @param response
	 * @param sessionId
	 * @param expired
	 */
	public void setSessionId(HttpServletResponse response, String sessionId, boolean expired) {
		Cookie cookie = new Cookie(this.cookieName, sessionId);

		if (StringUtil.isNotEmpty(this.cookieDomain)) {
			cookie.setDomain(this.cookieDomain);
		}

		if (StringUtil.isNotEmpty(this.cookiePath)) {
			cookie.setPath(this.cookiePath);
		} else {
			cookie.setPath("/");
		}

		if (this.cookieHttpOnly) {
			cookie.setHttpOnly(true);
		} else {
			cookie.setHttpOnly(false);
		}

		if (this.cookieSecure) {
			cookie.setSecure(true);
		} else {
			cookie.setSecure(false);
		}

		// if expired, delete cookie
		if (expired) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(this.cookieMaxAge);
		}

		response.addCookie(cookie);
	}

}
