package com.yale.test.java.app.easyexcel.constant;

public class ExcelConstant {
	/**
	 * 每个sheet存储的记录数100w
	 */
	public static final Integer PER_SHEET_ROW_COUNT = 1000000;

	/**
	 * 每次向excel写入的记录数(查询每页数据大小)20w
	 */
	public static final Integer PER_WRITE_ROW_COUNT = 200000;
}
