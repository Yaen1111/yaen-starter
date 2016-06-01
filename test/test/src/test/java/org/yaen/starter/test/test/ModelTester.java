package org.yaen.starter.test.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.core.model.user.Role;
import org.yaen.starter.test.test.tester.UnitTester;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class ModelTester extends UnitTester {

	@Autowired
	private ModelService modelService;

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
			Role role = new Role();
			modelService.selectModelByRowid(role, 1);
			List<String> list = role.getAuthIds();
			System.out.println(list);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
		System.out.println("------------");
	}

}
