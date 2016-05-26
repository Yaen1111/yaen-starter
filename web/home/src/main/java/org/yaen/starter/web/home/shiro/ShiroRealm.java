package org.yaen.starter.web.home.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.biz.shared.objects.UserDTO;
import org.yaen.starter.biz.shared.services.UserService;
import org.yaen.starter.common.data.exceptions.BizException;
import org.yaen.starter.common.util.utils.StringUtil;

/**
 * shiro realm for authentication(user info) and authorization(role and perm)
 * <p>
 * using biz service to get user info, session and cache is done by shiro
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * check user login info, throw exception if login failed
	 * 
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// get user token
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;

		String username = StringUtil.trimToNull(userToken.getUsername());
		String password = StringUtil.trimToNull(String.copyValueOf(userToken.getPassword()));

		// check user name
		if (username == null) {
			throw new UnknownAccountException("username is empty");
		}

		UserDTO user = new UserDTO();

		// find user
		try {
			user.setUserName(username);
			userService.Login(user);

		} catch (BizException ex) {
			// user not exists
			throw new UnknownAccountException("user not exists");
		}

		// check password
		if (!StringUtil.equalsIgnoreCase(password, user.getPasswordHash())) {
			throw new IncorrectCredentialsException("password error");
		}

		// here is ok, create authentication info

		// create principal(user object)
		ShiroPrincipal principal = new ShiroPrincipal(username);

		// create credentials(password hash and salt)
		ShiroCredentials credentials = new ShiroCredentials(user.getPasswordHash(), user.getPasswordSalt());

		// return auth info
		return new SimpleAuthenticationInfo(principal, credentials, this.getName());
	}

	/**
	 * get user perm
	 * 
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// get user principal
		ShiroPrincipal principal = (ShiroPrincipal) principals.fromRealm(this.getName()).iterator().next();

		// TODO
		if (principal != null) {
			// the return info
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

			info.addRole("admin");

			return info;

		}
		return null;
	}

}
