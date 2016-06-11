package org.yaen.starter.web.home.contexts.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import lombok.Setter;

/**
 * session listener, used to delete expired or ended session
 * 
 * @author Yaen 2016年5月25日下午5:43:28
 */
public class ShiroSessionListener implements SessionListener {

	/** the session dao */
	@Setter
	private ShiroSessionDAO sessionDAO;

	/**
	 * @see org.apache.shiro.session.SessionListener#onStart(org.apache.shiro.session.Session)
	 */
	@Override
	public void onStart(Session session) {
	}

	/**
	 * @see org.apache.shiro.session.SessionListener#onStop(org.apache.shiro.session.Session)
	 */
	@Override
	public void onStop(Session session) {
		// delete given session, as is logout
		sessionDAO.delete(session);
	}

	/**
	 * @see org.apache.shiro.session.SessionListener#onExpiration(org.apache.shiro.session.Session)
	 */
	@Override
	public void onExpiration(Session session) {
		// delete given session, as is expired
		sessionDAO.delete(session);
	}

}
