package org.yaen.starter.web.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * index controller, deals welcome
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/")
public class StarterIndexController {

	/**
	 * index
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "", "index", "index.html" })
	public String index(Model model) throws Exception {
		return "index";
	}

}
