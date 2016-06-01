package org.yaen.starter.common.data.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;

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

	/** the camel upper case separator */
	public static final char CAMEL_SEPERATOR_CHAR = '_';

	/** the actual entity, maybe self */
	protected BaseEntity entity;

	/** the primary key */
	@Getter
	@Setter
	private long rowid = 0;

	/** the main key */
	@Getter
	@Setter
	private String id = "";

	/** the table name, if null, use class name instead */
	protected String tableName = "";

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
			name = toCamelUpper(this.entity.getClass().getSimpleName());
		}

		// set to member
		this.tableName = name;

		return this.tableName;
	}

	/** the indexes to create */
	private List<String> indexes = new ArrayList<String>();

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

	/** the uniqueIndexes to create */
	private List<String> uniqueIndexes = new ArrayList<String>();

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

	/** columns, with key of field name */
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

		// remove rowid, as is auto primary key
		col.remove("rowid");
		col.remove("serialVersionUID");

		// set to member
		this.columns = col;

		return this.columns;
	}

	/** the selected column name */
	@Getter
	@Setter
	private String selectedColumnName = "";

	/** the modified field name */
	@Getter
	@Setter
	private String modifiedFieldName = "";

	/** the added field name */
	@Getter
	@Setter
	private String addedFieldName = "";

	/** the rowid list for batch select */
	@Getter
	@Setter
	private List<Long> rowids = new ArrayList<Long>();

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

			// add columns
			if (data != null) {
				// add data field
				String column_name = data.FieldName();
				if ((column_name == null || column_name.trim().isEmpty())) {
					column_name = toCamelUpper(field.getName());
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
		} // for
	}

	/**
	 * to camel upper, getUserName = GET_USER_NAME
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelUpper(String s) {
		if (s == null)
			return "";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(CAMEL_SEPERATOR_CHAR);
				sb.append(c);
			} else {
				sb.append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * to camel lower, GET_USER_NAME = getUserName
	 * 
	 * @param s
	 * @return
	 */
	public static String toCamelLower(String s) {
		if (s == null)
			return "";

		StringBuilder sb = new StringBuilder();
		boolean is_upper = false;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == CAMEL_SEPERATOR_CHAR) {
				is_upper = true;
			} else {
				if (is_upper) {
					sb.append(Character.toUpperCase(c));
					is_upper = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
		}
		return sb.toString();
	}
}
