package org.yaen.starter.test.test;

import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.test.test.tester.WebTester;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class ControllerTester extends WebTester {

	/*
	 * unit test
	 */
	@org.junit.Test
	public void Test() {

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");

		try {

			final ModelAndView mv = this.excuteAction("GET", "/starter/index");
			System.out.println(mv.getViewName());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
	}

}
