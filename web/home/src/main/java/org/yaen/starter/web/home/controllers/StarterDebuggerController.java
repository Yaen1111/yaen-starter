package org.yaen.starter.web.home.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * debugger controller, deals welcome
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/debugger")
public class StarterDebuggerController {

	@Autowired
	private HttpClient httpClient;

	/**
	 * index
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "", "index", "index.html" })
	public String index(Model model) {
		return "debugger/index";
	}

	/**
	 * http client debugger
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("http")
	public String http(Model model, String api, String content) {

		model.addAttribute("api", api);
		model.addAttribute("content", content);

		// get if not empty
		if (StringUtil.isNotBlank(api)) {

			try {
				String result = httpClient.httpPost(api, content);

				model.addAttribute("result", result);
			} catch (IOException ex) {
				log.debug("debugger:http:error", ex);
				model.addAttribute("error", ex);
			}
		}

		return "debugger/http";
	}

}
