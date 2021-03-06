package org.yaen.starter.core.model.wechat.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * music response message for wechat
 * 
 * @author Yaen 2016年6月6日下午11:46:28
 */
@Getter
@Setter
public class MusicResponseMessage extends BaseResponseMessage {

	/** the music */
	private Music music;

}
