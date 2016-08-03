package org.yaen.starter.core.model.excel.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * excel 2007 event mode processor
 * <p>
 * set handler to get each row
 * 
 * @author Yaen 2016年6月14日下午4:00:56
 */
public class Excel2007Processor extends DefaultHandler {

	/** the handler */
	private ExcelHandler handler;

	/** the user data for handler */
	private Object userData;

	private SharedStringsTable sst;
	private String lastContents;
	private boolean nextIsString;

	private int sheetIndex = -1;
	private List<String> rowlist = new ArrayList<String>();
	private int curRow = 0; // 当前行
	private int curCol = 0; // 当前列索引
	private int preCol = 0; // 上一列列索引

	/** the title row, usually is 0 */
	@Getter
	@Setter
	private int titleRow = 0; // 标题行，一般情况下为0

	private int rowsize = 0; // 列数

	/**
	 * constructor
	 * 
	 * @param handler
	 * @param userData
	 */
	public Excel2007Processor(ExcelHandler handler, Object userData) {
		super();

		this.handler = handler;
		this.userData = userData;
	}

	/**
	 * process one sheet, sheet id is 1-based
	 * 
	 * @param filename
	 * @param sheetId
	 * @throws Exception
	 */
	public void processOneSheet(String filename, int sheetId) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// rId2 found by processing the Workbook
		// 根据 rId# 或 rSheet# 查找sheet
		InputStream sheet2 = r.getSheet("rId" + sheetId);
		sheetIndex++;
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}

	/**
	 * process whole excel sheets
	 * 
	 * @param filename
	 * @throws OpenXML4JException
	 * @throws IOException
	 * @throws SAXException
	 */
	public void process(String filename) throws OpenXML4JException, IOException, SAXException {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			curRow = 0;
			sheetIndex++;
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}
	}

	/**
	 * get parser
	 * 
	 * @param sst
	 * @return
	 * @throws SAXException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}

	/**
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
	 *      org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		// c => 单元格
		if (name.equals("c")) {
			// 如果下一个元素是 SST 的索引，则将nextIsString标记为true
			String cellType = attributes.getValue("t");
			String rowStr = attributes.getValue("r");
			curCol = this.getRowIndex(rowStr);
			if (cellType != null && cellType.equals("s")) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}
		}
		// 置空
		lastContents = "";
	}

	/**
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		// 根据SST的索引值的到单元格的真正要存储的字符串
		// 这时characters()方法可能会被调用多次
		if (nextIsString) {
			try {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			} catch (Exception e) {

			}
		}

		// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
		// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
		if (name.equals("v")) {
			String value = lastContents.trim();
			value = value.equals("") ? " " : value;
			int cols = curCol - preCol;
			if (cols > 1) {
				for (int i = 0; i < cols - 1; i++) {
					rowlist.add(preCol, "");
				}
			}
			preCol = curCol;
			rowlist.add(curCol - 1, value);
		} else {
			// 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
			if (name.equals("row")) {
				int tmpCols = rowlist.size();
				if (curRow > this.titleRow && tmpCols < this.rowsize) {
					for (int i = 0; i < this.rowsize - tmpCols; i++) {
						rowlist.add(rowlist.size(), "");
					}
				}

				// call handler
				this.handler.processEachRows(sheetIndex, curRow, rowlist, this.userData);

				if (curRow == this.titleRow) {
					this.rowsize = rowlist.size();
				}
				rowlist.clear();
				curRow++;
				curCol = 0;
				preCol = 0;
			}
		}
	}

	/**
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// 得到单元格内容的值
		lastContents += new String(ch, start, length);
	}

	/**
	 * 得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引， 如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
	 * 
	 * @param rowStr
	 * @return
	 */
	public int getRowIndex(String rowStr) {
		rowStr = rowStr.replaceAll("[^A-Z]", "");
		byte[] rowAbc = rowStr.getBytes();
		int len = rowAbc.length;
		float num = 0;
		for (int i = 0; i < len; i++) {
			num += (rowAbc[i] - 'A' + 1) * Math.pow(26, len - i - 1);
		}
		return (int) num;
	}

}
