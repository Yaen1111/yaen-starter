package org.yaen.starter.web.home.tags;

import org.apache.shiro.subject.Subject;

/**
 * Displays body content if the current user has any of the roles specified.
 *
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.HasAnyRolesTag}
 * </p>
 *
 * @since 0.2
 */
public class HasAnyRolesTag extends RoleTag {
	/** Delimiter that separates role names in tag attribute */
	private static final String ROLE_NAMES_DELIMETER = ",";

	/**
	 * 
	 * @see org.yaen.starter.web.home.tags.RoleTag#showTagBody(java.lang.String)
	 */
	@Override
	protected boolean showTagBody(String roleNames) {
		if (roleNames == null)
			return false;

		boolean hasAnyRole = false;
		Subject subject = this.getSubject();

		if (subject != null) {
			// Iterate through roles and check to see if the user has one of the roles
			for (String role : roleNames.split(ROLE_NAMES_DELIMETER)) {
				if (subject.hasRole(role.trim())) {
					hasAnyRole = true;
					break;
				}
			}
		}

		return hasAnyRole;
	}
}