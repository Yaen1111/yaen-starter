package org.yaen.starter.test.test;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class AllTester extends StarterUnitTester {

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
//
//		try {
////			// create one party
////			PartyDTO party = new PartyDTO();
////			party.setPartyRoleType("admin2");
////			party.setPartyType("PERSON");
////
////			long partyid = partyService.RegisterNewParty(party);
////
////			System.out.println(partyid);
////
////			// create user by given party
////			UserDTO user = new UserDTO();
////			user.setUserId("Linda");
////			user.setPasswordSalt("123");
////			user.setPasswordHash("321");
////
////			userService.registerNewUser(user);
////
////			System.out.println(user.getUserId());
//
//		} catch (CommonException ex) {
//			System.out.println(ex);
//		} catch (CoreException ex) {
//			System.out.println(ex);
//		} catch (BizException ex) {
//			System.out.println(ex);
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}

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
