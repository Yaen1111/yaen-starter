package org.yaen.starter.web.home.shiro;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;

/**
 * shiro principal(user info)
 * 
 * @author Yaen 2016年5月19日下午6:42:54
 */
@Data
public class ShiroPrincipal implements Serializable {
	private static final long serialVersionUID = 2591810631949952332L;

	/** user name */
	private String username;

	/**
	 * constructor
	 * 
	 * @param username
	 */
	public ShiroPrincipal(String username) {
		this.username = username;
	}
}
