package org.yaen.starter.web.home.contexts.shiro;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.yaen.starter.common.integration.clients.RedisClient;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Setter;

/**
 * shiro session DAO used to save session
 * 
 * @author Yaen 2016年5月25日下午4:47:55
 */
public class ShiroSessionDAO extends AbstractSessionDAO {

	/** redis shiro session prefix */
	private static final String REDIS_SHIRO_SESSION = "shiro-session:";

	/** the client for injection */
	@Setter
	private RedisClient redisClient;

	/** session timeout in second, default to 30 minutes */
	@Setter
	private Integer sessionTimeoutInSecond = 1800;

	/**
	 * build session key with prefix
	 * 
	 * @param sessionId
	 * @return
	 */
	protected String buildSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + StringUtil.toString(sessionId);
	}

	/**
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doCreate(org.apache.shiro.session.Session)
	 */
	@Override
	protected Serializable doCreate(Session session) {
		AssertUtil.notNull(session);

		// generate session id
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	/**
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doReadSession(java.io.Serializable)
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			return null;
		}

		return (Session) redisClient.getObjectByKey(this.buildSessionKey(sessionId));
	}

	/**
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#update(org.apache.shiro.session.Session)
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		// check input
		if (session == null || session.getId() == null) {
			throw new UnknownSessionException("session is empty");
		}

		// save with timeout
		redisClient.saveObjectByKey(this.buildSessionKey(session.getId()), session,
				session.getTimeout() < 0 ? -1 : this.sessionTimeoutInSecond);
	}

	/**
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#delete(org.apache.shiro.session.Session)
	 */
	@Override
	public void delete(Session session) {
		AssertUtil.notNull(session);

		redisClient.deleteByKey(this.buildSessionKey(session.getId()));
	}

	/**
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#getActiveSessions()
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		// TODO
		return null;
	}

}