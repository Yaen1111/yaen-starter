package org.yaen.starter.web.home.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * home controller, deals welcome, login, logout, error
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

	/**
	 * welcome
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "welcome.html", "" })
	public String welcome(Model model) throws Exception {
		return "home/welcome";
	}

	/**
	 * login
	 * <p>
	 * if has username, password, and rememberMe field, login will be done by shiro, and redirect to welcome.html
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("login.html")
	public String login(Model model, HttpServletRequest request) throws Exception {
		// check post for login result
		if (StringUtil.equalsIgnoreCase(request.getMethod(), "post")) {

			// get shiro auto auth error
			Object shiroExceptionName = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
			if (shiroExceptionName != null) {
				model.addAttribute("errormsg", shiroExceptionName);
			}
		}

		return "home/login";
	}

	/**
	 * logout
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("logout.html")
	public String logout(Model model) throws Exception {
		return "home/logout";
	}

}
