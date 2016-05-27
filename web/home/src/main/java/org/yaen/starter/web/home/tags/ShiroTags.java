package org.yaen.starter.web.home.tags;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;

/**
 * Shortcut for injecting the tags into Freemarker
 *
 * <p>
 * Usage: cfg.setSharedVeriable("shiro", new ShiroTags());
 * </p>
 */
public class ShiroTags extends SimpleHash {
	private static final long serialVersionUID = -9147616618165875072L;

	/**
	 * constructor
	 */
	public ShiroTags() {
		super((ObjectWrapper) null);

		// put shiro tags
		this.put("authenticated", new AuthenticatedTag());
		this.put("guest", new GuestTag());
		this.put("hasAnyRoles", new HasAnyRolesTag());
		this.put("hasPermission", new HasPermissionTag());
		this.put("hasRole", new HasRoleTag());
		this.put("lacksPermission", new LacksPermissionTag());
		this.put("lacksRole", new LacksRoleTag());
		this.put("notAuthenticated", new NotAuthenticatedTag());
		this.put("principal", new PrincipalTag());
		this.put("user", new UserTag());
	}
}