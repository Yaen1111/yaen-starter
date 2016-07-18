package org.yaen.starter.core.model.wechat.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * location request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:37:01
 */
@Getter
@Setter
public class LocationRequestMessage extends BaseRequestMessage {

	/** location x, longitude */
	private String locationX;

	/** location y, latitude */
	private String locationY;

	/** map scale size */
	private String scale;

	/** label address */
	private String label;

}
