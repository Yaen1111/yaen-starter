/**
 * 
 */
package org.yaen.starter.common.data.entities;

import java.util.LinkedHashMap;
import java.util.Map;

import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.models.BaseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * another entity(persistent object) which can contain another object, which must
 * have an "id" field of type bigint
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public class AnotherEntity extends OneEntity {
	private static final long serialVersionUID = 100111L;

	/**
	 * another object for the PO
	 */
	@Getter
	@Setter
	private BaseModel another;

	/**
	 * bridge id to another
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getId()
	 */
	@Override
	public long getId() {
		return this.another.getId();
	};

	/**
	 * bridge id to another
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.another.setId(id);
	};

	/**
	 * get table name of another
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getTableName()
	 */
	@Override
	public String getTableName() {
		// use local var to modify
		String name = "";

		OneTable table = this.another.getClass().getAnnotation(OneTable.class);

		// get table name from one table
		if (table != null) {
			name = table.TableName();
		}

		// default to class name if empty
		if (name == null || name.trim().isEmpty()) {
			name = this.another.getClass().getSimpleName().toUpperCase();
		}

		// set to member
		this.tableName = name;

		return tableName;
	}

	/**
	 * get columns of another
	 * 
	 * @see org.yaen.starter.common.data.entities.OneEntity#getColumns()
	 */
	@Override
	public Map<String, OneColumnEntity> getColumns() throws Exception {
		// use local var to modify
		Map<String, OneColumnEntity> col = new LinkedHashMap<String, OneColumnEntity>();

		try {
			// try get column info
			this.getOneColumnInfo(col, this.another, this.another.getClass());
		} catch (Exception ex) {
			// just throw out
			throw ex;
		}

		// remove id, as is primary key
		col.remove("id");
		col.remove("serialVersionUID");

		// set to member
		this.columns = col;

		return this.columns;
	}

	/**
	 * create another po with given entity
	 * 
	 * @param another
	 */
	public AnotherEntity(BaseModel another) {
		super();

		if (another == null)
			throw new IllegalArgumentException("another should not be null");

		this.another = another;
	}
}
