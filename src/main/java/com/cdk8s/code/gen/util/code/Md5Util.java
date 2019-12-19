package com.cdk8s.code.gen.util.code;

import com.cdk8s.code.gen.util.StringUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;


@Slf4j
public final class Md5Util {


	/**
	 * MD5 哈希（Apache Common）
	 */
	public static String md5ByApache(String source) {
		return DigestUtils.md5Hex(source).toLowerCase();
	}

	/**
	 * MD5，支持迭代次数
	 *
	 * @param source
	 * @param iterations 迭代次数
	 * @return
	 */
	public static String md5ByApache(String source, int iterations) {
		if (iterations < 1) {
			return null;
		}
		for (int i = 0; i < iterations; i++) {
			source = md5ByApache(source);
		}

		return source;
	}


	/**
	 * 把字符串编码为 MD5 字符串(通过Google guava)
	 * 新版本 guava 不推荐使用 md5 哈希，而是使用 sha256 或更高水平的哈希
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String md5ByGuava(final String sourceString) {
		if (StringUtil.isNotBlank(sourceString)) {
			return Hashing.md5().hashString(sourceString, Charset.forName(Charsets.UTF_8.toString())).toString();
		}
		return null;
	}

	/**
	 * Guava 废弃掉 md5，推荐 sha256 或更高级别
	 *
	 * @param sourceString
	 * @return
	 */
	public static String sha256ByGuava(final String sourceString) {
		if (StringUtil.isNotBlank(sourceString)) {
			return Hashing.sha256().hashString(sourceString, Charset.forName(Charsets.UTF_8.toString())).toString();
		}
		return null;
	}
}
