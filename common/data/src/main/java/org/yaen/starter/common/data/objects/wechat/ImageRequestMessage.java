package org.yaen.starter.common.data.objects.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * image request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:36:13
 */
@Getter
@Setter
public class ImageRequestMessage extends BaseRequestMessage {

	/** picture url */
	private String picUrl;

}
