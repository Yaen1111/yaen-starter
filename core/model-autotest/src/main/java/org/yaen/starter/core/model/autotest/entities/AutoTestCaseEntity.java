package org.yaen.starter.core.model.autotest.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * auto test case entity
 * 
 * @author Yaen 2016年8月1日下午3:04:49
 */
@Getter
@Setter
@OneTable(TableName = "AT_TEST_CASE")
@OneUniqueIndex("ID")
public class AutoTestCaseEntity extends TwoEntity {
	private static final long serialVersionUID = 3139063379556209140L;

	/** the title of the test */
	@OneData(DataType = DataTypes.VARCHAR64)
	private String title;

	/** the test type, mainly used to determine test model, see TestTypes */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String testType;

	/** the api url for http related test */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String api;

	/** the param list json string */
	@OneData(DataType = DataTypes.VARCHAR1024)
	private String param;

	/** the demand response */
	@OneData(DataType = DataTypes.VARCHAR4096)
	private String demandResponse;

}
