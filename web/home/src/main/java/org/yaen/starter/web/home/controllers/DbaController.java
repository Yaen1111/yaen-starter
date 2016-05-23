package org.yaen.starter.web.home.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.common.dal.mappers.ZeroMapper;
import org.yaen.starter.common.data.entities.MyDescribeEntity;
import org.yaen.starter.web.home.viewmodels.ViewModel;

import lombok.extern.slf4j.Slf4j;

/**
 * dba controller, mostly like show table info
 * 
 * @author Yaen 2016年5月19日下午2:28:18
 */
@Slf4j
@Controller
@RequestMapping("/dba")
public class DbaController {

	@Autowired
	private ZeroMapper zero;

	/**
	 * show tables
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showtables")
	public ModelAndView showTables(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ViewModel model = new ViewModel();
		model.setTitle("DBA");

		List<String> tableNames = zero.showTables();

		model.addAttribute("tableNames", tableNames);

		return model.asView("/dba/showtables");
	}

	/**
	 * show tables
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/describetable/{tableName}")
	public ModelAndView describeTable(@PathVariable(value = "tableName") String tableName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ViewModel model = new ViewModel();
		model.setTitle("DBA");

		List<MyDescribeEntity> columns = zero.describeTable(tableName);

		model.addAttribute("tableName", tableName);
		model.addAttribute("columns", columns);

		return model.asView("/dba/describetable");
	}

}
