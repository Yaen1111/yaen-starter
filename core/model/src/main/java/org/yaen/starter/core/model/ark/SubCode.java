/**
 * 
 */
package org.yaen.starter.core.model.ark;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.ark.NamedOne;

import lombok.Getter;
import lombok.Setter;

/**
 * sub code element, mostly for test purpose
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@OneTable(TableName = "ARK_SUBCODE")
@OneIndex("SUBCODE")
public class SubCode extends NamedOne {
	private static final long serialVersionUID = -1638352121549503589L;

	/**
	 * the sub code
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String subcode;

	/**
	 * the code family, that is the group of the code
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.INT)
	private Integer intvalue;

	/**
	 * the title for the code
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.BIGINT)
	private Long longvalue;

}
