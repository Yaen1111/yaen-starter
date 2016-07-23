package org.yaen.starter.web.home.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaen.starter.common.integration.clients.HttpClient;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.web.home.utils.WebUtil;

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
	 * a debug api, can be anything, only useable on debug mode
	 * 
	 * @param model
	 * @param request
	 */
	@RequestMapping("debug")
	public String debug(Model model, HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			// log api
			log.info("api:debugger:debug:called, uri={}, ip={}, method={}, querystring={}", request.getRequestURI(),
					WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());
		}

		return "debugger/debug";
	}

	/**
	 * http client debugger
	 * 
	 * @param model
	 * @param method
	 * @param api
	 * @param content
	 * @param request
	 * @return
	 */
	@RequestMapping("http")
	public String http(Model model, String method, String api, String content, HttpServletRequest request) {
		// log api
		log.info("api:debugger:http:called, uri={}, ip={}, method={}, querystring={}", request.getRequestURI(),
				WebUtil.getClientIp(request), request.getMethod(), request.getQueryString());

		model.addAttribute("api", api);
		model.addAttribute("content", content);

		// get if not empty
		if (StringUtil.isNotBlank(api)) {

			try {
				if (StringUtil.equalsIgnoreCase(method, "POST")) {
					String result = httpClient.httpPost(api, content);
					model.addAttribute("result", result);
				} else {
					String result = httpClient.httpGet(api);
					model.addAttribute("result", result);
				}

			} catch (IOException ex) {
				log.debug("debugger:http:error", ex);
				model.addAttribute("error", ex);
			}
		} else {
			model.addAttribute("result", "please input api");
		}

		return "debugger/http";
	}

}
