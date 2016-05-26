package org.yaen.starter.web.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * welcome controller, deals welcome
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

	/**
	 * welcome
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "welcome.html", "" })
	public String index(Model model) throws Exception {
		return "welcome";
	}

}
