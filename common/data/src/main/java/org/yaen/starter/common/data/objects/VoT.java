/**
 * 
 */
package org.yaen.starter.common.data.objects;

import lombok.Getter;
import lombok.Setter;

/**
 * common view object
 * 
 * @param <T>
 * @author Yaen 2016年5月4日上午9:26:19
 */
public class VoT<T> extends Vo {
	private static final long serialVersionUID = 1130011306921457863L;
	
	/**
	 * the item of vo
	 */
	@Getter
	@Setter
	private T item;

	/**
	 * common view object
	 */
	public VoT() {
		super();
	}

}
