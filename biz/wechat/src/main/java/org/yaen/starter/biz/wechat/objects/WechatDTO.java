package org.yaen.starter.biz.wechat.objects;

import java.io.Serializable;

import lombok.Data;

/**
 * wechat dto
 * 
 * @author Yaen 2016年5月17日下午4:30:18
 */
@Data
public class WechatDTO implements Serializable {
	private static final long serialVersionUID = -6724045254585597274L;

	private String openid;

}
