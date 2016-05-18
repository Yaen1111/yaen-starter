/**
 * 
 */
package org.yaen.starter.common.data.entities;

import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * another entity(persistent object) which can contain another object, which must have an "id" field of type bigint
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public class AnotherEntity extends OneEntity {
	private static final long serialVersionUID = 100111L;

	/**
	 * bridge id to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getId()
	 */
	@Override
	public long getId() {
		return this.entity.getId();
	};

	/**
	 * bridge id to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.entity.setId(id);
	};

	/**
	 * create another po with given entity
	 * 
	 * @param another
	 * @throws CoreException
	 */
	public AnotherEntity(BaseEntity another) throws CoreException {
		super();

		if (another == null)
			throw new CoreException("another should not be null");

		this.entity = another;
	}
}
