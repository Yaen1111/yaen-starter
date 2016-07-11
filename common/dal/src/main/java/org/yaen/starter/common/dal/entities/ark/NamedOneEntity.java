package org.yaen.starter.common.dal.entities.ark;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * named one, the objects has name
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@Getter
@Setter
@OneTable(TableName = "ARK_NAMED_ONE")
public class NamedOneEntity extends TwoEntity {
	private static final long serialVersionUID = 7297317955813307397L;

	/** the name */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String name;

	/** the family */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String family;

	/** the title */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** the description */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;
}
