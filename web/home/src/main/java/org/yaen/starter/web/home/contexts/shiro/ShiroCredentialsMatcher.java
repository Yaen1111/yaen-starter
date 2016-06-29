package org.yaen.starter.web.home.contexts.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.yaen.starter.core.model.models.user.UserModel;

/**
 * credentials matcher, used to check password
 * 
 * <p>
 * all shiro auth check will use this, so the check logic only in this class
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
		ShiroCredentials credentials = (ShiroCredentials) this.getCredentials(info);

		// call service to check credential
		return new UserModel().checkUserCredentials(new String(userToken.getPassword()), credentials.getPasswordHash(),
				credentials.getPasswordSalt());
	}
}
