package org.yaen.starter.web.home.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.yaen.starter.common.util.utils.StringUtil;

/**
 * credentials matcher, used to check password
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
public class ShiroCredentialsMatcher extends SimpleCredentialsMatcher {

	/**
	 * @see org.apache.shiro.authc.credential.SimpleCredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken,
	 *      org.apache.shiro.authc.AuthenticationInfo)
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// get auth from token, this is the user input
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;

		// get user credentials from info, this is stored account info
		// TODO
		String password = (String) this.getCredentials(info);

		// check
		if (StringUtil.equalsIgnoreCase(String.copyValueOf(userToken.getPassword()), password)) {
			return true;
		}

		return false;
	}
}
