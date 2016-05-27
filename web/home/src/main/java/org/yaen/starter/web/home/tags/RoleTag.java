package org.yaen.starter.web.home.tags;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.RoleTag}
 * </p>
 */
@SuppressWarnings("rawtypes")
public abstract class RoleTag extends SecureTag {

	/**
	 * get param by "name"
	 * 
	 * @param params
	 * @return
	 */
	protected String getName(Map params) {
		return getParam(params, "name");
	}

	/**
	 * 
	 * @see org.yaen.starter.web.home.tags.SecureTag#render(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateDirectiveBody)
	 */
	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		boolean show = this.showTagBody(getName(params));
		if (show) {
			this.renderBody(env, body);
		}
	}

	/**
	 * decide whether show body or not by child
	 * 
	 * @param roleName
	 * @return
	 */
	protected abstract boolean showTagBody(String roleName);
}