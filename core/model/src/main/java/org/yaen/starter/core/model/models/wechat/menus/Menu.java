package org.yaen.starter.core.model.models.wechat.menus;

import lombok.Getter;
import lombok.Setter;

/**
 * the whole menu, make up with buttons, up to 3 top-level bottun
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
@Getter
@Setter
public class Menu {

	/** the buttons */
	private Button[] button;

}
