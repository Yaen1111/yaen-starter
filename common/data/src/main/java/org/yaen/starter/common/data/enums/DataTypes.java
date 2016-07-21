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

	/** varchar 256, for description, long string */
	public static final String VARCHAR256 = "VARCHAR(256)";

	/** varchar 512, for some code, long string */
	public static final String VARCHAR512 = "VARCHAR(512)";

	/** varchar 1024, for url/path, very-long string */
	public static final String VARCHAR1024 = "VARCHAR(1024)";

	/** varchar 4096, for log content, very-long string */
	public static final String VARCHAR4096 = "VARCHAR(4096)";

	public static final String BIGINT = "BIGINT";
	public static final String INT = "INT";
	public static final String DATE = "DATE";
	public static final String DATETIME = "DATETIME";
	public static final String DECIMAL = "DECIMAL";
	public static final String TEXT = "MEDIUMTEXT";
}
