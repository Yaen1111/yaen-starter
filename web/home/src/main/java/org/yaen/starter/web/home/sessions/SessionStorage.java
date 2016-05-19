/**
 * 
 */
package org.yaen.starter.web.home.sessions;

import org.springframework.session.ExpiringSession;

/**
 * thread local session storage, only for single controller life cycle use
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class SessionStorage {

	/** ThreadLocal */
	private static ThreadLocal<ExpiringSession> context = new ThreadLocal<ExpiringSession>();

	/**
	 * get local session
	 * 
	 * @return
	 */
	public static ExpiringSession getLocalSession() {
		return context.get();
	}

	/**
	 * set local session
	 * 
	 * @param session
	 */
	public static void setLocalSession(ExpiringSession session) {
		context.set(session);
	}

}
