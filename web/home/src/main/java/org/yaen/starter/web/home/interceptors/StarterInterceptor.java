package org.yaen.starter.web.home.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * starter interceptor, mostly for sample
 * 
 * @author Yaen 2016年5月19日下午2:04:04
 */
@Slf4j
public class StarterInterceptor extends HandlerInterceptorAdapter {

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// call super
		if (super.preHandle(request, response, handler)) {

			log.debug("preHandle, ip={}, uri={}", WebUtil.getClientIp(request), request.getRequestURI());

			// get user
			Subject user = WebUtil.getUser();

			log.debug("preHandle, user={}", user);

			// return true for next step
			return true;
		}
		return false;
	}

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// after controller done, but before view

		// call super
		super.postHandle(request, response, handler, modelAndView);
	}

}
