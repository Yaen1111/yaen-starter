package org.yaen.starter.core.model.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * news article
 * 
 * @author Yaen 2016年6月6日下午11:49:57
 */
@Getter
@Setter
public class Article {

	/** the title */
	private String title;

	/** the description */
	private String description;

	/** the picture url, support jpg, png, big is 640*320, small is 80*80, the url should be same */
	private String picUrl;

	/** the news url */
	private String url;

}
