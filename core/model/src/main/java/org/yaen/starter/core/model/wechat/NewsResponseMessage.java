package org.yaen.starter.core.model.wechat;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * news response message for wechat, with text and picture
 * 
 * @author Yaen 2016年6月6日下午11:48:41
 */
@Getter
@Setter
public class NewsResponseMessage extends BaseResponseMessage {

	/** the message count, limited to 10 */
	private int articleCount;

	/** the articles, the first one is big picture */
	private List<Article> articles;

}
