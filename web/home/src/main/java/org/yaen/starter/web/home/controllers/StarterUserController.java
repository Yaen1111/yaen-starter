package org.yaen.starter.web.home.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.dal.entities.user.RoleEntity;
import org.yaen.starter.core.model.services.UserService;

/**
 * user controller, deals user/role/auth
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/user")
public class StarterUserController {

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
		List<RoleEntity> rolelist = this.userService.getRoleListAll();
		model.addAttribute("rolelist", rolelist);

		return "user/role";
	}

}
