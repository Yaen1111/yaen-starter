package org.yaen.starter.web.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * home controller, deals welcome, login, logout, error
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

	/**
	 * common error
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "error", "error.jspx" })
	public String error(Model model) throws Exception {
		return "error/error";
	}

	/**
	 * error 404 not found
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "404", "404.jspx" })
	public String error404(Model model) throws Exception {
		return "error/404";
	}

	/**
	 * error 403 unauth
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "403", "403.jspx" })
	public String error403(Model model) throws Exception {
		return "error/403";
	}

}
