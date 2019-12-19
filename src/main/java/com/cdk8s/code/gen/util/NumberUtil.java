package com.cdk8s.code.gen.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 随机数工具类
 */
public final class NumberUtil {


	//=====================================Apache Common 包 start=====================================

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static int minimumInt(final int... array) {
		return NumberUtils.min(array);
	}

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static int maximumInt(final int... array) {
		return NumberUtils.max(array);
	}

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static long minimumLong(final long... array) {
		return NumberUtils.min(array);
	}

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static long maximumLong(final long... array) {
		return NumberUtils.max(array);
	}

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static double minimumDouble(final double... array) {
		return NumberUtils.min(array);
	}

	/**
	 * 在数组中找出最小值
	 *
	 * @param array
	 * @return
	 */
	public static double maximumDouble(final double... array) {
		return NumberUtils.max(array);
	}


	//=====================================Apache Common 包  end=====================================

	//=====================================其他包 start=====================================


	//=====================================其他包  end=====================================


	//=====================================私有方法 start=====================================

	//=====================================私有方法  end=====================================

}



