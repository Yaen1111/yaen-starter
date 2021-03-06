/**
 * 
 */
package org.yaen.starter.web.home.viewmodels;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.yaen.starter.common.util.utils.AssertUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * common view model
 * 
 * @author Yaen 2016年5月20日下午5:12:21
 */
public class ViewModel implements Model {

	/** the json view name */
	private static final String JSON_VIEW_NAME = "json";

	/** the page title */
	@Getter
	@Setter
	private String title;

	/**
	 * get shiro user
	 * 
	 * @return
	 */
	public Subject getUser() {
		return SecurityUtils.getSubject();
	}

	/** the inner hash map */
	Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * @see org.springframework.ui.Model#addAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public Model addAttribute(String attributeName, Object attributeValue) {
		AssertUtil.notNull(attributeName);

		this.map.put(attributeName, attributeValue);
		return this;
	}

	/**
	 * @see org.springframework.ui.Model#addAttribute(java.lang.Object)
	 */
	@Override
	public Model addAttribute(Object attributeValue) {
		AssertUtil.notNull(attributeValue);

		this.map.put(org.springframework.core.Conventions.getVariableName(attributeValue), attributeValue);
		return this;
	}

	/**
	 * @see org.springframework.ui.Model#addAllAttributes(java.util.Collection)
	 */
	@Override
	public Model addAllAttributes(Collection<?> attributeValues) {
		AssertUtil.notNull(attributeValues);

		for (Object attributeValue : attributeValues) {
			this.addAttribute(attributeValue);
		}

		return this;
	}

	/**
	 * @see org.springframework.ui.Model#addAllAttributes(java.util.Map)
	 */
	@Override
	public Model addAllAttributes(Map<String, ?> attributes) {
		AssertUtil.notNull(attributes);

		this.map.putAll(attributes);
		return this;
	}

	/**
	 * @see org.springframework.ui.Model#mergeAttributes(java.util.Map)
	 */
	@Override
	public Model mergeAttributes(Map<String, ?> attributes) {
		AssertUtil.notNull(attributes);

		for (Entry<String, ?> entry : attributes.entrySet()) {
			this.map.put(entry.getKey(), entry.getValue());
		}

		return this;
	}

	/**
	 * @see org.springframework.ui.Model#containsAttribute(java.lang.String)
	 */
	@Override
	public boolean containsAttribute(String attributeName) {
		return this.map.containsKey(attributeName);
	}

	/**
	 * @see org.springframework.ui.Model#asMap()
	 */
	@Override
	public Map<String, Object> asMap() {
		// add fixed item
		this.map.put("title", this.title);
		this.map.put("user", this.getUser());

		return this.map;
	}

	/**
	 * return as view
	 * 
	 * @param viewName
	 * @return
	 */
	public ModelAndView asView(String viewName) {
		return new ModelAndView(viewName, this.asMap());
	}

	/**
	 * return as view, using title
	 * 
	 * @return
	 */
	public ModelAndView asView() {
		return new ModelAndView(this.getTitle(), this.asMap());
	}

	/**
	 * return as json
	 * 
	 * @return
	 */
	public ModelAndView asJson() {
		return new ModelAndView(JSON_VIEW_NAME, this.asMap());
	}

}
