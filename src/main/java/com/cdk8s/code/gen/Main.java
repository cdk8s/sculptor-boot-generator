package com.cdk8s.code.gen;

import com.cdk8s.code.gen.mapper.SysGeneratorMapper;
import com.cdk8s.code.gen.util.MybatisHelper;
import com.cdk8s.code.gen.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

@Slf4j
public class Main {

	//=====================================业务处理 start=====================================

	@SneakyThrows
	public static void main(String[] args) {
		Configuration config = getConfig();
		String tableName = config.getString("tableName");

		SqlSession sqlSession = MybatisHelper.getSqlSession();
		SysGeneratorMapper sysGeneratorMapper = sqlSession.getMapper(SysGeneratorMapper.class);

		String[] tableNames = StringUtils.split(tableName, ":");

		generatorFrontend(sysGeneratorMapper, config, tableNames);
		generatorBackend(sysGeneratorMapper, config, tableNames);
	}

	//=====================================业务处理  end=====================================
	//=====================================私有方法 start=====================================

	/**
	 * 获取配置信息
	 */
	private static Configuration getConfig() {
		try {
			return new PropertiesConfiguration("generator.properties");
		} catch (Exception e) {
			throw new RuntimeException("获取配置文件失败，", e);
		}
	}

	/**
	 * 前端生成逻辑
	 */
	private static void generatorFrontend(SysGeneratorMapper sysGeneratorMapper, Configuration config, String[] tableNames) {
		// 要考虑一对多，多对多，树结构等布局结构的逻辑，需要额外考虑页面布局
		boolean boolGeneratorFrontend = config.getBoolean("boolGeneratorFrontend");
		if (!boolGeneratorFrontend) {
			return;
		}

		String tableRelationFrontStyle = config.getString("tableRelationFrontStyle");

		if (tableNames.length == 3 && StringUtil.containsIgnoreCase(tableNames[1], "rel_")) {
			// 多对多操作
			String tableNameByFirst = tableNames[0];
			String tableNameBySecond = tableNames[1];
			String tableNameByThird = tableNames[2];

			Map<String, String> tableInfoByFirst = sysGeneratorMapper.queryTable(tableNameByFirst);
			Map<String, String> tableInfoBySecond = sysGeneratorMapper.queryTable(tableNameBySecond);
			Map<String, String> tableInfoByThird = sysGeneratorMapper.queryTable(tableNameByThird);


			List<Map<String, Object>> columnsByFirst = sysGeneratorMapper.queryColumns(tableNameByFirst);
			List<Map<String, Object>> columnsBySecond = sysGeneratorMapper.queryColumns(tableNameBySecond);
			List<Map<String, Object>> columnsByThird = sysGeneratorMapper.queryColumns(tableNameByThird);

			GeneratorFrontendByManyToManyUtil.generatorCode(
					config,
					tableInfoByFirst, columnsByFirst,
					tableInfoBySecond, columnsBySecond,
					tableInfoByThird, columnsByThird
			);

		} else if (!StringUtil.containsAny(tableRelationFrontStyle, "And")) {
			// 单表操作
			for (String tableNameItem : tableNames) {
				Map<String, String> tableInfo = sysGeneratorMapper.queryTable(tableNameItem);
				if (null == tableInfo) {
					continue;
				}
				List<Map<String, Object>> columns = sysGeneratorMapper.queryColumns(tableNameItem);
				GeneratorFrontendUtil.generatorCode(config, tableInfo, columns);
			}
		} else {
			// 一对多关联表操作
			if (tableNames.length != 2) {
				throw new RuntimeException("一对多关联表生成必须填写两个表名，例如：sys_param:sys_param_item");
			}
			String tableNameByOne = tableNames[0];
			String tableNameByMany = tableNames[1];

			Map<String, String> tableInfoByOne = sysGeneratorMapper.queryTable(tableNameByOne);
			List<Map<String, Object>> columnsByOne = sysGeneratorMapper.queryColumns(tableNameByOne);

			if (StringUtil.equals(tableRelationFrontStyle, "treeAndTable")) {
				//one 一方必须有 parent_id 字段
				boolean flag = false;
				for (Map<String, Object> map : columnsByOne) {
					String columnName = (String) map.get("columnName");
					if (StringUtil.equals(columnName, "parent_id")) {
						flag = true;
					}
				}

				if (!flag) {
					throw new RuntimeException("treeAndTable 布局 one 一方必须有 parent_id 字段");
				}
			}

			Map<String, String> tableInfoByMany = sysGeneratorMapper.queryTable(tableNameByMany);
			List<Map<String, Object>> columnsByMany = sysGeneratorMapper.queryColumns(tableNameByMany);

			GeneratorFrontendByOneToManyUtil.generatorCode(config, tableInfoByOne, columnsByOne, tableInfoByMany, columnsByMany);
		}
	}

	/**
	 * 后端生成逻辑
	 */
	private static void generatorBackend(SysGeneratorMapper sysGeneratorMapper, Configuration config, String[] tableNames) {
		// 一般不需要考虑一对多，多对多的结构逻辑，主要还是面向 API 的思维方式
		boolean boolGeneratorBackend = config.getBoolean("boolGeneratorBackend");
		if (!boolGeneratorBackend) {
			return;
		}

		// mysql8 有些细节和 mysql7 不太一样，所以这里要做特殊处理
		Map<String, String> mysqlVersion = sysGeneratorMapper.selectMySQLVersion();
		String boolMySQL8 = "false";
		if (StringUtil.startsWith(mysqlVersion.get("version"), "8.0")) {
			boolMySQL8 = "true";
		}

		for (String tableNameItem : tableNames) {
			Map<String, String> tableInfo = sysGeneratorMapper.queryTable(tableNameItem);
			if (null == tableInfo) {
				continue;
			}
			tableInfo.put("boolMySQL8", boolMySQL8);
			List<Map<String, Object>> columns = sysGeneratorMapper.queryColumns(tableNameItem);
			GeneratorBackendUtil.generatorCode(config, tableInfo, columns);
		}
	}

	//=====================================私有方法  end=====================================


}
