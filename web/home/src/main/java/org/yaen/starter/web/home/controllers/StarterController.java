/**
 * 
 */
package org.yaen.starter.web.home.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * starter controller, mostly for sample
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/starter")
public class StarterController {

	@Autowired
	private UserService userService;

	/**
	 * welcome show fixed page
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/welcome.html")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return new ModelAndView("welcome", "welcome", "welcome content");
	}

	/**
	 * model and view, show model content
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/model.html")
	public ModelAndView model(HttpServletRequest request, HttpServletResponse response) throws IOException {

		UserDTO user = new UserDTO();
		user.setUserName("John");
		user.setTrueName("John Smith");

		return new ModelAndView("model", "user", user);
	}

	/**
	 * json, show model content
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/json.json")
	public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String, Object> model = new HashMap<String, Object>();
		UserDTO user = new UserDTO();
		user.setUserName("username");
		user.setTrueName("truename");

		model.put("user", user);

		return new ModelAndView("json", model);
	}
}
