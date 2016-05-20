package org.yaen.starter.test.test.tester;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * common web tester
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ ServletTestExecutionListener.class, DirtiesContextBeforeModesTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:/spring/test-test-web.xml" })
public abstract class WebTester {

	/** RequestMappingHandlerMapping */
	@Resource(type = RequestMappingHandlerMapping.class)
	protected HandlerMapping handlerMapping;

	/** RequestMappingHandlerAdapter */
	@Resource(type = RequestMappingHandlerAdapter.class)
	protected HandlerAdapter handlerAdapter;

	/**
	 * Simulate Request to URL appoint by MockHttpServletRequest.
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	public final ModelAndView excuteAction(final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		HandlerExecutionChain chain = this.handlerMapping.getHandler(request);
		final ModelAndView model = this.handlerAdapter.handle(request, response, chain.getHandler());
		return model;
	}

	/**
	 * Simulate request to url
	 * 
	 * @param method
	 *            post or get
	 * @param url
	 * @param port
	 * @param objects
	 * @return
	 * @throws Exception
	 */
	public final ModelAndView excuteAction(final String method, final String url, final int port,
			final Object[]... objects) throws Exception {
		// use mock request and response
		MockHttpServletRequest request = new MockHttpServletRequest(method, url);
		MockHttpServletResponse response = new MockHttpServletResponse();

		// set port
		request.setServerPort(port);
		request.setLocalPort(port);

		// set param in pair
		if (objects != null) {
			for (Object[] object : objects) {
				if (object != null && object.length == 2) {
					request.addParameter(object[0].toString(), object[1].toString());
				}
			}
		}

		// run
		return this.excuteAction(request, response);
	}

	/**
	 * simulate request to url
	 * 
	 * @param method
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public final ModelAndView excuteAction(final String method, final String url) throws Exception {
		return this.excuteAction(method, url, 80, (Object[][]) null);
	}

}
