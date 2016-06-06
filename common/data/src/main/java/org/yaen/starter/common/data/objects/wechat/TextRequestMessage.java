package org.yaen.starter.common.data.objects.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * text request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:35:22
 */
@Getter
@Setter
public class TextRequestMessage extends BaseRequestMessage {

	/** text content */
	private String content;

}
