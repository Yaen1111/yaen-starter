/**
 * 
 */
package org.yaen.ark.biz.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.ark.corecif.model.entities.Party;
import org.yaen.spring.common.exceptions.BizException;
import org.yaen.spring.data.services.QueryService;

/**
 * 
 * @author xl 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml" })
public class EventTester extends AbstractJUnit4SpringContextTests {

	@Autowired
	private QueryService event;

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

			Party party = new Party();
			party.setPartyName("home party77");
			party.setPartyType("ORG");

			List<Long> ids = event.SelectIDsByAllField(party);

			System.out.println("------------");
			System.out.println("------------");
			System.out.println(ids);
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
