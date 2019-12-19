package com.cdk8s.code.gen.util.code;

import com.cdk8s.code.gen.util.StringUtil;
import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具类
 */
@Slf4j
public final class CodecUtil {

	//utf-8转成url可传输的  可读→可传输
	public static String encodeURL(String source) {
		String target;
		try {
			target = URLEncoder.encode(source, "UTF-8");
		} catch (Exception e) {
			log.error("encode url failure", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	//utf-8解码   可传输→可读
	public static String decodeURL(String source) {
		String target;
		try {
			target = URLDecoder.decode(source, "UTF-8");
		} catch (Exception e) {
			log.error("decode url failure", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 把base64字符串解码为源字符串数组(通过Google guava)
	 *
	 * @return
	 */
	public static byte[] base64StringToSourceByteByGuava(final String base64String) {
		if (StringUtil.isNotBlank(base64String)) {
			try {
				return BaseEncoding.base64().decode(base64String);
			} catch (IllegalArgumentException e) {
				System.out.println("--------------------------------把base64字符串解码为源字符数组出错，数据格式错误");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 把字节编码为base64进制字符串(通过Google guava)
	 *
	 * @return
	 */
	public static String byteToBase64StringByGuava(final byte[] bytes) {
		if (null != bytes) {
			return BaseEncoding.base64().encode(bytes);
		}
		return null;
	}

}
