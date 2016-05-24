package org.yaen.starter.web.home.contexts.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.contexts.SessionManager;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * stand alone session manager, only support single server
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class StandAloneSessionManager implements SessionManager {

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

	/** session max age */
	@Setter
	private int sessionMaxInactiveIntervalInSeconds = 108000;

	/** session repository, save session content */
	private SessionRepository<ExpiringSession> sessionRepository = new MapSessionRepository();

	/** ThreadLocal */
	private static ThreadLocal<ExpiringSession> local = new ThreadLocal<ExpiringSession>();

	/**
	 * get session id from cookie
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public String getSessionIdFromCookie(HttpServletRequest request) {
		return WebUtil.getCookieValue(request, cookieName, null);
	}

	/**
	 * set session id into cookie
	 * 
	 * @param response
	 * @param sessionId
	 * @param expired
	 */
	@Override
	public void setSessionIdToCookie(HttpServletResponse response, String sessionId, boolean expired) {
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

	/**
	 * get local session
	 * 
	 * @return
	 */
	@Override
	public ExpiringSession getLocalSession() {
		return local.get();
	}

	/**
	 * set local session
	 * 
	 * @param sessionId
	 */
	@Override
	public void setLocalSession(ExpiringSession session) {
		local.set(session);
	}

	/**
	 * delete local session, just set to null
	 */
	@Override
	public void deleteLocalSession() {
		local.set(null);
	}

	/**
	 * get global session by id
	 * 
	 * @param sessionId
	 * @return
	 */
	@Override
	public ExpiringSession getGlobalSession(String sessionId) {
		return this.sessionRepository.getSession(sessionId);
	}

	/**
	 * save global session
	 * 
	 * @param session
	 */
	@Override
	public void saveGlobalSession(ExpiringSession session) {
		session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
		this.sessionRepository.save(session);
	}

	/**
	 * delete global session by id
	 * 
	 * @param sessionId
	 */
	@Override
	public void deleteGlobalSession(String sessionId) {
		this.sessionRepository.delete(sessionId);
	}

	/**
	 * create global session
	 * 
	 * @return
	 */
	@Override
	public ExpiringSession createGlobalSession() {
		ExpiringSession session = this.sessionRepository.createSession();
		session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
		return session;
	}
}
