package com.cdk8s.code.gen;

import com.cdk8s.code.gen.mapper.SysGeneratorMapper;
import com.cdk8s.code.gen.util.MybatisHelper;
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
		boolean boolGeneratorBackend = config.getBoolean("boolGeneratorBackend");
		boolean boolGeneratorFrontend = config.getBoolean("boolGeneratorFrontend");

		SqlSession sqlSession = MybatisHelper.getSqlSession();
		SysGeneratorMapper sysGeneratorMapper = sqlSession.getMapper(SysGeneratorMapper.class);

		String[] tableNames = StringUtils.split(tableName, ":");
		for (String tableNameItem : tableNames) {
			Map<String, String> table = sysGeneratorMapper.queryTable(tableNameItem);
			if (null == table) {
				continue;
			}
			List<Map<String, Object>> columns = sysGeneratorMapper.queryColumns(tableNameItem);
			if (boolGeneratorBackend) {
				GeneratorBackendUtil.generatorCode(config, table, columns);
			}
			if (boolGeneratorFrontend) {
				GeneratorFrontendUtil.generatorCode(config, table, columns);
			}

		}

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

	//=====================================私有方法  end=====================================


}
