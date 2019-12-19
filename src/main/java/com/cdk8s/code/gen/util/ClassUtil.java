package com.cdk8s.code.gen.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 */
@Slf4j
public final class ClassUtil {


	//返回本线程的ClassLoader 用于类加载
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 加载类
	 *
	 * @param className     类名
	 * @param isInitialized 是否执行静态代码块
	 * @return 类
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			log.error("load class failure", e);
			throw new RuntimeException(e);
		}
		return cls;
	}


	/**
	 * 获取指定包名下的所有类
	 * 从文件一层层往下找 用文件名得到类名
	 * 让类加载器加载(不运行static块)这个类名的类  返回
	 */
	public static Set<Class<?>> getClassSet(String packageName) {

		//放这
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			//得此路径下所有文件
			String path = packageName.replace(".", "/");
			Enumeration<URL> urls = getClassLoader().getResources(path);

			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url == null) {
					continue;
				}
				addOneUrlClass(classSet, url);

			}
		} catch (Exception e) {
			log.error("get class set failure", e);
			throw new RuntimeException(e);
		}
		return classSet;
	}

	private static void addOneUrlClass(Set<Class<?>> classSet, URL url) throws IOException {

		String protocol = url.getProtocol();

		if ("file".equals(protocol)) {

			//一个遗留bug 不用管
			String packagePath = url.getPath().replaceAll("%20", " ");

			addOneFile(classSet, packagePath);
			return;
		}

		if ("jar".equals(protocol)) {
			addOneJar(classSet, url);
		}

	}

	private static void addOneJar(Set<Class<?>> classSet, URL url) throws IOException {

		JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
		if (jarURLConnection == null) {
			return;
		}
		JarFile jarFile = jarURLConnection.getJarFile();
		if (jarFile == null) {
			return;
		}

		Enumeration<JarEntry> jarEntries = jarFile.entries();

		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName();
			if (jarEntryName.endsWith(".class")) {
				String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
				doAddOneClass(classSet, className);
			}
		}
	}

	/**
	 * 把这个路径的类都加入指定classSet里面
	 *
	 * @param classSet    放class的地方
	 * @param packagePath 这个路径下的类要
	 */
	private static void addOneFile(Set<Class<?>> classSet, String packagePath) {

		//筛选这个路径下的文件夹 要求:
		//.class结尾 或者 是文件夹
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});

		for (File file : files) {

			String fileName = file.getName();
			//是文件的话 直接按名字加载进来就行
			if (file.isFile()) {
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				doAddOneClass(classSet, className);
				continue;
			}

			//要是是下级目录 加上这个名字 继续递归
			String subPackagePath = fileName;
			if (StringUtil.isNotBlank(packagePath)) {
				subPackagePath = packagePath + "/" + fileName;
			}
			addOneFile(classSet, subPackagePath);

		}
	}

	/**
	 * todo 最终都会到这 调用这里
	 * 指定类(不运行里面的statis)放入set
	 *
	 * @param classSet  放class的地方
	 * @param className 类名
	 */
	private static void doAddOneClass(Set<Class<?>> classSet, String className) {

		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
}
