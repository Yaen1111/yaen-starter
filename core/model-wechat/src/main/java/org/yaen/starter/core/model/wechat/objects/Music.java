package org.yaen.starter.core.model.wechat.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * the music object, mainly used by music response
 * 
 * @author Yaen 2016年6月6日下午11:47:30
 */
@Getter
@Setter
public class Music {

	/** music title */
	private String title;

	/** music description */
	private String description;

	/** music url */
	private String musicUrl;

	/** high quality music url, if in wifi, will use this for music */
	private String hqMusicUrl;

}
