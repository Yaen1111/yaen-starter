/**
 * 
 */
package org.yaen.starter.web.home.contexts;

import java.util.HashMap;
import java.util.Map;

import org.springframework.session.ExpiringSession;

/**
 * thread local storage context
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class LocalStorageContext {

	/** sessionId */
	public static final String SESSION_KEY = "session";

	/** ThreadLocal */
	private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected java.util.Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		};
	};

	/**
	 * get object by key
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return context.get().get(key);
	}

	/**
	 * set object by key
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static void put(String key, Object value) {
		context.get().put(key, value);
	}

	/**
	 * get session
	 * 
	 * @return
	 */
	public static ExpiringSession getSession() {
		return (ExpiringSession) get(SESSION_KEY);
	}

	/**
	 * set session
	 * 
	 * @param sessionId
	 */
	public static void setSession(ExpiringSession session) {
		put(SESSION_KEY, session);
	}
}
