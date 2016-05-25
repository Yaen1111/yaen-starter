package org.yaen.starter.web.home.shiro;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;

/**
 * shiro user info
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
@Data
public class ShiroCredentials implements Serializable {
	private static final long serialVersionUID = 2591810641949952332L;

	/** hashed password */
	@NonNull
	private String passwordHash;

	/** password salt */
	@NonNull
	private String salt;
}
