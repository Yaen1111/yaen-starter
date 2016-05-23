package org.yaen.starter.web.velocity.modelandviews;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.context.Context;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

import lombok.Setter;

/**
 * extend VelocityLayoutView for tools map support
 * 
 * @author Yaen 2016年5月19日下午4:06:46
 */
public class ExtendVelocityLayoutView extends VelocityLayoutView {

	@Setter
	private Map<String, Object> toolsMap = new HashMap<String, Object>();

	@Override
	protected void exposeToolAttributes(Context velocityContext, HttpServletRequest request) throws Exception {
		super.exposeToolAttributes(velocityContext, request);

		for (String tool : this.toolsMap.keySet()) {
			velocityContext.put(tool, this.toolsMap.get(tool));
		}
	}
}
