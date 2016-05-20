package org.yaen.starter.web.home.modelandviews;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import lombok.Setter;

/**
 * extend VelocityLayoutViewResolver, add tools map support
 * 
 * @author Yaen 2016年5月19日下午4:08:44
 */
public class ExtendVelocityLayoutViewResolver extends VelocityLayoutViewResolver {

	@Setter
	private Map<String, Object> toolsMap = new HashMap<String, Object>();

	@Override
	protected Class<?> requiredViewClass() {
		return ExtendVelocityLayoutView.class;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		ExtendVelocityLayoutView view = (ExtendVelocityLayoutView) super.buildView(viewName);
		view.setToolsMap(this.toolsMap);
		return view;
	}
}
