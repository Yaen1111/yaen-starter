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

	/** total item, set by user */
	@Getter
	@Setter
	private int totalItem = 0;

	/** items per page, default to 0 for all */
	@Getter
	@Setter
	private int itemPerPage = 0;

	/** current page, 0-based */
	@Getter
	@Setter
	private int currentPage = 0;

	/**
	 * constructor
	 * 
	 * @param totalItem
	 * @param itemPerPage
	 * @param currentPage
	 */
	public Pager(int totalItem, int itemPerPage, int currentPage) {
		this.totalItem = totalItem;
		this.itemPerPage = itemPerPage;
		this.currentPage = currentPage;
	}

	/**
	 * constructor
	 * 
	 * @param itemPerPage
	 */
	public Pager(int itemPerPage) {
		this(0, itemPerPage, 0);
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
	public boolean TryParse(String str) {
		if (str == null)
			return false;

		// split by -, and need at least 3
		String[] temp = str.split("-");
		if (temp == null || temp.length < 3)
			return false;

		try {
			// parse
			int t = Integer.parseInt(temp[0]);
			int p = Integer.parseInt(temp[1]);
			int c = Integer.parseInt(temp[2]);

			// set if all ok
			this.totalItem = t;
			this.itemPerPage = p;
			this.currentPage = c;

			return true;

		} catch (NumberFormatException ex) {
			return false;
		}
	}

}
