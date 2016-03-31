/**
 * 
 */
package org.yaen.ark.core.model.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.ark.core.model.annotations.ElementCopy;
import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.spring.common.exceptions.BizException;
import org.yaen.spring.data.models.ElementInfo;
import org.yaen.spring.data.models.ElementModel;
import org.yaen.spring.common.utils.AssertUtil;
import org.yaen.spring.common.utils.ReflectUtil;
import org.yaen.spring.common.utils.StringUtil;

/**
 * 
 * @author xl 2016年1月29日下午2:12:34
 */
public class ElementUtil {

	/**
	 * get element table name, throw if null
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static String getElementTableName(Object entity) throws Exception {
		AssertUtil.notNull(entity);

		// get annotation
		ElementTable table = entity.getClass().getAnnotation(ElementTable.class);

		AssertUtil.notNull(table, "ElementTable is not set");

		String table_name = table.TableName();

		// if empty, use class name
		if (StringUtil.isBlank(table_name)) {
			table_name = entity.getClass().getSimpleName().toUpperCase();
		}

		return table_name;
	}

	/**
	 * get element info in columns
	 * 
	 * @param model
	 * @param element
	 * @param clazz
	 * @param callSuper
	 */
	private static <T extends BaseEntity> void getElementInfo(Map<String, ElementInfo> columns, T element,
			Class<?> clazz) throws Exception {
		if (clazz == null)
			return;

		// try to get super class
		Class<?> superclazz = clazz.getSuperclass();

		if (superclazz != null) {
			getElementInfo(columns, element, superclazz);
		}

		Field[] fields = ReflectUtil.getPrivateFields(clazz);

		for (Field field : fields) {
			// allow private field access
			field.setAccessible(true);

			if (StringUtil.like(field.getName(), "id")) {
				// id should skip
				continue;
			}

			// get all element annotation
			ElementData data = field.getAnnotation(ElementData.class);
			ElementCopy copy = field.getAnnotation(ElementCopy.class);

			// add data field
			if (data != null) {
				String column_name = data.FieldName();
				if (StringUtil.isBlank(column_name)) {
					column_name = field.getName().toUpperCase();
				}

				ElementInfo info = new ElementInfo();
				info.setColumnName(column_name);
				info.setValue(field.get(element));
				info.setDataType(data.DataType());
				info.setDataSize(data.DataSize());
				info.setField(field);

				columns.put(field.getName(), info);
			}

			// add copy fields
			if (copy != null) {
				// TODO
				// get object of the field, get the object as element,
				// include the columns
				throw new BizException("ElementCopy currently not supported.");
			}
		}
	}

	/**
	 * get element info for create table and other methods
	 * 
	 * @throws Exception
	 */
	public static <T extends BaseEntity> ElementModel GetElementModel(T element) throws Exception {
		AssertUtil.notNull(element);

		ElementModel model = new ElementModel();

		// set id
		model.setId(element.getId());

		// set table name
		model.setTableName(ElementUtil.getElementTableName(element));

		// make and set columns
		{
			Map<String, ElementInfo> columns = new LinkedHashMap<String, ElementInfo>();

			// call super
			getElementInfo(columns, element, element.getClass());

			model.setColumns(columns);
		}

		return model;
	}

}
