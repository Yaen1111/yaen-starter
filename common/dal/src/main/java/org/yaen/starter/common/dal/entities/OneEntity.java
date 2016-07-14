package org.yaen.starter.common.dal.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * one entity(persistent object) abstract for all crud operation, all entity has to inherit this one. if can not inherit
 * this one, try use another entity to hold other object as entity.
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public abstract class OneEntity implements BaseEntity {
	private static final long serialVersionUID = 218626629147397851L;

	/** the actual entity, maybe self */
	protected BaseEntity entity;

	/** the primary key */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.BIGINT, FieldName = "ROWID")
	protected long rowid = 0;

	/** the main key */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR32, FieldName = "ID")
	protected String id = "";

	/** set to true to enable auto table */
	@Getter
	protected boolean autoTable = true;

	/** the table name, if null, use class name instead */
	protected String tableName = "";

	/** the indexes to create */
	protected List<String> indexes = new ArrayList<String>();

	/** the uniqueIndexes to create */
	protected List<String> uniqueIndexes = new ArrayList<String>();

	/** columns, with key of field name */
	protected Map<String, OneColumnEntity> columns;

	/** the selected column name */
	@Getter
	@Setter
	protected String selectedColumnName = "";

	/** the modified field name */
	@Getter
	@Setter
	protected String modifiedFieldName = "";

	/** the added field name */
	@Getter
	@Setter
	protected String addedFieldName = "";

	/** the rowid list for batch select */
	@Getter
	@Setter
	protected List<Long> rowids = new ArrayList<Long>();

	/**
	 * protected constructor with entity
	 * 
	 * @param entity
	 */
	protected OneEntity(BaseEntity entity) {
		AssertUtil.notNull(entity, "entity should not be null");
		AssertUtil.isTrue(entity != this, "entity should not be self");
		this.entity = entity;
	}

	/**
	 * construct one entity of self
	 */
	protected OneEntity() {
		this.entity = this;
	}

	/**
	 * fetch one column info, if child has same field as parent, parent will be ignored
	 * 
	 * @param model
	 * @param one
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected void fetchOneColumnInfo(Map<String, OneColumnEntity> columns, Object one, Class<?> clazz)
			throws IllegalArgumentException, IllegalAccessException {
		if (clazz == null)
			return;

		// try to get super class, ignore interface
		Class<?> superclazz = clazz.getSuperclass();

		// fetch parent first, if the child override some field, the parent field will be ignored
		if (superclazz != null) {
			this.fetchOneColumnInfo(columns, one, superclazz);
		}

		// get all private fields
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// allow private field access
			field.setAccessible(true);

			// get all element annotation
			OneData data = field.getAnnotation(OneData.class);
			OneCopy copy = field.getAnnotation(OneCopy.class);

			// add columns
			if (data != null) {
				// add data field
				String column_name = data.FieldName();
				if ((column_name == null || column_name.trim().isEmpty())) {
					column_name = StringUtil.toCamelUpper(field.getName());
				}

				OneColumnEntity info = new OneColumnEntity();
				info.setColumnName(column_name);
				info.setValue(field.get(one));
				info.setDataType(data.DataType());
				info.setDataSize(data.DataSize());
				info.setScaleSize(data.ScaleSize());
				info.setField(field);

				columns.put(field.getName(), info);
			} else if (copy != null) {
				// add copy
				String prefix = copy.Prefix();

				// get copy entity, and do only if the entity is not null
				BaseEntity copyentity = (BaseEntity) field.get(one);
				if (copyentity != null) {
					AnotherEntity another = new AnotherEntity(copyentity);
					Map<String, OneColumnEntity> copycolumns = another.getColumns();

					// copy all columns with prefix
					for (String copykey : copycolumns.keySet()) {

						OneColumnEntity info = copycolumns.get(copykey);
						info.setColumnName(prefix + info.getColumnName());

						columns.put(prefix + copykey, info);
					}
				}

			} else {
				// nothing is set, ignore
			}
		} // for
	}

	/**
	 * getter of tableName
	 * 
	 * @return
	 */
	public String getTableName() {
		// use local var to modify
		String name = "";

		// try get interface
		if (this.entity instanceof OneTableHandler) {

			name = ((OneTableHandler) this.entity).getTableName();
		} else {
			// try get annotation
			OneTable table = this.entity.getClass().getAnnotation(OneTable.class);

			// get table name from one table
			if (table != null) {
				name = table.TableName();
			}
		}

		// default to class name if empty
		if (name == null || name.trim().isEmpty()) {
			name = StringUtil.toCamelUpper(this.entity.getClass().getSimpleName());
		}

		// set to member
		this.tableName = name;

		return this.tableName;
	}

	/**
	 * getter for indexes
	 * 
	 * @return
	 */
	public List<String> getIndexes() {
		// use local var to modify
		List<String> list = new ArrayList<String>();

		// try get annotation
		OneIndex index = this.entity.getClass().getAnnotation(OneIndex.class);

		// add index from one index
		if (index != null) {
			String[] temp = index.value();
			if (temp != null && temp.length > 0) {
				for (int i = 0; i < temp.length; i++) {
					list.add(temp[i]);
				}
			}
		}

		// set to member
		this.indexes = list;

		return this.indexes;
	}

	/**
	 * getter for uniqueIndexes
	 * 
	 * @return
	 */
	public List<String> getUniqueIndexes() {
		// use local var to modify
		List<String> list = new ArrayList<String>();

		// try get annotation
		OneUniqueIndex index = this.entity.getClass().getAnnotation(OneUniqueIndex.class);

		// add index from one index
		if (index != null) {
			String[] temp = index.value();
			if (temp != null && temp.length > 0) {
				for (int i = 0; i < temp.length; i++) {
					list.add(temp[i]);
				}
			}
		}

		// set to member
		this.uniqueIndexes = list;

		return this.uniqueIndexes;
	}

	/**
	 * getter of columns. get all field and value
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public Map<String, OneColumnEntity> getColumns() throws IllegalArgumentException, IllegalAccessException {
		// use local var to modify
		Map<String, OneColumnEntity> col = new LinkedHashMap<String, OneColumnEntity>();

		this.fetchOneColumnInfo(col, this.entity, this.entity.getClass());

		// remove rowid, as is auto primary key
		col.remove("rowid");
		col.remove("serialVersionUID");

		// set to member
		this.columns = col;

		return this.columns;
	}

	/**
	 * fill entity fields by given value
	 * 
	 * @param values
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void fillValues(Map<String, Object> values) throws IllegalArgumentException, IllegalAccessException {

		// fill rowid as is not contained in the column list
		if (values.containsKey("ROWID")) {
			this.setRowid((Long) values.get("ROWID"));
		}

		// get columns if not
		if (this.columns == null)
			this.getColumns();

		// fill model fields by value map
		for (Entry<String, OneColumnEntity> entry : this.columns.entrySet()) {
			OneColumnEntity info = entry.getValue();
			Field field = info.getField();

			// set value if exists
			if (values.containsKey(info.getColumnName())) {
				Object value = values.get(info.getColumnName());

				// set value of any type
				field.set(this.entity, value);
			}
		}
	}

	/**
	 * triggers, can be trigger chain, but only for simple and single-entity operations
	 * 
	 * <pre>
	 * if (super.BeforeSelect) {
	 * 	// do something
	 * 	return true; // true if need next, false for all done
	 * } else {
	 * 	return false; // super already done, do nothing but return false
	 * }
	 * </pre>
	 * 
	 * @param service
	 * @return true for next chain, false for done
	 */
	public boolean BeforeSelect() {
		return true;
	}

	public void AfterSelect() {
	}

	public boolean BeforeInsert() {
		return true;
	}

	public void AfterInsert() {
	}

	public boolean BeforeUpdate() {
		return true;
	}

	public void AfterUpdate() {
	}

	public boolean BeforeDelete() {
		return true;
	}

	public void AfterDelete() {
	}

}
