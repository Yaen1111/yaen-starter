/**
 * 
 */
package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common view object
 * 
 * @author Yaen 2015年12月1日上午12:27:12
 */
public class VoT2<T1, T2> extends Vo {
	private static final long serialVersionUID = -5448258857687062969L;

	/** item 1 */
	@Getter
	@Setter
	private T1 item1;

	/** item 2 */
	@Getter
	@Setter
	private T2 item2;

	/**
	 * common view object
	 */
	public VoT2() {
		super();
	}

}
