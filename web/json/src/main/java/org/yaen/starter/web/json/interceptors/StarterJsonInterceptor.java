package org.yaen.starter.web.json.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.web.home.interceptors.StarterInterceptor;
import org.yaen.starter.web.home.utils.WebUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * starter wechat interceptor for auth
 * 
 * @author Yaen 2016年5月19日下午2:04:04
 */
@Slf4j
public class StarterJsonInterceptor extends StarterInterceptor {

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (super.preHandle(request, response, handler)) {

			log.debug("preHandle, ip={}, uri={}", WebUtil.getClientIp(request), request.getRequestURI());

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
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		// after controller and view, may handle exception
		super.afterCompletion(request, response, handler, ex);
	}
}
