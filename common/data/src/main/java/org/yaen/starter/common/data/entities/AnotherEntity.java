package org.yaen.starter.common.data.entities;

import org.yaen.starter.common.data.exceptions.CoreException;

/**
 * another entity(persistent object) which can contain another object, which must implement BaseEntity and have an "id"
 * field of type long
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public class AnotherEntity extends OneEntity {
	private static final long serialVersionUID = -7232516405181212096L;

	/**
	 * bridge rowid to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getRowid()
	 */
	@Override
	public long getRowid() {
		return this.entity.getRowid();
	};

	/**
	 * bridge rowid to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#setRowid(long)
	 */
	@Override
	public void setRowid(long rowid) {
		this.entity.setRowid(rowid);
	};

	/**
	 * bridge id to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getId()
	 */
	@Override
	public String getId() {
		return this.entity.getId();
	}

	/**
	 * bridge id to entity
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.entity.setId(id);
	}

	/**
	 * create another po with given entity
	 * 
	 * @param another
	 * @throws CoreException
	 */
	public AnotherEntity(BaseEntity another) {
		super();

		if (another == null)
			throw new IllegalArgumentException("another entity should not be null");

		this.entity = another;
	}
}
