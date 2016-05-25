/**
 * 
 */
package org.yaen.starter.web.home.shiro;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.SerializeUtil;
import org.yaen.starter.web.home.contexts.RedisRepositoryManager;

import lombok.Setter;

/**
 * shiro session DAO used to save session
 * 
 * @author Yaen 2016年5月25日下午4:47:55
 */
public class ShiroSessionDAO extends AbstractSessionDAO {

	private static final String REDIS_SHIRO_SESSION = "shiro-session:";
	private static final int DB_INDEX = 1;

	/** the repository manager for injection */
	@Setter
	private RedisRepositoryManager repositoryManager;

	/** session timeout in second, default to 30 minutes */
	@Setter
	private Integer sessionTimeoutInSecond = 1800;

	/**
	 * update session
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#update(org.apache.shiro.session.Session)
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		AssertUtil.notNull(session);

		// check input
		if (session.getId() == null) {
			throw new UnknownSessionException("session is empty");
		}

		try {
			// make key and value
			byte[] key = SerializeUtil.serialize(REDIS_SHIRO_SESSION + session.getId());
			byte[] value = SerializeUtil.serialize(session);

			// save with timeout
			repositoryManager.saveValueByKey(DB_INDEX, key, value,
					session.getTimeout() < 0 ? -1 : this.sessionTimeoutInSecond);
		} catch (Exception ex) {
			throw new RuntimeException("save session error", ex);
		}
	}

	/**
	 * delete session
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#delete(org.apache.shiro.session.Session)
	 */
	@Override
	public void delete(Session session) {
		AssertUtil.notNull(session);

		try {
			repositoryManager.deleteByKey(DB_INDEX, SerializeUtil.serialize(REDIS_SHIRO_SESSION + session.getId()));
		} catch (Exception ex) {
			throw new RuntimeException("delete session error", ex);
		}
	}

	/**
	 * get all session, not implemented
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#getActiveSessions()
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		// TODO
		return null;
	}

	/**
	 * create new session
	 * 
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
	 * get session
	 * 
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doReadSession(java.io.Serializable)
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			return null;
		}

		Session session = null;
		try {
			byte[] value = repositoryManager.getValueByKey(DB_INDEX,
					SerializeUtil.serialize(REDIS_SHIRO_SESSION + sessionId));
			session = (Session) SerializeUtil.deserialize(value);
		} catch (Exception ex) {
			throw new RuntimeException("get session error", ex);
		}
		return session;
	}

}