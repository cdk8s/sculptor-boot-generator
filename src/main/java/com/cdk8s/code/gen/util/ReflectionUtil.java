package com.cdk8s.code.gen.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class ReflectionUtil {


	/**
	 * 创建实例
	 */
	public static Object newInstance(Class<?> cls) {
		Object instance;
		try {
			instance = cls.newInstance();
		} catch (Exception e) {
			log.error("new instance failure", e);
			throw new RuntimeException(e);
		}
		return instance;
	}

	/**
	 * 创建实例（根据类名）
	 */
	public static Object newInstance(String className) {
		Class<?> cls = ClassUtil.loadClass(className, true);
		return newInstance(cls);
	}

	/**
	 * 调用方法
	 */
	public static Object invokeMethod(Object obj, Method method, Object... args) {
		Object result;
		try {
			method.setAccessible(true);
			result = method.invoke(obj, args);
		} catch (Exception e) {
			log.error("invoke method failure", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 给实例的这个字段set值
	 *
	 * @param obj   实例
	 * @param field 字段
	 * @param value 值
	 */
	public static void setField(Object obj, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			log.error("set field failure", e);
			throw new RuntimeException(e);
		}
	}


	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null) {
			return null;
		}

		Object obj = beanClass.newInstance();

		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}

			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}

		return obj;
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}

		return map;
	}

	public static Map<String, String> objectToMapString(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), String.valueOf(field.get(obj)));
		}

		return map;
	}

	public static Map<String, String> objectToMapStringSnakeCase(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(StringUtil.lowerCamelToLowerUnderscore(field.getName()), String.valueOf(field.get(obj)));
		}

		return map;
	}

}
