/**
 * 
 */
package org.yaen.starter.web.home.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * starter controller, mostly for sample
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/starter")
public class StarterController {

	/**
	 * index, show something
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/index.html")
	public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return "index";
	}
}
