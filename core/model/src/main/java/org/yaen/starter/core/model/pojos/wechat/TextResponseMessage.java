package org.yaen.starter.core.model.pojos.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * text response message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:45:26
 */
@Getter
@Setter
public class TextResponseMessage extends BaseResponseMessage {

	/** text context */
	private String content;

}
