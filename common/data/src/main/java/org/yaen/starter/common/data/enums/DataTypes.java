package org.yaen.starter.common.data.enums;

/**
 * data type definition
 * 
 * @author Yaen 2016年1月13日上午11:52:48
 */
public final class DataTypes {
	public static final String VARCHAR = "VARCHAR";

	/** varchar 20, for id, code, name, type */
	public static final String VARCHAR20 = "VARCHAR(20)";

	/** varchar 50, for title, short string */
	public static final String VARCHAR50 = "VARCHAR(50)";

	/** varchar 250, for description, long string */
	public static final String VARCHAR250 = "VARCHAR(250)";

	/** varchar 64, for hash, salt */
	public static final String VARCHAR64 = "VARCHAR(64)";
	public static final String BIGINT = "BIGINT";
	public static final String INT = "INT";
	public static final String DATETIME = "DATETIME";
	public static final String DECIMAL = "DECIMAL";
	public static final String TEXT = "MEDIUMTEXT";
}
