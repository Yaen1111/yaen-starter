package org.yaen.starter.web.home.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.PermissionTag}
 * </p>
 */
@SuppressWarnings("rawtypes")
public abstract class PermissionTag extends SecureTag {

	/**
	 * get param by "name"
	 * 
	 * @param params
	 * @return
	 */
	protected String getName(Map params) {
		return this.getParam(params, "name");
	}

	/**
	 * 
	 * @see org.yaen.starter.web.home.freemarker.SecureTag#verifyParameters(java.util.Map)
	 */
	@Override
	protected void verifyParameters(Map params) throws TemplateModelException {
		String permission = this.getName(params);

		if (permission == null || permission.length() == 0) {
			throw new TemplateModelException("The 'name' tag attribute must be set.");
		}
	}

	/**
	 * 
	 * @see org.yaen.starter.web.home.freemarker.SecureTag#render(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateDirectiveBody)
	 */
	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		String p = this.getName(params);

		boolean show = this.showTagBody(p);
		if (show) {
			this.renderBody(env, body);
		}
	}

	/**
	 * check permit
	 * 
	 * @param p
	 * @return
	 */
	protected boolean isPermitted(String p) {
		return this.getSubject() != null && this.getSubject().isPermitted(p);
	}

	/**
	 * check whether show tab body by child
	 * 
	 * @param p
	 * @return
	 */
	protected abstract boolean showTagBody(String p);
}
