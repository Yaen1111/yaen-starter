package org.yaen.starter.web.home.freemarker;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.HasRoleTag}
 * </p>
 */
public class HasRoleTag extends RoleTag {

	/**
	 * 
	 * @see org.yaen.starter.web.home.freemarker.RoleTag#showTagBody(java.lang.String)
	 */
	@Override
	protected boolean showTagBody(String roleName) {
		return this.getSubject() != null && this.getSubject().hasRole(roleName);
	}
}
