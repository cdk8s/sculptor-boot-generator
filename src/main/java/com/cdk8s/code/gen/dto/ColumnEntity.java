package com.cdk8s.code.gen.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ColumnEntity {
	/**
	 * 列表
	 */
	private String columnName;

	/**
	 * 默认值
	 */
	private String columnDefault;

	/**
	 * 最大值
	 */
	private Integer maxValue;

	/**
	 * 是否为枚举字段
	 */
	private Boolean boolIsEnum = false;

	/**
	 * 是否允许为 NULL
	 */
	private Boolean boolIsNullable = false;

	/**
	 * 字符串最大长度
	 */
	private Integer characterMaximumLength;

	/**
	 * 数据库类型
	 */
	private String dataType;

	/**
	 * 备注
	 */
	private String comment;

	/**
	 * 短的备注（去掉冒号后面部分）
	 */
	private String shortComment;

	/**
	 * 大写开头驼峰属性
	 */
	private String upperAttrName;

	/**
	 * 小写开头驼峰属性
	 */
	private String lowerAttrName;

	/**
	 * java 属性类型
	 */
	private String attrType;

	/**
	 * 其他信息
	 */
	private String extra;
}
