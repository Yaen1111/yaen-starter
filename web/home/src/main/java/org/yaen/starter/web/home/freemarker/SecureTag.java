package org.yaen.starter.web.home.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.SecureTag}
 */
@SuppressWarnings("rawtypes")
public abstract class SecureTag implements TemplateDirectiveModel {

	/**
	 * 
	 * @see freemarker.template.TemplateDirectiveModel#execute(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)
	 */
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		verifyParameters(params);
		render(env, params, body);
	}

	/**
	 * abstract* render tag content
	 * 
	 * @param env
	 * @param params
	 * @param body
	 * @throws IOException
	 * @throws TemplateException
	 */
	public abstract void render(Environment env, Map params, TemplateDirectiveBody body)
			throws IOException, TemplateException;

	/**
	 * get param value as string
	 * 
	 * @param params
	 * @param name
	 * @return
	 */
	protected String getParam(Map params, String name) {
		Object value = params.get(name);

		if (value instanceof SimpleScalar) {
			return ((SimpleScalar) value).getAsString();
		}

		return null;
	}

	/**
	 * get subject helper, just call shiro SecurityUtils.getSubject()
	 * 
	 * @return
	 */
	protected Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * verify params, default do nothing
	 * 
	 * @param params
	 * @throws TemplateModelException
	 */
	protected void verifyParameters(Map params) throws TemplateModelException {
	}

	/**
	 * actually render body
	 * 
	 * @param env
	 * @param body
	 * @throws IOException
	 * @throws TemplateException
	 */
	protected void renderBody(Environment env, TemplateDirectiveBody body) throws IOException, TemplateException {
		if (body != null) {
			body.render(env.getOut());
		}
	}
}
