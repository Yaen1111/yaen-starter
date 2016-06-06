package org.yaen.starter.web.home.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.biz.shared.objects.RoleDTO;
import org.yaen.starter.biz.shared.services.UserService;

/**
 * user controller, deals user/role/auth
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * user list and pager
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("user.jspx")
	public String user(Model model) throws Exception {
		return "user/user";
	}

	/**
	 * role list
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("role.jspx")
	public String role(Model model) throws Exception {
		// get all role list
		List<RoleDTO> rolelist = userService.getRoleListAll();
		model.addAttribute("rolelist", rolelist);

		return "user/role";
	}

}