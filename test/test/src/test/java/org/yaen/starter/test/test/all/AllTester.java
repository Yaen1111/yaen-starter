package org.yaen.starter.test.test.all;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.starter.biz.shared.objects.PartyDTO;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.PartyService;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:/spring/test-test.xml" })
public class AllTester extends AbstractJUnit4SpringContextTests {

	@Autowired
	PartyService partyService;

	@Autowired
	UserService userService;

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
			// create one party
			PartyDTO dto = new PartyDTO();
			dto.setPartyRoleType("admin2");
			dto.setPartyType("PERSON");

			long partyid = partyService.RegisterNewParty(dto);

			System.out.println(partyid);

			// create user by given party
			UserDTO dto2 = new UserDTO();
			dto2.setUserID(partyid);
			dto2.setUserName("Linda");
			dto2.setPasswordSalt("123");
			dto2.setPasswordHash("321");

			long userid = userService.RegisterNewUser(dto2);

			System.out.println(userid);

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
