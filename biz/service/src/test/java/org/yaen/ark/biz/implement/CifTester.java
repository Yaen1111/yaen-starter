/**
 * 
 */
package org.yaen.ark.biz.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.ark.corecif.model.entities.Company;
import org.yaen.ark.corecif.model.entities.Employee;
import org.yaen.ark.corecif.model.entities.PartyRelationship;
import org.yaen.ark.corecif.model.enums.RelationshipTypes;
import org.yaen.ark.corerbac.service.RbacService;
import org.yaen.spring.common.exceptions.BizException;


/**
 * 
 * @author xl 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml" })
public class CifTester extends AbstractJUnit4SpringContextTests {

	@Autowired
	private RbacService cif;

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

			Company company = new Company();

			company.setFullName("home company");
			company.getOrg().setFullName("home company");
			company.getOrg().getParty().setPartyName("home party");

			cif.saveEntity(company);

			Employee emp = new Employee();
			emp.setFullName("john");
			emp.getPerson().setTrueName("john smith");
			emp.getPerson().getParty().setPartyName("smith home");

			cif.saveEntity(emp);

			PartyRelationship rel = new PartyRelationship(emp, company);
			rel.setRelationship(RelationshipTypes.EMPLOYEE_COMPANY);

			cif.saveEntity(rel);

			System.out.println("------------");
			System.out.println("------------");
			System.out.println(company);
			System.out.println(emp);
			System.out.println(rel);
			System.out.println("------------");
			System.out.println("------------");

		} catch (BizException cex) {
			cex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
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
