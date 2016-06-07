package org.yaen.starter.test.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.core.model.models.ark.SubCode;
import org.yaen.starter.core.model.models.user.Auth;
import org.yaen.starter.test.test.tester.UnitTester;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class OneTester extends UnitTester {

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

			Auth auth = new Auth();
			auth.setId("13");
			auth.setTitle("jus");

			service.insertModelByRowid(auth);

			SubCode code = new SubCode();

			if (service.trySelectModelByRowid(code, 2)) {

				System.out.println("------------");
				System.out.println("------------");
				System.out.println(code);
				System.out.println("------------");
				System.out.println("------------");

				code.setName("the4");
				code.setTitle("ti6");
				code.setIntvalue(556);

				service.updateModelByRowid(code);

				System.out.println("update done");

			}

			code.setRowid(0);
			code.setFamily("new");
			service.insertModelByRowid(code);

			System.out.println("insert done");

			// code.setId(3);
			// service.deleteModel(code);

			// System.out.println("delete done");

		} catch (CoreException ex) {
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
