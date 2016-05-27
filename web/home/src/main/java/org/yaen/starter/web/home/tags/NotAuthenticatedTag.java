package org.yaen.starter.web.home.tags;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * Freemarker tag that renders the tag body only if the current user has <em>not</em> executed a successful
 * authentication attempt <em>during their current session</em>.
 *
 * <p>
 * The logically opposite tag of this one is the {@link org.apache.shiro.web.tags.AuthenticatedTag}.
 *
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.NotAuthenticatedTag}
 * </p>
 */
@Slf4j
public class NotAuthenticatedTag extends SecureTag {

	/**
	 * 
	 * @see org.yaen.starter.web.home.tags.SecureTag#render(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateDirectiveBody)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		if (this.getSubject() == null || !this.getSubject().isAuthenticated()) {
			log.debug("Subject does not exist or is not authenticated.  Tag body will be evaluated.");
			this.renderBody(env, body);
		} else {
			log.debug("Subject exists and is authenticated.  Tag body will not be evaluated.");
		}
	}
}