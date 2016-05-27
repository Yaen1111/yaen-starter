package org.yaen.starter.web.home.tags;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * JSP tag that renders the tag body only if the current user has executed a <b>successful</b> authentication attempt
 * <em>during their current session</em>.
 *
 * <p>
 * This is more restrictive than the {@link UserTag}, which only ensures the current user is known to the system, either
 * via a current login or from Remember Me services, which only makes the assumption that the current user is who they
 * say they are, and does not guarantee it like this tag does.
 *
 * <p>
 * The logically opposite tag of this one is the {@link NotAuthenticatedTag}
 *
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.AuthenticatedTag}
 * </p>
 *
 * @since 0.2
 */
@Slf4j
public class AuthenticatedTag extends SecureTag {

	/**
	 * render body if is authenticated
	 * 
	 * @see org.yaen.starter.web.home.tags.SecureTag#render(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateDirectiveBody)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		if (this.getSubject() != null && this.getSubject().isAuthenticated()) {
			if (log.isDebugEnabled()) {
				log.debug("Subject exists and is authenticated.  Tag body will be evaluated.");
			}

			this.renderBody(env, body);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Subject does not exist or is not authenticated.  Tag body will not be evaluated.");
			}
		}
	}
}