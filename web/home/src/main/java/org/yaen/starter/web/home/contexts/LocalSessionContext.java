/**
 * 
 */
package org.yaen.starter.web.home.contexts;

import org.springframework.session.ExpiringSession;

/**
 * thread local session, only for single controller life cycle use
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class LocalSessionContext {

	/** ThreadLocal */
	private static ThreadLocal<ExpiringSession> context = new ThreadLocal<ExpiringSession>();

	/**
	 * get session
	 * 
	 * @return
	 */
	public static ExpiringSession getSession() {
		return context.get();
	}

	/**
	 * set session
	 * 
	 * @param session
	 */
	public static void setSession(ExpiringSession session) {
		context.set(session);
	}

}
