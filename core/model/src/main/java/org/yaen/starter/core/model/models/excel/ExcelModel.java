package org.yaen.starter.core.model.models.excel;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;
import org.yaen.starter.core.model.models.OneModel;
import org.yaen.starter.core.model.models.excel.handlers.Excel2003Processor;
import org.yaen.starter.core.model.models.excel.handlers.Excel2007Processor;
import org.yaen.starter.core.model.models.excel.handlers.ExcelHandler;

/**
 * excel model, handles excel import/export
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
public abstract class ExcelModel extends OneModel implements ExcelHandler {

	/**
	 * empty constructor
	 */
	public ExcelModel() {
		super("1.1.0");
	}

	/**
	 * import excel file with user data object, the file should be xls or xlsx
	 * 
	 * @param filename
	 * @param userData
	 * @throws CoreException
	 */
	public void importExcelFile(String filename, Object userData) throws CoreException {
		AssertUtil.notBlank(filename);

		// get extensions
		String extension = filename.substring(filename.lastIndexOf(".") + 1);

		if (StringUtil.equalsIgnoreCase(extension, "xls")) {
			try {
				Excel2003Processor processor = new Excel2003Processor(this, userData);
				processor.process(filename);
			} catch (IOException ex) {
				throw new CoreException("file read error.", ex);
			}
		} else if (StringUtil.equalsIgnoreCase(extension, "xls")) {
			try {
				Excel2007Processor processor = new Excel2007Processor(this, userData);
				processor.process(filename);
			} catch (OpenXML4JException ex) {
				throw new CoreException("xml error error.", ex);
			} catch (IOException ex) {
				throw new CoreException("file read error.", ex);
			} catch (SAXException ex) {
				throw new CoreException("sax error.", ex);
			}
		} else {
			throw new CoreException("the filename should end with xls or xlsx. filename=" + filename);
		}
	}

}
