package org.yaen.starter.web.home.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.sessions.CookieContext;
import org.yaen.starter.web.home.sessions.SessionStorage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * session interceptor
 * 
 * @author Yaen 2016年5月19日下午3:33:09
 */
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

	/** session max age */
	@Setter
	private int sessionMaxInactiveIntervalInSeconds = 108000;

	/** session client, save session id */
	@Setter
	private CookieContext cookieContext;

	/** session repository, save session content */
	@Setter
	private SessionRepository<ExpiringSession> sessionRepository;

	/**
	 * pre, init session
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// call super
		if (super.preHandle(request, response, handler)) {

			ExpiringSession session = null;

			// get session id from cookie
			String sessionId = this.cookieContext.getSessionId(request);
			log.debug("get session context id:{}", sessionId);

			// get session from repository
			if (StringUtil.isNotEmpty(sessionId)) {
				session = sessionRepository.getSession(sessionId);
				log.debug("get session:{}", session);
			}

			// check expire
			if (session != null && session.isExpired()) {
				this.sessionRepository.delete(session.getId());
				log.debug("session expired, deleted! sessionId:{}", session.getId());
				session = null;
			}

			// create session if not exists
			if (session == null) {
				session = this.sessionRepository.createSession();
				session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
				log.debug("session created! sessionId:{}", session.getId());
			}

			// save session in local storage
			SessionStorage.setLocalSession(session);
			log.debug("save session in local storage");

			return true;
		}
		return false;
	}

	/**
	 * post, save session
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// get session from thread local
		ExpiringSession session = SessionStorage.getLocalSession();
		log.debug("get session from local storage:{}", session);

		// deal session if any
		if (session != null) {

			// save or delete session, as may be changed by controller
			if (session.isExpired()) {
				// delete session
				this.sessionRepository.delete(session.getId());

				// delete cookie due to expired
				this.cookieContext.setSessionId(response, session.getId(), true);

				log.debug("session expired, deleted! sessionId:{}", session.getId());
			} else {
				// save session
				session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
				this.sessionRepository.save(session);

				// save to cookie
				this.cookieContext.setSessionId(response, session.getId(), false);

				log.debug("session saved");
			}
		}

		// call super
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * last, delete local
	 * 
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		// delete local
		SessionStorage.setLocalSession(null);

		// call super
		super.afterCompletion(request, response, handler, ex);
	}

}
