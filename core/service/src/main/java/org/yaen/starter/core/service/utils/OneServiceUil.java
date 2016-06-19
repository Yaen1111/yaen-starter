package org.yaen.starter.core.service.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.yaen.starter.common.dal.entities.OneColumnEntity;
import org.yaen.starter.core.model.models.BaseModel;

/**
 * one service util
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public class OneServiceUil {

	/**
	 * fill model by data in map, with rowid and id
	 * 
	 * @param model
	 * @param columns
	 * @param values
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T extends BaseModel> void fillModelByColumns(T model, Map<String, OneColumnEntity> columns,
			Map<String, Object> values) throws IllegalArgumentException, IllegalAccessException {

		// fill rowid and id
		if (values.containsKey("rowid")) {
			model.setRowid((Long) values.get("rowid"));
		}

		// fill model fields by value map
		for (Entry<String, OneColumnEntity> entry : columns.entrySet()) {
			OneColumnEntity info = entry.getValue();
			Field field = info.getField();

			// set value if exists
			if (values.containsKey(info.getColumnName())) {
				Object value = values.get(info.getColumnName());

				// set value of any type
				field.set(model, value);
			}
		}
	}

}
