package org.yaen.starter.core.model.models.excel.handlers;

import java.util.List;

/**
 * the excel file process handler, to process each rows
 * 
 * @author Yaen 2016年6月14日下午5:01:11
 */
public interface ExcelHandler {

	/**
	 * process each rows from the excel file, should not throw exception
	 * 
	 * @param sheetIndex
	 * @param curRow
	 * @param rowlist
	 * @param userData
	 */
	void processEachRows(int sheetIndex, int curRow, List<String> rowlist, Object userData);
}
