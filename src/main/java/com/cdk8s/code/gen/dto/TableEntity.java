package com.cdk8s.code.gen.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableEntity {
	/**
	 * 名称
	 */
	private String tableName;
	/**
	 * 备注
	 */
	private String comments;
	/**
	 * 主键
	 */
	private ColumnEntity pk;
	/**
	 * 列名
	 */
	private List<ColumnEntity> columns;
	/**
	 * 大写开头驼峰类型
	 */
	private String upperClassName;
	/**
	 * 小写开头驼峰
	 */
	private String lowerClassName;
}
