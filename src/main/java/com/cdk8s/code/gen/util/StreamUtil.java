package com.cdk8s.code.gen.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Slf4j
public final class StreamUtil {

	//输入流得全部字符串
	public static String getString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			log.error("get string failure", e);
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	/**
	 * 将输入流复制到输出流
	 */
	public static void copyStream(InputStream inputStream, OutputStream outputStream) {
		try {
			int length;
			byte[] buffer = new byte[4 * 1024];
			while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (Exception e) {
			log.error("copy stream failure", e);
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				log.error("close stream failure", e);
			}
		}
	}
}
