package com.cdk8s.code.gen.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 集合工具类
 */
public final class CollectionUtil {


	//=====================================Apache Common 包 start=====================================


	public static boolean isNotEmpty(final Collection coll) {
		return CollectionUtils.isNotEmpty(coll);
	}

	public static boolean isEmpty(final Collection coll) {
		return CollectionUtils.isEmpty(coll);
	}


	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return MapUtils.isEmpty(map);
	}

	/**
	 * 把字符串转换成 list
	 */
	public static List<String> stringToList(String str, String separator) {
		String[] split = StringUtils.split(str, separator);
		return toList(split);
	}

	//=====================================Apache Common 包  end=====================================

	//=====================================Guava 包  end=====================================

	/**
	 * 把一个 list 拆分成多个子 list（使用 guava）
	 */
	public static <T> List<List<T>> partitionList(List<T> list, int groupSize) {
		return Lists.partition(list, groupSize);
	}

	/**
	 * 把 list 用分隔符组装成一个字符串
	 */
	public static String listJoinToString(List<String> list, String separator) {
		return Joiner.on(separator).join(list);
	}

	/**
	 * 把 map 用分隔符组装成一个字符串
	 * 假如：
	 * map.put("John", 1000);
	 * map.put("Jane", 1500);
	 * 当 keyValueSeparator 为 =
	 * 当 elementSeparator 为 ,
	 * 结果：John=1000,Jane=1500
	 */
	public static String mapToString(Map map, String keyValueSeparator, String elementSeparator) {
		return Joiner.on(elementSeparator).withKeyValueSeparator(keyValueSeparator).join(map);
	}

	/**
	 * 把 string 转换为 map
	 * 比如格式：John=1000,Jane=1500
	 */
	public static Map stringToMap(String str, String keyValueSeparator, String elementSeparator) {
		return Splitter.on(elementSeparator).withKeyValueSeparator(keyValueSeparator).split(str);
	}

	public static <T> ArrayList<T> newList(T... elements) {
		return Lists.newArrayList(elements);
	}

	public static <T> HashSet<T> newSet(T... elements) {
		return Sets.newHashSet(elements);
	}
	//=====================================Guava 包  end=====================================

	//=====================================其他包 start=====================================

	public static <T> List<T> setToList(Set<T> set) {
		return new ArrayList<>(set);
	}

	public static <T> Set<T> listToSet(List<T> list) {
		return new HashSet<>(list);
	}

	//=====================================其他包  end=====================================


	//=====================================私有方法 start=====================================

	/**
	 * 数组转换成 List
	 */
	public static <T> List<T> toList(T[] arrays) {
		List<T> list = new ArrayList<>();
		CollectionUtils.addAll(list, arrays);
		return list;
	}

	/**
	 * List 转换成数组
	 */
	public static <T> T[] toArray(Collection<T> collection, Class<T> componentType) {
		final T[] array = newArray(componentType, collection.size());
		return collection.toArray(array);
	}

	/**
	 * 新建一个空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	//=====================================私有方法  end=====================================

}



