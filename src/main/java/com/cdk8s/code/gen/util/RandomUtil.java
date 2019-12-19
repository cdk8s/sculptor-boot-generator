package com.cdk8s.code.gen.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

/**
 * 随机数工具类
 */
public final class RandomUtil {


	//=====================================Apache Common 包 start=====================================

	/**
	 * 随机正整数
	 *
	 * @param startInclusive 包括，最小 0
	 * @param endExclusive   不包括
	 * @return
	 */
	public static int nextInt(final int startInclusive, final int endExclusive) {
		return RandomUtils.nextInt(startInclusive, endExclusive);
	}

	/**
	 * 随机正长整数
	 *
	 * @param startInclusive 包括，最小 0
	 * @param endExclusive   不包括
	 * @return
	 */
	public static long nextLong(final int startInclusive, final int endExclusive) {
		return RandomUtils.nextLong(startInclusive, endExclusive);
	}

	/**
	 * 随机正双精度浮点数（默认精度 2，四舍五入）
	 *
	 * @param startInclusive 包括，最小 0.00
	 * @param endExclusive   不包括
	 * @return
	 */
	public static double nextDouble(final int startInclusive, final int endExclusive) {
		return nextDouble(startInclusive, endExclusive, 2);
	}

	/**
	 * 随机正双精度浮点数（默认精度 2，四舍五入）
	 *
	 * @param startInclusive 包括，最小 0.00
	 * @param endExclusive   不包括
	 * @param scale          精度
	 * @return
	 */
	public static double nextDouble(final int startInclusive, final int endExclusive, Integer scale) {
		if (null == scale) {
			scale = 2;
		}
		double nextDouble = RandomUtils.nextDouble(startInclusive, endExclusive);
		return new BigDecimal(nextDouble).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 随机字符串，包含大小写字母和数字
	 *
	 * @param count
	 * @return
	 */
	public static String randomAlphanumeric(final int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}

	/**
	 * 随机数字字符串
	 *
	 * @param count
	 * @return
	 */
	public static String randomNumeric(final int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 随机字母字符串，包含大小写
	 *
	 * @param count
	 * @return
	 */
	public static String randomAlphabetic(final int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}

	/**
	 * 随机字母字符串，只包含小写
	 *
	 * @param count
	 * @return
	 */
	public static String randomAlphabeticByLowerCase(final int count) {
		return StringUtil.lowerCase(randomAlphabetic(count));
	}

	/**
	 * 随机 Ascii 字符串
	 *
	 * @param count
	 * @return
	 */
	public static String randomAscii(final int count) {
		return RandomStringUtils.randomAscii(count);
	}

	/**
	 * 随机自定义区间的字符串
	 *
	 * @param count
	 * @return
	 */
	public static String randomCustomString(final int count) {
		return RandomStringUtils.random(count, new char[]{'a', 'b', 'c', 'd', 'e', 'f', '1', '2', '3'});
	}


	//=====================================Apache Common 包  end=====================================

	//=====================================其他包 start=====================================


	//=====================================其他包  end=====================================


	//=====================================私有方法 start=====================================

	//=====================================私有方法  end=====================================

}



