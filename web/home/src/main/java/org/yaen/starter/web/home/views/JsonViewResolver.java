package org.yaen.starter.web.home.views;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.yaen.starter.common.util.utils.StringUtil;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

import lombok.Getter;
import lombok.Setter;

/**
 * json view resolver, the object will be convert to json with this view
 * 
 * @author Yaen 2016年5月19日下午4:04:20
 */
public class JsonViewResolver implements ViewResolver, Ordered {

	/** order of resolver, small is higher */
	@Getter
	@Setter
	private int order = Ordered.HIGHEST_PRECEDENCE;

	/**
	 * @see org.springframework.web.servlet.ViewResolver#resolveViewName(java.lang.String, java.util.Locale)
	 */
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if (StringUtil.equals("json", viewName)) {
			return new FastJsonJsonView();
		}
		// return null for not found, will try to get next view
		return null;
	}

}
