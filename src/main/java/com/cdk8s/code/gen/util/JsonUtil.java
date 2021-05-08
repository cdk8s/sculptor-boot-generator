package com.cdk8s.code.gen.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public final class JsonUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 将 POJO 转为 JSON
	 */
	public static <T> String toJson(T obj) {
		String json;
		try {
			json = OBJECT_MAPPER.writeValueAsString(obj);
		} catch (Exception e) {
			log.error("convert POJO to JSON failure", e);
			throw new RuntimeException(e);
		}
		return json;
	}

	/**
	 * 将 POJO 转为 JSON 带格式化
	 */
	public static <T> String toJsonPretty(T obj) {
		String json;
		try {
			json = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			log.error("------zch------convert POJO to JSON failure", e);
			throw new RuntimeException(e);
		}
		return json;
	}

	/**
	 * 将 JSON 转为 POJO
	 */
	public static <T> T toObject(String json, Class<T> type) {
		T pojo;
		try {
			pojo = OBJECT_MAPPER.readValue(json, type);
		} catch (Exception e) {
			log.error("convert JSON to POJO failure", e);
			throw new RuntimeException(e);
		}
		return pojo;
	}

	/**
	 * 将 JSON 转为 List
	 */
	public static <T> T toList(String json, TypeReference<T> type) {
		try {
			return OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			log.error("convert JSON to List failure", e);
			throw new RuntimeException(e);
		}
	}

	public static Map<String, String> objectToMap(Object object) {
		return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, String>>() {
		});
	}

	public static Map<String, Object> objectToMapObject(Object object) {
		return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 格式化json字符串
	 *
	 * @param jsonStr 需要格式化的json串
	 * @return 格式化后的json串
	 */
	public static String formatJson(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			//遇到{ [换行，且下一行缩进
			switch (current) {
				case '{':
				case '[':
					sb.append(current);
					sb.append('\n');
					indent++;
					addIndentBlank(sb, indent);
					break;
				//遇到} ]换行，当前行缩进
				case '}':
				case ']':
					sb.append('\n');
					indent--;
					addIndentBlank(sb, indent);
					sb.append(current);
					break;
				//遇到,换行
				case ',':
					sb.append(current);
					if (last != '\\') {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					sb.append(current);
			}
		}
		return sb.toString();
	}


	//======================================================

	/**
	 * 添加space
	 *
	 * @param sb
	 * @param indent
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

}
