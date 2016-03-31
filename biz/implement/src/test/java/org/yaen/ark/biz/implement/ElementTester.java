/**
 * 
 */
package org.yaen.ark.biz.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.ark.core.model.SubCode;
import org.yaen.ark.core.service.ElementEntityService;
import org.yaen.spring.common.exceptions.BizException;


/**
 * 
 * @author xl 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml" })
public class ElementTester extends AbstractJUnit4SpringContextTests {
	//
	// @Autowired
	// private ElementMapper mapper;

	@Autowired
	private ElementEntityService service;

	@org.junit.BeforeClass
	public static void BeforeClass() {
		// before all test, only once
		System.out.println("------beforeclass------");
	}

	@org.junit.Before
	public void Before() {
		// before each test
		System.out.println("------before------");
	}

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

			SubCode code = new SubCode();

			service.selectEntity(code, 2);

			System.out.println("------------");
			System.out.println("------------");
			System.out.println(code);
			System.out.println("------------");
			System.out.println("------------");

			code.setName("the4");
			code.setTitle("ti6");
			code.setIntvalue(556);

			service.updateElement(code);

			System.out.println("update done");

			code.setId(0);
			code.setFamily("new");
			service.insertEntity(code);

			System.out.println("insert done");

			code.setId(3);
			service.deleteEntity(code);

			System.out.println("delete done");

		} catch (BizException cex) {
			System.out.println(cex);
		} catch (Exception ex) {
			System.out.println(ex);
		}

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
	}

	@org.junit.After
	public void After() {
		// after each test
		System.out.println("------after------");
	}

	@org.junit.AfterClass
	public static void AfterClass() {
		// after all test, only once
		System.out.println("------afterclass------");
	}

}
