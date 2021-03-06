package org.yaen.starter.web.home.tags;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}
 * </p>
 *
 * @since 0.1
 */
public class HasPermissionTag extends PermissionTag {

	/**
	 * 
	 * @see org.yaen.starter.web.home.tags.PermissionTag#showTagBody(java.lang.String)
	 */
	@Override
	protected boolean showTagBody(String p) {
		return this.isPermitted(p);
	}
}
