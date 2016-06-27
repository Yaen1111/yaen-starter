package org.yaen.starter.common.data.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * common token which send between systems, mostly for request and response
 * 
 * @author Yaen 2015年11月30日下午11:56:39
 */
public class Token implements Serializable {
	private static final long serialVersionUID = -5838918950786836805L;

	/** token code */
	@Getter
	@Setter
	private String code = "";

	/** token message */
	@Getter
	@Setter
	private String message = "";

	/** token timestamp */
	@Getter
	@Setter
	private long timestamp = 0;

	/** token pager */
	@Getter
	@Setter
	private Pager pager = new Pager(0);

	/**
	 * empty token with current timestamp
	 */
	public Token() {
		this.timestamp = System.currentTimeMillis();
	}

}
