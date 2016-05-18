package org.yaen.starter.test.test.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * 
 * @author Yaen 2015年12月15日下午1:00:19
 */
@ContextConfiguration(locations = { "classpath:/spring/test-test.xml" })
public class UserServiceTester extends AbstractJUnit4SpringContextTests {

	@Autowired
	private UserService userService;

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
			UserDTO dto = new UserDTO();
			dto.setUserID(124);
			dto.setUserName("John2");
			dto.setPasswordSalt("123");
			dto.setPasswordHash("321");

			long userid = userService.RegisterNewUser(dto);

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

}
