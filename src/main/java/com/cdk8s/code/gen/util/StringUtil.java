package com.cdk8s.code.gen.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类, 继承StringUtils类
 */
public final class StringUtil {

	public static final String EMPTY = "";

	//=====================================Apache Common 包 start=====================================

	public static boolean isNotBlank(final String str) {
		return StringUtils.isNotBlank(str);
	}

	public static boolean isBlank(final String str) {
		return StringUtils.isBlank(str);
	}

	public static boolean containsAny(final String str, final CharSequence... search) {
		return StringUtils.containsAny(str, search);
	}

	public static boolean containsIgnoreCase(final String str, final String search) {
		return StringUtils.containsIgnoreCase(str, search);
	}

	/**
	 * 返回字符串第一次出现的下标位置，如果不存在返回 -1。如果存返回从 0 开始的下标。
	 */
	public static int indexOf(final String str, final String search) {
		return StringUtils.indexOf(str, search);
	}

	/**
	 * 根据下标截取字符串
	 */
	public static String substring(final String str, final int start, final int end) {
		return StringUtils.substring(str, start, end);
	}

	/**
	 * 根据前后字符串截取字符
	 */
	public static String substringBetween(final String str, final String open, final String close) {
		return StringUtils.substringBetween(str, open, close);
	}

	/**
	 * 截取第一个 search 匹配到之后的字符串，不包含 search 字符
	 * 比如：(abbccddef,c) 得到的结果就是：cddef
	 */
	public static String substringAfter(final String str, final String search) {
		return StringUtils.substringAfter(str, search);
	}

	/**
	 * 截取第一个 search 匹配到之前的字符串，不包含 search 字符
	 * 比如：(abbccddef,c) 得到的结果就是：abb
	 */
	public static String substringBefore(final String str, final String search) {
		return StringUtils.substringBefore(str, search);
	}

	/**
	 * 截取最后一个 search 匹配到之前的字符串，不包含 search 字符
	 * 比如：(abbccddef,c) 得到的结果就是：abbc
	 */
	public static String substringBeforeLast(final String str, final String search) {
		return StringUtils.substringBeforeLast(str, search);
	}

	/**
	 * 截取最后一个 search 匹配到之后的字符串，不包含 search 字符
	 * 比如：(abbccddef,c) 得到的结果就是：ddef
	 */
	public static String substringAfterLast(final String str, final String search) {
		return StringUtils.substringAfterLast(str, search);
	}

	/**
	 * 删除前后空格
	 */
	public static String trim(final String str) {
		return StringUtils.trim(str);
	}

	public static boolean equals(final String str1, final String str2) {
		return StringUtils.equals(str1, str2);
	}

	public static boolean notEquals(final String str1, final String str2) {
		return !StringUtils.equals(str1, str2);
	}

	public static boolean equalsIgnoreCase(final String str1, final String str2) {
		return StringUtils.equalsIgnoreCase(str1, str2);
	}

	public static boolean notEqualsIgnoreCase(final String str1, final String str2) {
		return !StringUtils.equalsIgnoreCase(str1, str2);
	}

	public static String lowerCase(final String str1) {
		return StringUtils.lowerCase(str1);
	}

	public static String upperCase(final String str1) {
		return StringUtils.upperCase(str1);
	}

	/**
	 * 检查字符串是否以某个字符串开头
	 */
	public static boolean startsWith(final String str, final String prefix) {
		return StringUtils.startsWith(str, prefix);
	}

	/**
	 * 检查字符串是否以某几个中的任意一个字符串开头
	 */
	public static boolean startsWithAny(final String str, final List<String> stringList) {
		String[] strings = CollectionUtil.toArray(stringList, String.class);
		return StringUtils.startsWithAny(str, strings);
	}

	/**
	 * 检查字符串是否以某个字符串结尾
	 */
	public static boolean endsWith(final String str, final String suffix) {
		return StringUtils.endsWith(str, suffix);
	}

	/**
	 * 检查字符串是否以某几个中的任意一个字符串结尾
	 */
	public static boolean endsWithAny(final String str, final List<String> stringList) {
		String[] strings = CollectionUtil.toArray(stringList, String.class);
		return StringUtils.endsWithAny(str, strings);
	}

	public static boolean endsWithAny(final CharSequence sequence, final CharSequence... searchStrings) {
		return StringUtils.endsWithAny(sequence, searchStrings);
	}

	/**
	 * 如果有多个位置被匹配到，也只有第一个匹配会被替换
	 */
	public static String replaceOnce(final String text, final String searchString, final String replacement) {
		return StringUtils.replaceOnce(text, searchString, replacement);
	}

	/**
	 * 如果有多个位置被匹配到，都会被替换
	 */
	public static String replace(final String text, final String searchString, final String replacement) {
		return StringUtils.replace(text, searchString, replacement);
	}

	/**
	 * 如果有多个位置被匹配到，则根据 max 次数替换。如果 max = 2，则只表示匹配两次替换即可
	 */
	public static String replace(final String text, final String searchString, final String replacement, final int max) {
		return StringUtils.replace(text, searchString, replacement, max);
	}

	public static String replaceIgnoreCase(final String text, final String searchString, final String replacement) {
		return StringUtils.replaceIgnoreCase(text, searchString, replacement);
	}

	/**
	 * 比如：(aa#bb#, #) = aa,bb（一共 2 个元素）
	 */
	public static List<String> split(String str, String separator) {
		return CollectionUtil.toList(StringUtils.split(str, separator));
	}

	public static List<String> splitAndTrim(String str, String separator) {
		List<String> result = new ArrayList<>();
		String[] split = StringUtils.split(str, separator);
		for (String temp : split) {
			result.add(trim(temp));
		}
		return result;
	}

	/**
	 * 比如：(aa#bb#, #) = aa,bb,""（一共 3 个元素）
	 */
	public static List<String> splitByWholeSeparator(String str, String separator) {
		return CollectionUtil.toList(StringUtils.splitByWholeSeparator(str, separator));
	}

	/**
	 * 比如：(":cd:ef:", ":") = ["", cd", "ef", ""]，保留所有元素，包括空字符
	 */
	public static List<String> splitPreserveAllTokens(String str, String separator) {
		return CollectionUtil.toList(StringUtils.splitPreserveAllTokens(str, separator));
	}

	/**
	 * 多个符号都可以进行分割(Guava)
	 * 比如：("apple.banana,,orange,,.", "[.|,]") = [apple, banana, orange]
	 */
	public static List<String> splitOnPatternByGuava(String str, String separatorPattern) {
		return Splitter.onPattern(separatorPattern).omitEmptyStrings().splitToList(str);
	}

	/**
	 * 相反设置
	 */
	public static String uncapitalize(String str) {
		return StringUtils.uncapitalize(str);
	}

	//=====================================Apache Common 包  end=====================================

	//=====================================其他包 start=====================================

	/**
	 * 下划线的字符串转换为驼峰式字符串
	 */
	public static String lowerUnderscoreToLowerCamel(String str) {
		CaseFormat fromFormat = CaseFormat.LOWER_UNDERSCORE;//
		CaseFormat toFormat = CaseFormat.LOWER_CAMEL;
		return fromFormat.to(toFormat, str);
	}

	/**
	 * 驼峰式的字符串转换为下划线
	 */
	public static String lowerCamelToLowerUnderscore(String str) {
		CaseFormat fromFormat = CaseFormat.LOWER_CAMEL;
		CaseFormat toFormat = CaseFormat.LOWER_UNDERSCORE;
		return fromFormat.to(toFormat, str);
	}

	//=====================================其他包  end=====================================


	//=====================================私有方法 start=====================================

	//=====================================私有方法  end=====================================

}



