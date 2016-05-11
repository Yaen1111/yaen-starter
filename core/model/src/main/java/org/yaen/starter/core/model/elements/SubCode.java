/**
 * 
 */
package org.yaen.starter.core.model.elements;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.elements.NameElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * sub code element, mostly for test purpose
 * 
 * @author Yaen 2016年1月4日下午8:38:45
 */
@ToString(callSuper = true)
@OneTable(TableName = "ARK_SUBCODE")
public class SubCode extends NameElement {
	private static final long serialVersionUID = -1638352121549503589L;

	/**
	 * the sub code
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20)
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
