package org.yaen.starter.web.home.freemarker;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.LacksPermissionTag}
 * </p>
 */
public class LacksPermissionTag extends PermissionTag {

	/**
	 * 
	 * @see org.yaen.starter.web.home.freemarker.PermissionTag#showTagBody(java.lang.String)
	 */
	@Override
	protected boolean showTagBody(String p) {
		return !this.isPermitted(p);
	}
}
