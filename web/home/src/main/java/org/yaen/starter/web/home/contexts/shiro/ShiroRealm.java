package org.yaen.starter.web.home.contexts.shiro;

import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.DataException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.user.RbacModel;
import org.yaen.starter.core.model.models.user.UserModel;
import org.yaen.starter.core.model.services.ProxyService;

import lombok.extern.slf4j.Slf4j;

/**
 * shiro realm for authentication(user info) and authorization(role and perm)
 * <p>
 * using biz service to get user info, session and cache is done by shiro
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private ProxyService proxyService;

	/**
	 * get user info only, no password check, throw exception if user not found
	 * 
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// get user token
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;

		String username = StringUtil.trimToNull(userToken.getUsername());

		// check user name
		if (username == null) {
			throw new UnknownAccountException("username is empty");
		}

		UserModel user = new UserModel(this.proxyService);

		// find user
		try {
			user.loadById(username);
		} catch (DataNotExistsException ex) {
			throw new UnknownAccountException();
		} catch (DataException ex) {
			throw new AccountException("unknown error", ex);
		} catch (CommonException ex) {
			throw new AccountException("unknown error", ex);
		}

		// here is ok, create authentication info

		// create principal(user object), the username maybe changed by capital
		ShiroPrincipal principal = new ShiroPrincipal(user.getEntity().getId());

		// create credentials(password hash and salt)
		ShiroCredentials credentials = new ShiroCredentials(user.getEntity().getPasswordHash(),
				user.getEntity().getPasswordSalt());

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

		if (principal != null) {

			try {
				String userId = principal.getUsername();

				RbacModel rbac = new RbacModel(this.proxyService);

				rbac.loadById(userId);

				// get user roles
				Set<String> roles = rbac.getRoles();
				Set<String> auths = rbac.getAuths();

				// simple
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

				// add all roles
				info.addRoles(roles);

				// should add permissions manually, or should inject RolePermissionResolver to this realm to get
				// permissions of role in real-time(no cache)

				// get all permissions of all roles, this may use cache
				info.addStringPermissions(auths);

				log.debug("get user roles, user={}, roles={}", userId, roles);
				log.debug("get user auths, user={}, auths={}", userId, auths);

				return info;
			} catch (CommonException ex) {
				log.error("get role auth error", ex);
				return null;
			} catch (DataException ex) {
				log.error("get role auth error", ex);
				return null;
			}
		}
		return null;
	}

}
