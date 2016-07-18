package org.yaen.starter.core.model.wechat.objects;

import org.yaen.starter.core.model.wechat.enums.ButtonTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * view button, type is view, jump direct to url with out server side
 * 
 * @author Yaen 2016年6月11日下午3:24:20
 */
@Getter
@Setter
public class ViewButton extends Button {

	/** the type of button, click/view */
	private String type = ButtonTypes.VIEW;

	/** the url of button */
	private String url;

}
