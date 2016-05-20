package org.yaen.starter.test.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.biz.shared.objects.PartyDTO;
import org.yaen.starter.biz.shared.services.PartyService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.test.test.tester.UnitTester;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
public class PartyServiceTester extends UnitTester {

	@Autowired
	private PartyService partyService;

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
			PartyDTO dto = new PartyDTO();
			dto.setPartyRoleType("admin2");
			dto.setPartyType("PERSON");

			long partyid = partyService.RegisterNewParty(dto);

			System.out.println(partyid);

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

}
