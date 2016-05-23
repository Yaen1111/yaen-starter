package org.yaen.starter.web.home.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.contexts.SessionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * session interceptor
 * 
 * @author Yaen 2016年5月19日下午3:33:09
 */
@Slf4j
public class SessionInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SessionManager sessionManager;

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
			String sessionId = sessionManager.getSessionIdFromCookie(request);
			log.debug("get session context id:{}", sessionId);

			// get session from repository
			if (StringUtil.isNotEmpty(sessionId)) {
				session = sessionManager.getGlobalSession(sessionId);
				log.debug("get session:{}", session);
			}

			// check expire
			if (session != null && session.isExpired()) {
				sessionManager.deleteGlobalSession(session.getId());
				log.debug("session expired, deleted! sessionId:{}", session.getId());
				session = null;
			}

			// create session if not exists
			if (session == null) {
				session = sessionManager.createGlobalSession();
				log.debug("session created! sessionId:{}", session.getId());
			}

			// save session in local storage
			sessionManager.setLocalSession(session);
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
		ExpiringSession session = sessionManager.getLocalSession();
		log.debug("get session from local storage:{}", session);

		// deal session if any
		if (session != null) {

			// save or delete session, as may be changed by controller
			if (session.isExpired()) {
				// delete session
				sessionManager.deleteGlobalSession(session.getId());

				// delete cookie due to expired
				sessionManager.setSessionIdToCookie(response, session.getId(), true);

				log.debug("session expired, deleted! sessionId:{}", session.getId());
			} else {
				// save session
				sessionManager.saveGlobalSession(session);

				// save to cookie
				sessionManager.setSessionIdToCookie(response, session.getId(), false);

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
		sessionManager.deleteLocalSession();

		// call super
		super.afterCompletion(request, response, handler, ex);
	}

}
