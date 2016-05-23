/**
 * 
 */
package org.yaen.starter.web.home.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.web.home.utils.WebUtil;
import org.yaen.starter.web.home.viewmodels.ErrorModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * handle all exceptions, return normal view
 * 
 * @author Yaen 2016年5月20日下午4:53:50
 */
@Slf4j
public class StarterExceptionHandler implements HandlerExceptionResolver {

	/**
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// get client ip
		String clientIp = WebUtil.getClientIp(request);

		String requestUri = request.getRequestURI();
		String queryString = request.getQueryString();

		StringBuilder sb = new StringBuilder();
		sb.append("handler=").append(handler).append(",");
		sb.append("clientIp=").append(clientIp).append(",");
		sb.append("requestUri=").append(requestUri).append(",");
		sb.append("queryString=").append(queryString).append(",");

		log.error(sb.toString(), ex);

		return new ErrorModelAndView(ex);
	}

}
