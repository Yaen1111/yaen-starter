/**
 * 
 */
package org.yaen.starter.common.data.pos;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.Virtual;

import lombok.Getter;
import lombok.Setter;

/**
 * one PO(persistent object) for all crud operation, all po has to inherit this
 * one
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
public class OnePO implements Serializable {
	private static final long serialVersionUID = 100110L;

	/**
	 * the primary key
	 */
	@Getter
	@Setter
	private long id;

	/**
	 * the table name, if null, use class name instead
	 */
	protected String tableName;

	/**
	 * getter of tableName
	 * 
	 * @return
	 */
	public String getTableName() {
		// use local var to modify
		String name = "";

		OneTable table = this.getClass().getAnnotation(OneTable.class);

		// get table name from one table
		if (table != null) {
			name = table.TableName();
		}

		// default to class name if empty
		if (name == null || name.trim().isEmpty()) {
			name = this.getClass().getSimpleName().toUpperCase();
		}

		// set to member
		this.tableName = name;

		return tableName;
	}

	/**
	 * columns, with key of field name
	 */
	protected Map<String, OneColumnPO> columns;

	/**
	 * getter of columns. get all field and value
	 * 
	 * @return
	 * @throws Exception
	 */
	@Virtual
	public Map<String, OneColumnPO> getColumns() throws Exception {
		// use local var to modify
		Map<String, OneColumnPO> col = new LinkedHashMap<String, OneColumnPO>();

		try {
			// try get column info
			this.getOneColumnInfo(col, this, this.getClass());
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
	 * get one columns
	 * 
	 * @param model
	 * @param one
	 * @param clazz
	 */
	protected void getOneColumnInfo(Map<String, OneColumnPO> columns, Object one, Class<?> clazz) throws Exception {
		if (clazz == null)
			return;

		// try to get super class, ignore interface
		Class<?> superclazz = clazz.getSuperclass();

		if (superclazz != null) {
			this.getOneColumnInfo(columns, one, superclazz);
		}

		// get all private fields
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// allow private field access
			field.setAccessible(true);

			// get all element annotation
			OneData data = field.getAnnotation(OneData.class);

			// add columns
			if (data != null) {
				// add data field
				String column_name = data.FieldName();
				if ((column_name == null || column_name.trim().isEmpty())) {
					column_name = field.getName().toUpperCase();
				}

				OneColumnPO info = new OneColumnPO();
				info.setColumnName(column_name);
				info.setValue(field.get(one));
				info.setDataType(data.DataType());
				info.setDataSize(data.DataSize());
				info.setField(field);

				columns.put(field.getName(), info);
			} else {
				// no data, use field name
				String column_name = field.getName().toUpperCase();
				OneColumnPO info = new OneColumnPO();
				info.setColumnName(column_name);
				info.setValue(field.get(one));
				info.setField(field);

				columns.put(field.getName(), info);
			}
		}
	}

}
