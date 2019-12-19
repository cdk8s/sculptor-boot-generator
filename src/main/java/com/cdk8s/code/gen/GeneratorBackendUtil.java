package com.cdk8s.code.gen;


import cn.hutool.core.bean.BeanUtil;
import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.EnumEntity;
import com.cdk8s.code.gen.dto.EnumItemEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.StrategyContext;
import com.cdk8s.code.gen.strategy.backend.*;
import com.cdk8s.code.gen.util.CollectionUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import com.cdk8s.code.gen.util.id.GenerateIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.util.*;

@Slf4j
public final class GeneratorBackendUtil {

	// =====================================业务 start=====================================

	/**
	 * 生成代码
	 */
	public static void generatorCode(Configuration config, Map<String, String> table, List<Map<String, Object>> columns) {

		Map<String, Object> contextParam = GeneratorCommonUtil.buildContextParam(config, table, columns);
		TableEntity tableEntity = (TableEntity) contextParam.get("tableEntity");
		Boolean isRelationTable = (Boolean) contextParam.get("isRelationTable");

		contextParam.put("createColumnsToMapperTest", createColumnsToMapperTest(tableEntity.getColumns()));
		contextParam.put("createColumnsToService", createColumnsToService(tableEntity.getColumns()));
		contextParam.put("createRequestParamColumns", createRequestParamColumns(tableEntity.getColumns()));
		contextParam.put("createRequestServiceBOColumns", createRequestServiceBOColumns(tableEntity.getColumns(), contextParam));
		contextParam.put("updateRequestParamColumns", updateRequestParamColumns(tableEntity.getColumns()));
		contextParam.put("updateRequestServiceBOColumns", updateRequestServiceBOColumns(tableEntity.getColumns()));
		contextParam.put("updateRequestServiceBOColumnsToServiceTest", updateRequestServiceBOColumnsToServiceTest(tableEntity.getColumns()));
		contextParam.put("pageQueryParamColumns", pageQueryParamColumns(tableEntity.getColumns()));
		List<ColumnEntity> allEnumColumns = allEnumColumns(tableEntity.getColumns());
		contextParam.put("allEnumColumns", allEnumColumns);
		contextParam.put("allDateColumns", allDateColumns(tableEntity.getColumns()));
		contextParam.put("entityColumns", entityColumns(tableEntity.getColumns()));
		contextParam.put("responseDTOColumns", responseDTOColumns(tableEntity.getColumns()));
		contextParam.put("foreignKeyColumns", foreignKeyColumns(tableEntity.getColumns()));


		Set<ColumnEntity> oneParamMapperBOColumns = oneParamMapperBOColumns(tableEntity.getColumns());
		Set<ColumnEntity> listParamMapperBOColumns = listParamMapperBOColumns(tableEntity.getColumns());
		contextParam.put("oneParamMapperBOColumns", oneParamMapperBOColumns);
		contextParam.put("listParamMapperBOColumns", listParamMapperBOColumns);
		Set<ColumnEntity> paramMapperBOColumns = new HashSet<>();
		paramMapperBOColumns.addAll(oneParamMapperBOColumns);
		paramMapperBOColumns.addAll(listParamMapperBOColumns);
		contextParam.put("paramMapperBOColumns", paramMapperBOColumns);

		Long menuId = GenerateIdUtil.getId();
		contextParam.put("menuId", menuId);
		contextParam.put("createButtonId", menuId + 1L);
		contextParam.put("updateButtonId", menuId + 2L);
		contextParam.put("deleteButtonId", menuId + 3L);


		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		VelocityContext context = new VelocityContext(contextParam);
		StrategyContext strategyContext = new StrategyContext();

		boolean boolOverwriteOldFile = config.getBoolean("boolOverwriteOldFile");
		if (!boolOverwriteOldFile) {
			String fileName = GeneratorEntity.getFileName(config, tableEntity.getUpperClassName());
			Boolean flag = FileUtil.checkFile(fileName, false);
			if (flag) {
				throw new RuntimeException(tableEntity.getUpperClassName() + " 生成文件已存在，已忽略");
			}
		}


		// 生成 Entity
		strategyContext.setGeneratorStrategy(new GeneratorEntity());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 Mapper
		strategyContext.setGeneratorStrategy(new GeneratorMapper());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 MapperXML
		strategyContext.setGeneratorStrategy(new GeneratorMapperXML());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 Service
		strategyContext.setGeneratorStrategy(new GeneratorService());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 cacheConfig
		strategyContext.setGeneratorStrategy(new GeneratorCacheConfig());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 Enum（可能会多个）
		if (CollectionUtil.isNotEmpty(allEnumColumns)) {
			List<EnumEntity> enumEntityList = enumClassEntityList(allEnumColumns);
			for (EnumEntity enumEntity : enumEntityList) {
				contextParam.put("enumClassEntity", enumEntity);
				VelocityContext contextToEnum = new VelocityContext(contextParam);
				strategyContext.setGeneratorStrategy(new GeneratorEnum());
				strategyContext.executeStrategy(contextToEnum, tableEntity, config);
			}
		}

		// 生成 paramMapperBOColumns
		strategyContext.setGeneratorStrategy(new GeneratorParam());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorServiceBO());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 MapperBO
		strategyContext.setGeneratorStrategy(new GeneratorPageQueryMapperBO());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ForeignKeyMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyMapperBO());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ParamMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorParamMapperBO());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 MapStruct
		strategyContext.setGeneratorStrategy(new GeneratorMapStruct());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ResponseDTO
		strategyContext.setGeneratorStrategy(new GeneratorResponseDTO());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 Controller
		strategyContext.setGeneratorStrategy(new GeneratorController());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 MapperTest
		strategyContext.setGeneratorStrategy(new GeneratorMapperTest());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ServiceTest
		strategyContext.setGeneratorStrategy(new GeneratorServiceTest());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 生成 ControllerTest
		strategyContext.setGeneratorStrategy(new GeneratorControllerTest());
		strategyContext.executeStrategy(context, tableEntity, config);

		// 不是中间表的情况下
		if (!isRelationTable) {
			// 生成 PermissionSQL
			strategyContext.setGeneratorStrategy(new GeneratorPermissionSQL());
			strategyContext.executeStrategy(context, tableEntity, config);

			// 生成 Gatling
			strategyContext.setGeneratorStrategy(new GeneratorGatling());
			strategyContext.executeStrategy(context, tableEntity, config);
		}

	}

	// =====================================业务 end=====================================
	// =====================================私有方法 start=====================================

	private static List<ColumnEntity> createRequestParamColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("parent_ids");
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

	private static List<ColumnEntity> createRequestServiceBOColumns(List<ColumnEntity> columnEntityList, Map<String, Object> contextParam) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		if (!((Boolean) contextParam.get("isIncludeDeleteEnum"))) {
			foreachIgnoreColumns.add("delete_enum");
			foreachIgnoreColumns.add("delete_date");
			foreachIgnoreColumns.add("delete_user_id");
		}

		if (!((Boolean) contextParam.get("isIncludeCreateDate"))) {
			foreachIgnoreColumns.add("create_date");
			foreachIgnoreColumns.add("create_user_id");
		}

		if (!((Boolean) contextParam.get("isIncludeUpdateDate"))) {
			foreachIgnoreColumns.add("update_date");
			foreachIgnoreColumns.add("update_user_id");
		}

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> updateRequestParamColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");//模板已经设置了该值
		foreachIgnoreColumns.add("parent_ids");
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

	private static List<ColumnEntity> updateRequestServiceBOColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> updateRequestServiceBOColumnsToServiceTest(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
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

	private static List<ColumnEntity> pageQueryParamColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("create_date");
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

	private static List<ColumnEntity> entityColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("state_enum");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> allEnumColumns(List<ColumnEntity> columnEntityList) {
		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (StringUtil.endsWith(columnName, "_enum") || StringUtil.startsWith(columnName, "bool_")) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> allDateColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("delete_date");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName()) && StringUtil.endsWith(columnName, "_date")) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<EnumEntity> enumClassEntityList(List<ColumnEntity> allEnumColumns) {
		List<EnumEntity> enumClassEntityList = new ArrayList<>();
		for (ColumnEntity columnEntity : allEnumColumns) {
			// 过滤掉 bool 开头的，这类枚举统一用 BooleanEnum
			if (StringUtil.startsWith(columnEntity.getColumnName(), "bool_")) {
				continue;
			}
			EnumEntity enumEntity = new EnumEntity();
			enumEntity.setUpperAttrName(columnEntity.getUpperAttrName());
			String comment = columnEntity.getComment();
			String enumComment = StringUtil.substringBetween(comment, "[", "]");
			List<String> enumItemList = StringUtil.splitAndTrim(enumComment, ",");
			List<EnumItemEntity> enumItemEntityList = new ArrayList<>();
			for (String temp : enumItemList) {
				List<String> enumInfoList = StringUtil.splitAndTrim(temp, "=");
				EnumItemEntity enumItemEntity = new EnumItemEntity();
				enumItemEntity.setCode(Integer.valueOf(enumInfoList.get(0)));
				enumItemEntity.setDescription(enumInfoList.get(1));
				enumItemEntity.setCodeName(enumInfoList.get(2));
				enumItemEntityList.add(enumItemEntity);
			}
			enumEntity.setEnumItemEntityList(enumItemEntityList);
			enumClassEntityList.add(enumEntity);
		}
		return enumClassEntityList;
	}

	private static List<ColumnEntity> responseDTOColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			String columnName = columnEntity.getColumnName();
			if (!foreachIgnoreColumns.contains(columnName)) {
				if (StringUtil.containsAny(columnName, "password", "pwd")) {
					// 不允许对外展示密码，所以要过滤掉相关字段
					continue;
				}
				columns.add(columnEntity);
				if (StringUtil.endsWith(columnName, "_enum") || StringUtil.startsWith(columnName, "bool_")) {
					ColumnEntity enumStringColumn = new ColumnEntity();
					BeanUtil.copyProperties(columnEntity, enumStringColumn);
					enumStringColumn.setColumnName(columnEntity.getColumnName() + "_string");
					enumStringColumn.setUpperAttrName(columnEntity.getUpperAttrName() + "String");
					enumStringColumn.setLowerAttrName(columnEntity.getLowerAttrName() + "String");
					enumStringColumn.setAttrType("String");
					columns.add(enumStringColumn);
				}
			}
		}

		return columns;
	}

	private static List<ColumnEntity> foreignKeyColumns(List<ColumnEntity> columnEntityList) {
		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "foreignKey")) {
				columns.add(columnEntity);
			}

		}
		return columns;
	}

	private static Set<ColumnEntity> oneParamMapperBOColumns(List<ColumnEntity> columnEntityList) {
		Set<ColumnEntity> columns = new HashSet<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "oneParam")) {
				columns.add(columnEntity);
			}

		}
		return columns;
	}

	private static Set<ColumnEntity> listParamMapperBOColumns(List<ColumnEntity> columnEntityList) {
		Set<ColumnEntity> columns = new HashSet<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "listParam")) {
				columns.add(columnEntity);
			}

		}
		return columns;
	}

	private static List<ColumnEntity> createColumnsToMapperTest(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		// 不包含时
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	private static List<ColumnEntity> createColumnsToService(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("ranking");
		foreachIgnoreColumns.add("state_enum");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				columns.add(columnEntity);
			}
		}
		return columns;
	}

	// =====================================私有方法 end=====================================

}
