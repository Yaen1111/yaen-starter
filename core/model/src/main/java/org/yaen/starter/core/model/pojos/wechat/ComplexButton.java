package org.yaen.starter.core.model.pojos.wechat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * complex button, with sub button
 * 
 * @author Yaen 2016年6月11日下午3:26:08
 */
@Getter
@Setter
public class ComplexButton extends Button {

	/** the sub button */
	private List<Button> sub_button = new ArrayList<Button>();

}
