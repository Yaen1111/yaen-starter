package org.yaen.starter.web.home.tags;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * JSP tag that renders the tag body if the current user <em>is not</em> known to the system, either because they
 * haven't logged in yet, or because they have no 'RememberMe' identity.
 *
 * <p>
 * The logically opposite tag of this one is the {@link UserTag}. Please read that class's JavaDoc as it explains more
 * about the differences between Authenticated/Unauthenticated and User/Guest semantic differences.
 *
 * <p>
 * Equivalent to {@link org.apache.shiro.web.tags.GuestTag}
 * </p>
 *
 * @since 0.9
 */
@Slf4j
public class GuestTag extends SecureTag {

	/**
	 * render if guest
	 * 
	 * @see org.yaen.starter.web.home.tags.SecureTag#render(freemarker.core.Environment, java.util.Map,
	 *      freemarker.template.TemplateDirectiveBody)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		if (this.getSubject() == null || this.getSubject().getPrincipal() == null) {
			if (log.isDebugEnabled()) {
				log.debug("Subject does not exist or does not have a known identity (aka 'principal').  "
						+ "Tag body will be evaluated.");
			}

			this.renderBody(env, body);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Subject exists or has a known identity (aka 'principal').  "
						+ "Tag body will not be evaluated.");
			}
		}
	}
}
