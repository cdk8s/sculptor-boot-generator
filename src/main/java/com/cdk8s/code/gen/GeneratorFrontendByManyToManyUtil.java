package com.cdk8s.code.gen;


import cn.hutool.core.util.StrUtil;
import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.StrategyContext;
import com.cdk8s.code.gen.strategy.frontend.*;
import com.cdk8s.code.gen.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.util.*;

@Slf4j
public final class GeneratorFrontendByManyToManyUtil {

	// =====================================业务 start=====================================

	/**
	 * 生成代码
	 */
	public static void generatorCode(
			Configuration config,
			Map<String, String> tableInfoByFirst, List<Map<String, Object>> columnsByFirst,
			Map<String, String> tableInfoBySecond, List<Map<String, Object>> columnsBySecond,
			Map<String, String> tableInfoByThird, List<Map<String, Object>> columnsByThird
	) {

		Map<String, Object> contextParamByFirst = GeneratorCommonUtil.buildContextParam(config, tableInfoByFirst, columnsByFirst);
		Map<String, Object> contextParamBySecond = GeneratorCommonUtil.buildContextParam(config, tableInfoBySecond, columnsBySecond);
		Map<String, Object> contextParamByThird = GeneratorCommonUtil.buildContextParam(config, tableInfoByThird, columnsByThird);
		TableEntity tableEntityByFirst = (TableEntity) contextParamByFirst.get("tableEntity");
		TableEntity tableEntityBySecond = (TableEntity) contextParamBySecond.get("tableEntity");
		TableEntity tableEntityByThird = (TableEntity) contextParamByThird.get("tableEntity");


		// zchtodo 必须确保 manyToManyForeignKeyName 是模糊搜索的
		buildManyToManyParam(config, contextParamByFirst, tableEntityByFirst, tableEntityBySecond, tableEntityByThird);
		buildManyToManyParam(config, contextParamByThird, tableEntityByFirst, tableEntityBySecond, tableEntityByThird);


		contextParamByFirst.put("isRelationTable", contextParamByFirst.get("isRelationTable"));
		contextParamByFirst.put("isIncludeStateEnum", contextParamByFirst.get("isIncludeStateEnum"));
		contextParamByFirst.put("isIncludeRanking", contextParamByFirst.get("isIncludeRanking"));
		contextParamByFirst.put("isIncludeCreateDate", contextParamByFirst.get("isIncludeCreateDate"));
		contextParamByFirst.put("isIncludeUpdateDate", contextParamByFirst.get("isIncludeUpdateDate"));
		contextParamByFirst.put("isIncludeDescription", contextParamByFirst.get("isIncludeDescription"));
		contextParamByFirst.put("frontendPageColumns", frontendPageColumns(tableEntityByFirst.getColumns()));
		contextParamByFirst.put("frontendPageNotBoolEnumColumns", frontendPageNotBoolEnumColumns(tableEntityByFirst.getColumns()));
		contextParamByFirst.put("frontendPageBoolEnumColumns", frontendPageBoolEnumColumns(tableEntityByFirst.getColumns()));
		contextParamByFirst.put("frontendNewModalColumns", frontendNewModalColumns(tableEntityByFirst.getColumns()));
		contextParamByFirst.put("frontendSearchFormInputColumns", frontendSearchFormInputColumns(tableEntityByFirst.getColumns()));
		contextParamByFirst.put("likeParamColumns", likeParamColumns(tableEntityByFirst.getColumns()));


		contextParamBySecond.put("isRelationTable", contextParamBySecond.get("isRelationTable"));
		contextParamBySecond.put("isIncludeStateEnum", contextParamBySecond.get("isIncludeStateEnum"));
		contextParamBySecond.put("isIncludeRanking", contextParamBySecond.get("isIncludeRanking"));
		contextParamBySecond.put("isIncludeCreateDate", contextParamBySecond.get("isIncludeCreateDate"));
		contextParamBySecond.put("isIncludeUpdateDate", contextParamBySecond.get("isIncludeUpdateDate"));
		contextParamBySecond.put("isIncludeDescription", contextParamBySecond.get("isIncludeDescription"));
		contextParamBySecond.put("frontendPageColumns", frontendPageColumns(tableEntityBySecond.getColumns()));
		contextParamBySecond.put("frontendPageNotBoolEnumColumns", frontendPageNotBoolEnumColumns(tableEntityBySecond.getColumns()));
		contextParamBySecond.put("frontendPageBoolEnumColumns", frontendPageBoolEnumColumns(tableEntityBySecond.getColumns()));
		contextParamBySecond.put("frontendNewModalColumns", frontendNewModalColumns(tableEntityBySecond.getColumns()));
		contextParamBySecond.put("frontendSearchFormInputColumns", frontendSearchFormInputColumns(tableEntityBySecond.getColumns()));
		contextParamBySecond.put("likeParamColumns", likeParamColumns(tableEntityBySecond.getColumns()));


		contextParamByThird.put("isRelationTable", contextParamByThird.get("isRelationTable"));
		contextParamByThird.put("isIncludeStateEnum", contextParamByThird.get("isIncludeStateEnum"));
		contextParamByThird.put("isIncludeRanking", contextParamByThird.get("isIncludeRanking"));
		contextParamByThird.put("isIncludeCreateDate", contextParamByThird.get("isIncludeCreateDate"));
		contextParamByThird.put("isIncludeUpdateDate", contextParamByThird.get("isIncludeUpdateDate"));
		contextParamByThird.put("isIncludeDescription", contextParamByThird.get("isIncludeDescription"));
		contextParamByThird.put("frontendPageColumns", frontendPageColumns(tableEntityByThird.getColumns()));
		contextParamByThird.put("frontendPageNotBoolEnumColumns", frontendPageNotBoolEnumColumns(tableEntityByThird.getColumns()));
		contextParamByThird.put("frontendPageBoolEnumColumns", frontendPageBoolEnumColumns(tableEntityByThird.getColumns()));
		contextParamByThird.put("frontendNewModalColumns", frontendNewModalColumns(tableEntityByThird.getColumns()));
		contextParamByThird.put("frontendSearchFormInputColumns", frontendSearchFormInputColumns(tableEntityByThird.getColumns()));
		contextParamByThird.put("likeParamColumns", likeParamColumns(tableEntityByThird.getColumns()));


		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		VelocityContext contextByFirst = new VelocityContext(contextParamByFirst);
		generatorFirst(contextByFirst, config);

		VelocityContext contextBySecond = new VelocityContext(contextParamBySecond);
		generatorSecond(contextBySecond, config);

		VelocityContext contextByThird = new VelocityContext(contextParamByThird);
		generatorThird(contextByThird, config);
	}

	private static void buildManyToManyParam(
			Configuration config,
			Map<String, Object> contextParam,
			TableEntity tableEntityByFirst,
			TableEntity tableEntityBySecond,
			TableEntity tableEntityByThird
	) {
		String manyToManyForeignKeyId = config.getString("manyToManyForeignKeyId");
		String manyToManyForeignKeyName = config.getString("manyToManyForeignKeyName");
		String manyToManyForeignKeyNameLabel = config.getString("manyToManyForeignKeyNameLabel");

		contextParam.put("classNameBySecond", tableEntityBySecond.getLowerClassName());
		contextParam.put("ClassNameBySecond", tableEntityBySecond.getUpperClassName());
		contextParam.put("classNameByFirst", tableEntityByFirst.getLowerClassName());
		contextParam.put("ClassNameByFirst", tableEntityByFirst.getUpperClassName());
		contextParam.put("classNameByThird", tableEntityByThird.getLowerClassName());
		contextParam.put("ClassNameByThird", tableEntityByThird.getUpperClassName());

		List<String> manyToManyForeignKeyNameList = StringUtil.split(manyToManyForeignKeyName, ":");
		String likeParamByFirst = manyToManyForeignKeyNameList.get(0);
		String likeParamByThird = manyToManyForeignKeyNameList.get(1);
		contextParam.put("likeParamByFirst", likeParamByFirst);
		contextParam.put("LikeParamByFirst", StrUtil.upperFirst(likeParamByFirst));
		contextParam.put("likeParamByThird", likeParamByThird);
		contextParam.put("LikeParamByThird", StrUtil.upperFirst(likeParamByThird));


		List<String> manyToManyForeignKeyIdList = StringUtil.split(manyToManyForeignKeyId, ":");
		String foreignKeyIdByFirst = manyToManyForeignKeyIdList.get(0);
		String foreignKeyIdByThird = manyToManyForeignKeyIdList.get(1);
		contextParam.put("foreignKeyIdByFirst", foreignKeyIdByFirst);
		contextParam.put("foreignKeyIdByThird", foreignKeyIdByThird);

		List<String> manyToManyForeignKeyNameLabelList = StringUtil.split(manyToManyForeignKeyNameLabel, ":");
		String foreignKeyNameByFirst = manyToManyForeignKeyNameLabelList.get(0);
		String foreignKeyNameByThird = manyToManyForeignKeyNameLabelList.get(1);
		contextParam.put("foreignKeyNameByFirst", foreignKeyNameByFirst);
		contextParam.put("foreignKeyNameByThird", foreignKeyNameByThird);
	}


	// =====================================业务 end=====================================
	// =====================================私有方法 start=====================================

	private static void generatorFirst(VelocityContext context, Configuration config) {
		StrategyContext strategyContext = new StrategyContext();

		// 生成 page
		strategyContext.setGeneratorStrategy(new GeneratorManyToManyFirstPage());
		strategyContext.executeStrategy(context, config);

		// 补充 routerConfig
		strategyContext.setGeneratorStrategy(new GeneratorRouterConfig());
		strategyContext.executeStrategy(context, config);

		// 补充 apiConfig
		strategyContext.setGeneratorStrategy(new GeneratorApiConfig());
		strategyContext.executeStrategy(context, config);

		// 生成 model
		strategyContext.setGeneratorStrategy(new GeneratorModel());
		strategyContext.executeStrategy(context, config);

		// 生成 service
		strategyContext.setGeneratorStrategy(new GeneratorFrontendManyToManyService());
		strategyContext.executeStrategy(context, config);
	}

	private static void generatorSecond(VelocityContext context, Configuration config) {
		StrategyContext strategyContext = new StrategyContext();

		// 补充 apiConfig
		strategyContext.setGeneratorStrategy(new GeneratorApiConfig());
		strategyContext.executeStrategy(context, config);

		// 生成 model
		strategyContext.setGeneratorStrategy(new GeneratorModel());
		strategyContext.executeStrategy(context, config);

		// 生成 service
		strategyContext.setGeneratorStrategy(new GeneratorFrontendManyToManyService());
		strategyContext.executeStrategy(context, config);
	}

	private static void generatorThird(VelocityContext context, Configuration config) {
		StrategyContext strategyContext = new StrategyContext();

		// 生成 page
		strategyContext.setGeneratorStrategy(new GeneratorManyToManyThirdPage());
		strategyContext.executeStrategy(context, config);

		// 补充 routerConfig
		strategyContext.setGeneratorStrategy(new GeneratorRouterConfig());
		strategyContext.executeStrategy(context, config);

		// 补充 apiConfig
		strategyContext.setGeneratorStrategy(new GeneratorApiConfig());
		strategyContext.executeStrategy(context, config);

		// 生成 model
		strategyContext.setGeneratorStrategy(new GeneratorModel());
		strategyContext.executeStrategy(context, config);

		// 生成 service
		strategyContext.setGeneratorStrategy(new GeneratorFrontendManyToManyService());
		strategyContext.executeStrategy(context, config);
	}

	private static List<ColumnEntity> frontendNewModalColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("tenant_id");
		foreachIgnoreColumns.add("parent_id");
		foreachIgnoreColumns.add("parent_ids");
		foreachIgnoreColumns.add("description");
		foreachIgnoreColumns.add("ranking");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");

		// 排除所有枚举
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (StringUtil.endsWith(columnName, "_enum") || StringUtil.startsWith(columnName, "bool_")) {
				foreachIgnoreColumns.add(columnName);
			}
		}

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> frontendSearchFormInputColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("tenant_id");
		foreachIgnoreColumns.add("ranking");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");

		// 排除所有枚举
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (StringUtil.endsWith(columnName, "_enum") || StringUtil.startsWith(columnName, "bool_")) {
				foreachIgnoreColumns.add(columnName);
			}
		}

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static Set<ColumnEntity> likeParamColumns(List<ColumnEntity> columnEntityList) {
		Set<ColumnEntity> columns = new HashSet<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "likeParam")) {
				columns.add(columnEntity);
			}

		}
		return columns;
	}

	private static List<ColumnEntity> frontendPageColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("tenant_id");
		foreachIgnoreColumns.add("ranking");
		foreachIgnoreColumns.add("state_enum");
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> frontendPageNotBoolEnumColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("state_enum");
		foreachIgnoreColumns.add("delete_enum");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName()) && StringUtil.endsWith(columnName, "_enum") && !StringUtil.startsWith(columnName, "bool_")) {
				columns.add(columnEntity);
			}
		}

		return columns;
	}

	private static List<ColumnEntity> frontendPageBoolEnumColumns(List<ColumnEntity> columnEntityList) {
		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (StringUtil.endsWith(columnName, "_enum") && StringUtil.startsWith(columnName, "bool_")) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	// =====================================私有方法 end=====================================

}
