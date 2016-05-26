package org.yaen.starter.web.home.freemarker;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.LacksRoleTag}
 * </p>
 */
public class LacksRoleTag extends RoleTag {

	/**
	 * 
	 * @see org.yaen.starter.web.home.freemarker.RoleTag#showTagBody(java.lang.String)
	 */
	@Override
	protected boolean showTagBody(String roleName) {
		boolean hasRole = this.getSubject() != null && this.getSubject().hasRole(roleName);
		return !hasRole;
	}
}
