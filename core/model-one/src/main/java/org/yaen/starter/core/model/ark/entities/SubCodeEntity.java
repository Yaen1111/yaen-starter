package org.yaen.starter.core.model.ark.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;

import lombok.Getter;
import lombok.Setter;

/**
 * sub code element, mostly for test purpose
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@Getter
@Setter
@OneTable(TableName = "ARK_SUBCODE")
@OneIndex("SUBCODE")
public class SubCodeEntity extends NamedOneEntity {
	private static final long serialVersionUID = -1638352121549503589L;

	/** the sub code */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String subcode;

	/** the code family, that is the group of the code */
	@OneData(DataType = DataTypes.INT)
	private Integer intvalue;

	/** the title for the code */
	@OneData(DataType = DataTypes.BIGINT)
	private Long longvalue;

}
