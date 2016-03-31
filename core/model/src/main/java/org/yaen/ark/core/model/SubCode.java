/**
 * 
 */
package org.yaen.ark.core.model;

import org.yaen.ark.core.model.annotations.ElementData;
import org.yaen.ark.core.model.annotations.ElementTable;
import org.yaen.ark.core.model.enums.DataTypes;
import org.yaen.ark.core.model.NameElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * sub code element, mostly for test purpose
 * 
 * @author xl 2016年1月4日下午8:38:45
 */
@ToString(callSuper = true)
@ElementTable(TableName = "ARK_SUBCODE")
public class SubCode extends NameElement {

	/**
	 * the sub code
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.VARCHAR, DataSize = 20)
	private String subcode;

	/**
	 * the code family, that is the group of the code
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.INT)
	private int intvalue;

	/**
	 * the title for the code
	 */
	@Getter
	@Setter
	@ElementData(DataType = DataTypes.BIGINT)
	private long longvalue;

}
