package org.yaen.starter.common.data.objects.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * link request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:40:48
 */
@Getter
@Setter
public class LinkRequestMessage extends BaseRequestMessage {

	/** title */
	private String title;

	/** description */
	private String description;

	/** url */
	private String url;

}
