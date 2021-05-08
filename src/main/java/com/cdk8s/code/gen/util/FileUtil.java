package com.cdk8s.code.gen.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 文件操作工具类
 */
@Slf4j
public final class FileUtil {

	@SneakyThrows
	public static void writeStringToFile(File file, String content) {
		FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
	}

	/**
	 * 判断文件是否存在
	 */
	@SneakyThrows
	public static Boolean checkFile(String filePath, Boolean boolCreate) {
		File file = new File(filePath);
		if (file.exists()) {
			// 已存在
			if (file.isDirectory()) {
				return false;
			}
			return true;
		} else {
			// 不存在
			if (boolCreate) {
				file.createNewFile();
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断文件夹是否存在
	 */
	@SneakyThrows
	public static Boolean checkDirectory(String directoryPath, Boolean boolCreate) {
		File file = new File(directoryPath);
		if (file.exists()) {
			// 已存在
			if (!file.isDirectory()) {
				return false;
			}
			return true;
		} else {
			// 不存在
			if (boolCreate) {
				file.createNewFile();
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取真实文件名（自动去掉文件路径）
	 */
	public static String getRealFileName(String fileName) {
		return FilenameUtils.getName(fileName);
	}

	/**
	 * 读取文件内容
	 */
	@SneakyThrows
	public static String readFileToString(String filePath) {
		return FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
	}

	/**
	 * 读取文件内容，相对于 classpath 下
	 */
	@SneakyThrows
	public static String readFileToStringByClasspath(String resourceLocation) {
		// 可以获取路径：/Users/youmeek/code_space/gitlab_code/sculptor-boot-backend/sculptor-boot-biz/target/classes/
		ClassLoader classLoader = FileUtil.class.getClassLoader();
		URL url = classLoader.getResource(resourceLocation);
		if (null == url) {
			return null;
		}
		File file = new File(url.getFile());

		return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
	}

	/**
	 * 创建文件
	 */
	public static File createFile(String filePath) {
		File file;
		try {
			file = new File(filePath);
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) {
				FileUtils.forceMkdir(parentDir);
			}
		} catch (Exception e) {
			log.error("create file failure", e);
			throw new RuntimeException(e);
		}
		return file;
	}
}
