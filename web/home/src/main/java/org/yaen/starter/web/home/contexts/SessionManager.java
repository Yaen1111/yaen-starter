package org.yaen.starter.web.home.contexts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.ExpiringSession;

/**
 * session manager interface, manager full session life cycle, including cookie, global session, local session
 * 
 * <p>
 * implements:
 * <p>
 * StandAloneSessionManager: server side session manager, only support one server
 * <p>
 * RedisSessionManager: using redis, support cluster of servers
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public interface SessionManager {

	/**
	 * get session id from cookie
	 * 
	 * @param request
	 * @return
	 */
	public String getSessionIdFromCookie(HttpServletRequest request);

	/**
	 * set session id into cookie
	 * 
	 * @param response
	 * @param sessionId
	 * @param expired
	 */
	public void setSessionIdToCookie(HttpServletResponse response, String sessionId, boolean expired);

	/**
	 * get local session
	 * 
	 * @return
	 */
	public ExpiringSession getLocalSession();

	/**
	 * set local session
	 * 
	 * @param sessionId
	 */
	public void setLocalSession(ExpiringSession session);

	/**
	 * delete local session, just set to null
	 */
	public void deleteLocalSession();

	/**
	 * get global session by id
	 * 
	 * @param sessionId
	 * @return
	 */
	public ExpiringSession getGlobalSession(String sessionId);

	/**
	 * save global session
	 * 
	 * @param session
	 */
	public void saveGlobalSession(ExpiringSession session);

	/**
	 * delete global session by id
	 * 
	 * @param sessionId
	 */
	public void deleteGlobalSession(String sessionId);

	/**
	 * create global session
	 * 
	 * @return
	 */
	public ExpiringSession createGlobalSession();
}
