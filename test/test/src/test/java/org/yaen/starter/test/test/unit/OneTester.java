package org.yaen.starter.test.test.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.core.model.ark.SubCode;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:/spring/test-test.xml" })
public class OneTester extends AbstractJUnit4SpringContextTests {

	@Autowired
	private ModelService service;

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
			code.setSaveChangeLog(true);

			if (service.trySelectModel(code, 2)) {

				System.out.println("------------");
				System.out.println("------------");
				System.out.println(code);
				System.out.println("------------");
				System.out.println("------------");

				code.setName("the4");
				code.setTitle("ti6");
				code.setIntvalue(556);

				service.updateModel(code);

				System.out.println("update done");

			}

			code.setId(0);
			code.setFamily("new");
			service.insertModel(code);

			System.out.println("insert done");

			// code.setId(3);
			// service.deleteModel(code);

			// System.out.println("delete done");

		} catch (CommonException ex) {
			System.out.println(ex);
		} catch (CoreException ex) {
			System.out.println(ex);
		} catch (BizException ex) {
			System.out.println(ex);
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
