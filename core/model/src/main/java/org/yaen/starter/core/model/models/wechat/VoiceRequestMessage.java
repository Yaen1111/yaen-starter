package org.yaen.starter.core.model.models.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * voice request message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:35:22
 */
@Getter
@Setter
public class VoiceRequestMessage extends BaseRequestMessage {

	/** media id */
	private String mediaId;

	/** voice format */
	private String format;

}
