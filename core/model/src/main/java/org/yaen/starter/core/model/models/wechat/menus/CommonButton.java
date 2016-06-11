package org.yaen.starter.core.model.models.wechat.menus;

import lombok.Getter;
import lombok.Setter;

/**
 * common button, no sub button
 * 
 * @author Yaen 2016年6月11日下午3:24:20
 */
@Getter
@Setter
public class CommonButton extends Button {
	
	/** the type of button, click/ */
	private String type;

	/** the key of button */
	private String key;

}
