package org.yaen.starter.core.model.user;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * auth object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_AUTH")
@OneUniqueIndex("ID")
public class Auth extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** override the id to be long enough */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50, FieldName = "ID")
	protected String id = "";

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
	public Auth() {
		super();
	}
}
