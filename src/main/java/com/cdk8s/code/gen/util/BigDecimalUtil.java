package com.cdk8s.code.gen.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 随机数工具类
 */
public final class BigDecimalUtil {


	//=====================================Apache Common 包 start=====================================


	//=====================================Apache Common 包  end=====================================

	//=====================================其他包 start=====================================


	//=====================================其他包  end=====================================

	//=====================================自定义 start=====================================

	/**
	 * 输入多个数字进行相乘
	 *
	 * @param values
	 * @return
	 */
	public static Double multiply(Number... values) {
		BigDecimal result = new BigDecimal(1);
		for (Number v : values) {
			if (v == null) {
				continue;
			}
			result = result.multiply(new BigDecimal(String.valueOf(v)), MathContext.DECIMAL64);
		}
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 输入多个数字进行相加
	 *
	 * @param values
	 * @return
	 */
	public static Double add(Number... values) {
		BigDecimal result = BigDecimal.ZERO;
		for (Number v : values) {
			if (v == null) {
				continue;
			}
			result = result.add(new BigDecimal(String.valueOf(v)), MathContext.DECIMAL64);
		}
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 前面减后面
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static Double subtract(Number start, Number end) {
		BigDecimal result = new BigDecimal(String.valueOf(start == null ? 0 : start)).subtract(new BigDecimal(String.valueOf(end == null ? 0 : end)));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 前台除后面
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static Double divide(Number start, Number end) {
		BigDecimal result = new BigDecimal(String.valueOf(start == null ? 0 : start)).divide(new BigDecimal(String.valueOf(end)), MathContext.DECIMAL64);
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	//=====================================自定义  end=====================================


	//=====================================私有方法 start=====================================

	//=====================================私有方法  end=====================================

}



