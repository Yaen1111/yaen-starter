package org.yaen.starter.core.model.user.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * auth object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@Getter
@Setter
@OneTable(TableName = "ZU_AUTH")
@OneUniqueIndex("ID")
public class AuthEntity extends TwoEntity {
	private static final long serialVersionUID = -709733522935110043L;

	/** override the id to be long enough */
	@OneData(DataType = DataTypes.VARCHAR64, FieldName = "ID")
	protected String id = "";

	/** title */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** description */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String description;

}
