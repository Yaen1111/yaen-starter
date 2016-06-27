package org.yaen.starter.core.model.models.wechat.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * wechat access token
 * 
 * @author Yaen 2016年6月11日下午3:21:40
 */
@Getter
@Setter
public class AccessToken {

	/** the access token */
	private String token;

	/** the expire time, usually is 2 hour */
	private int expiresIn;

}
