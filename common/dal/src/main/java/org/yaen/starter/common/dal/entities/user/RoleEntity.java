package org.yaen.starter.common.dal.entities.user;

import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * role object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZU_ROLE")
@OneUniqueIndex("ID")
public class RoleEntity extends TwoEntity {
	private static final long serialVersionUID = -709733522935110043L;

	/** group name */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String groupName;

	/** title */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** description */
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

}
