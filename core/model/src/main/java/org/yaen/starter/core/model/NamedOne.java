/**
 * 
 */
package org.yaen.starter.core.model;


import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * code element, only for unique code/name
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@ToString(callSuper = true)
public abstract class NamedOne extends OneModel {
	private static final long serialVersionUID = 7297317955813307397L;

	/**
	 * the name
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String name;

	/**
	 * the family
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String family;

	/**
	 * the title
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 50)
	private String title;

	/**
	 * the description
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 250)
	private String description;
}
