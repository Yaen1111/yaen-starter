/**
 * 
 */
package org.yaen.starter.web.home.viewmodels;

import org.springframework.web.servlet.ModelAndView;

/**
 * error model and view
 * 
 * @author Yaen 2016年5月20日下午5:12:21
 */
public class ErrorModelAndView extends ModelAndView {

	/**
	 * construct an error model and view
	 * 
	 * @param model
	 */
	public ErrorModelAndView(Object model) {
		super("error", "error", model);
	}

}
