/**
 * 
 */
package org.yaen.starter.common.data.objects;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * pager for most case
 * 
 * @author Yaen 2015年12月1日上午12:27:12
 */
public class Pager implements Serializable {
	private static final long serialVersionUID = -6042765037026590908L;

	/**
	 * total item, set by user
	 */
	@Getter
	@Setter
	private int totalItem = 0;

	/**
	 * items per page, default to 10
	 */
	@Getter
	@Setter
	private int itemPerPage = 10;

	/**
	 * current page, should not
	 */
	@Getter
	@Setter
	private int currentPage = 0;

	/**
	 * common pager
	 */
	public Pager() {

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%1-%2-%3", this.totalItem, this.itemPerPage, this.currentPage);
	}

	/**
	 * try to parse given str to pager
	 * 
	 * @param str
	 */
	public void TryParse(String str) {
		// TODO
	}

}
