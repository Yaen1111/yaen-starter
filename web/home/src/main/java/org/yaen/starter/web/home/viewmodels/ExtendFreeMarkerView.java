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

	private static final String CONTEXT_PATH = "base";

	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		model.put(CONTEXT_PATH, request.getContextPath());
		super.exposeHelpers(model, request);
	}
}
