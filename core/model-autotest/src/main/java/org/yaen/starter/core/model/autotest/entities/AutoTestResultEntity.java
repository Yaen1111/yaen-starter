package org.yaen.starter.core.model.autotest.entities;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * auto test result entity
 * 
 * @author Yaen 2016年8月1日下午3:04:49
 */
@Getter
@Setter
@OneTable(TableName = "AT_TEST_RESULT")
@OneUniqueIndex("ID")
public class AutoTestResultEntity extends TwoEntity {
	private static final long serialVersionUID = 9071031058058907494L;

	/** the test case id */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String testCaseId;

	/** the test result, ok or error */
	@OneData(DataType = DataTypes.VARCHAR32)
	private String testResult;

	/** the test result message */
	@OneData(DataType = DataTypes.VARCHAR256)
	private String testResultMessage;

	/** the test response */
	@OneData(DataType = DataTypes.VARCHAR4096)
	private String testResponse;

}
