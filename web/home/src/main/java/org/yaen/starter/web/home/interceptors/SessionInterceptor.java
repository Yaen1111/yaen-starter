package org.yaen.starter.web.home.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.contexts.CookieSessionContext;
import org.yaen.starter.web.home.contexts.LocalSessionContext;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * session interceptor, mostly for redis session
 * 
 * @author Yaen 2016年5月19日下午3:33:09
 */
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

	/** session max age */
	@Setter
	private int sessionMaxInactiveIntervalInSeconds = 108000;

	/** session repository, maybe any type */
	@Setter
	private SessionRepository<ExpiringSession> sessionRepository;

	/** session context */
	@Setter
	private CookieSessionContext sessionContext;

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

			// get session id from cookie
			String sessionId = this.sessionContext.getSessionId(request);

			// get session from given repository
			ExpiringSession session = null;
			if (StringUtil.isNotEmpty(sessionId)) {
				session = sessionRepository.getSession(sessionId);
			}

			// create session if not exists
			if (session == null) {
				session = this.sessionRepository.createSession();
				session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
				this.sessionRepository.save(session);
				log.debug("createSession success! sessionId:{}", session.getId());
			}

			// save session in thread local
			LocalSessionContext.setSession(session);

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
		ExpiringSession session = LocalSessionContext.getSession();

		// save or delete session, as may be changed by controller
		if (session.isExpired()) {
			this.sessionRepository.delete(session.getId());
		} else {
			session.setMaxInactiveIntervalInSeconds(this.sessionMaxInactiveIntervalInSeconds);
			this.sessionRepository.save(session);
		}

		// set to cookie
		this.sessionContext.setSessionId(response, session.getId(), session.isExpired());

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
		LocalSessionContext.setSession(null);

		// call super
		super.afterCompletion(request, response, handler, ex);
	}

}
