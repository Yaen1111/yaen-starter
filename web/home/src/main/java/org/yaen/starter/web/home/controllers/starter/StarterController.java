package org.yaen.starter.web.home.controllers.starter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.web.home.viewmodels.ViewModel;

/**
 * starter controller, mostly for sample
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Controller
@RequestMapping("/starter/starter")
public class StarterController {

	/**
	 * welcome show fixed page, using default servlet and modelandview
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("index.jspx")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return new ModelAndView("starter/welcome", "welcome", "welcome content");
	}

	/**
	 * use spring model, recommended way
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("model.jspx")
	public String model(Model model) throws IOException {

		UserDTO user = new UserDTO();
		user.setUserId("John");
		user.setTrueName("John Smith");

		model.addAttribute("user", user);

		return "starter/model";
	}

	/**
	 * using customer view model
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("model2.jspx")
	public ModelAndView viewModel() throws IOException {

		ViewModel model = new ViewModel();

		UserDTO user = new UserDTO();
		user.setUserId("username");
		user.setTrueName("truename");

		model.addAttribute("user", user);

		model.setTitle("model2");

		return model.asView("starter/model2");
	}
}
