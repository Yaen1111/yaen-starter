package org.yaen.starter.core.model.pojos.wechat;

import org.yaen.starter.core.model.enums.wechat.ButtonTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * click button, type is click, call server side to do something
 * 
 * @author Yaen 2016年6月11日下午3:24:20
 */
@Getter
@Setter
public class ClickButton extends Button {

	/** the type of button, click/view */
	private String type = ButtonTypes.CLICK;

	/** the key of button */
	private String key;

}
