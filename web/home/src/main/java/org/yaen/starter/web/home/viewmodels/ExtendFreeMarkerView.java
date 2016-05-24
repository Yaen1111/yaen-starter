package org.yaen.starter.web.home.viewmodels;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * put base path to model
 * 
 * @author Yaen 2016年5月23日下午11:12:09
 */
public class ExtendFreeMarkerView extends FreeMarkerView {

	/**
	 * add some common model
	 * 
	 * @see org.springframework.web.servlet.view.freemarker.FreeMarkerView#exposeHelpers(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {

		// add base to model
		model.putIfAbsent("base", request.getContextPath());
		// ensure title is not null
		model.putIfAbsent("title", "");

		super.exposeHelpers(model, request);
	}
}
