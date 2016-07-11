package org.yaen.starter.common.data.enums;

/**
 * data type definition
 * 
 * @author Yaen 2016年1月13日上午11:52:48
 */
public final class DataTypes {
	public static final String VARCHAR = "VARCHAR";

	/** varchar 32, for id, code, name, type, guid */
	public static final String VARCHAR32 = "VARCHAR(32)";

	/** varchar 64, for title, short string, also for hash, salt */
	public static final String VARCHAR64 = "VARCHAR(64)";

	/** varchar 250, for description, long string */
	public static final String VARCHAR250 = "VARCHAR(250)";

	/** varchar 1000, for url/path, very-long string */
	public static final String VARCHAR1000 = "VARCHAR(1000)";

	public static final String BIGINT = "BIGINT";
	public static final String INT = "INT";
	public static final String DATETIME = "DATETIME";
	public static final String DECIMAL = "DECIMAL";
	public static final String TEXT = "MEDIUMTEXT";
}
