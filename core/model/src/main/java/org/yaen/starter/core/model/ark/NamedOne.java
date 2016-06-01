/**
 * 
 */
package org.yaen.starter.core.model.ark;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * code element, only for unique code/name
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@OneTable(TableName = "ARK_NAMED_ONE")
public class NamedOne extends OneModel {
	private static final long serialVersionUID = 7297317955813307397L;

	/**
	 * the name
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String name;

	/**
	 * the family
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String family;

	/**
	 * the title
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String title;

	/**
	 * the description
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;
}
