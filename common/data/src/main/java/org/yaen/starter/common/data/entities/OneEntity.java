/**
 * 
 */
package org.yaen.starter.common.data.entities;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIgnore;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneTableHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * one entity(persistent object) for all crud operation, all entity has to inherit this one. if can not inherite this
 * one, try use another entity to hold other object as entity.
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public class OneEntity implements BaseEntity {
	private static final long serialVersionUID = 218626629147397851L;

	/** the actual entity, maybe self */
	@OneIgnore
	protected BaseEntity entity;

	/** the primary key */
	@Getter
	@Setter
	@OneIgnore
	private long id;

	/** the table name, if null, use class name instead */
	@OneIgnore
	protected String tableName;

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
			name = this.entity.getClass().getSimpleName().toUpperCase();
		}

		// set to member
		this.tableName = name;

		return tableName;
	}

	/**
	 * columns, with key of field name
	 */
	@OneIgnore
	protected Map<String, OneColumnEntity> columns;

	/**
	 * getter of columns. get all field and value
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, OneColumnEntity> getColumns() throws Exception {
		// use local var to modify
		Map<String, OneColumnEntity> col = new LinkedHashMap<String, OneColumnEntity>();

		this.fetchOneColumnInfo(col, this.entity, this.entity.getClass());

		// remove id, as is primary key
		col.remove("id");
		col.remove("serialVersionUID");

		// set to member
		this.columns = col;

		return this.columns;
	}

	/** the modified field name */
	@Getter
	@Setter
	@OneIgnore
	private String modifiedFieldName;

	/** the added field name */
	@Getter
	@Setter
	@OneIgnore
	private String addedFieldName;

	/** the id list for batch select */
	@Getter
	@Setter
	@OneIgnore
	private List<Long> ids;

	/**
	 * construct one entity of self
	 */
	public OneEntity() {
		this.entity = this;
	}

	/**
	 * fetch one column info
	 * 
	 * @param model
	 * @param one
	 * @param clazz
	 */
	protected void fetchOneColumnInfo(Map<String, OneColumnEntity> columns, Object one, Class<?> clazz)
			throws Exception {
		if (clazz == null)
			return;

		// try to get super class, ignore interface
		Class<?> superclazz = clazz.getSuperclass();

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
			OneIgnore ignore = field.getAnnotation(OneIgnore.class);

			// ignore
			if (ignore == null) {

				// add columns
				if (data != null) {
					// add data field
					String column_name = data.FieldName();
					if ((column_name == null || column_name.trim().isEmpty())) {
						column_name = field.getName().toUpperCase();
					}

					OneColumnEntity info = new OneColumnEntity();
					info.setColumnName(column_name);
					info.setValue(field.get(one));
					info.setDataType(data.DataType());
					info.setDataSize(data.DataSize());
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
			} // ignore
		} // for
	}

}
