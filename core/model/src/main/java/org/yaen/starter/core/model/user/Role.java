package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * role object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_ROLE")
public class Role extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** title */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String title;

	/** description */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

	/**
	 * constructor
	 */
	public Role() {
		super();
	}
}
