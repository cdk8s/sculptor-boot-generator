package com.cdk8s.code.gen;


import cn.hutool.core.bean.BeanUtil;
import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.EnumEntity;
import com.cdk8s.code.gen.dto.EnumItemEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.StrategyContext;
import com.cdk8s.code.gen.strategy.backend.*;
import com.cdk8s.code.gen.strategy.backend.bo.mapper.*;
import com.cdk8s.code.gen.strategy.backend.bo.service.*;
import com.cdk8s.code.gen.strategy.backend.controller.GeneratorController;
import com.cdk8s.code.gen.strategy.backend.junit.GeneratorControllerTest;
import com.cdk8s.code.gen.strategy.backend.junit.GeneratorMapperTest;
import com.cdk8s.code.gen.strategy.backend.junit.GeneratorServiceTest;
import com.cdk8s.code.gen.strategy.backend.mapper.GeneratorMapper;
import com.cdk8s.code.gen.strategy.backend.mapper.GeneratorMapperXML;
import com.cdk8s.code.gen.strategy.backend.param.*;
import com.cdk8s.code.gen.strategy.backend.service.GeneratorService;
import com.cdk8s.code.gen.strategy.backend.sql.GeneratorPermissionSQL;
import com.cdk8s.code.gen.strategy.backend.sql.GeneratorPostgreSQL;
import com.cdk8s.code.gen.util.CollectionUtil;
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
	public static void generatorCode(Configuration config, Map<String, String> tableInfo, List<Map<String, Object>> columns) {

		Map<String, Object> contextParam = GeneratorCommonUtil.buildContextParam(config, tableInfo, columns);


		TableEntity tableEntity = (TableEntity) contextParam.get("tableEntity");
		Boolean isRelationTable = (Boolean) contextParam.get("isRelationTable");

		contextParam.put("postgreSQLColumns", postgreSQLColumns(tableEntity.getColumns()));
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
		contextParam.put("easymockResponseDTOColumns", easymockResponseDTOColumns(tableEntity.getColumns()));
		contextParam.put("initCreateBasicParamColumns", initCreateBasicParamColumns(tableEntity.getColumns()));

		List<ColumnEntity> foreignKeyColumns = foreignKeyColumns(tableEntity.getColumns());
		contextParam.put("foreignKeyColumns", foreignKeyColumns);
		if (foreignKeyColumns.size() > 1 && isRelationTable) {
			// 中间表场景
			contextParam.put("foreignKeyFirstColumn", foreignKeyColumns.get(0));
			contextParam.put("foreignKeySecondColumn", foreignKeyColumns.get(1));
		}

		Set<ColumnEntity> likeParamColumns = likeParamColumns(tableEntity.getColumns());
		contextParam.put("likeParamColumns", likeParamColumns);

		Set<ColumnEntity> oneQueryParamColumns = oneQueryParamColumns(tableEntity.getColumns());
		Set<ColumnEntity> listQueryParamColumns = listQueryParamColumns(tableEntity.getColumns());
		contextParam.put("oneQueryParamColumns", oneQueryParamColumns);
		contextParam.put("listQueryParamColumns", listQueryParamColumns);

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


		// 生成 Entity
		strategyContext.setGeneratorStrategy(new GeneratorEntity());
		strategyContext.executeStrategy(context, config);

		// 生成 Mapper
		strategyContext.setGeneratorStrategy(new GeneratorMapper());
		strategyContext.executeStrategy(context, config);

		// 生成 MapperXML
		strategyContext.setGeneratorStrategy(new GeneratorMapperXML());
		strategyContext.executeStrategy(context, config);

		// 生成 Service
		strategyContext.setGeneratorStrategy(new GeneratorService());
		strategyContext.executeStrategy(context, config);

		// 生成 cacheConfig
		strategyContext.setGeneratorStrategy(new GeneratorCacheConfig());
		strategyContext.executeStrategy(context, config);

		// 生成 Enum（可能会多个）
		if (CollectionUtil.isNotEmpty(allEnumColumns)) {
			List<EnumEntity> enumEntityList = enumClassEntityList(allEnumColumns);
			for (EnumEntity enumEntity : enumEntityList) {
				contextParam.put("enumClassEntity", enumEntity);
				VelocityContext contextToEnum = new VelocityContext(contextParam);
				strategyContext.setGeneratorStrategy(new GeneratorEnum());
				strategyContext.executeStrategy(contextToEnum, config);
			}
		}

		// 生成 basicRequestParam
		strategyContext.setGeneratorStrategy(new GeneratorBasicRequestParam());
		strategyContext.executeStrategy(context, config);

		// 生成 ServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 pageQueryMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorPageQueryMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyOneToQueryMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyOneToQueryMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToQueryMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToQueryMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToDeleteMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToDeleteMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyOneToQueryServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyOneToQueryServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToQueryServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToQueryServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToDeleteServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToDeleteServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyOneToQueryParam
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyOneToQueryParam());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToQueryParam
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToQueryParam());
		strategyContext.executeStrategy(context, config);

		// 生成 ForeignKeyListToDeleteParam
		strategyContext.setGeneratorStrategy(new GeneratorForeignKeyListToDeleteParam());
		strategyContext.executeStrategy(context, config);

		// 生成 likeParamMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorLikeQueryParamMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 likeParamServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorLikeQueryParamServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 likeParam
		strategyContext.setGeneratorStrategy(new GeneratorLikeQueryParam());
		strategyContext.executeStrategy(context, config);

		// 生成 oneParamMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorOneQueryParamMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 listParamMapperBO
		strategyContext.setGeneratorStrategy(new GeneratorListQueryParamMapperBO());
		strategyContext.executeStrategy(context, config);

		// 生成 oneParamServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorOneQueryParamServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 ListParamServiceBO
		strategyContext.setGeneratorStrategy(new GeneratorListQueryParamServiceBO());
		strategyContext.executeStrategy(context, config);

		// 生成 oneRequestParam
		strategyContext.setGeneratorStrategy(new GeneratorOneQueryParam());
		strategyContext.executeStrategy(context, config);

		// 生成 listRequestParam
		strategyContext.setGeneratorStrategy(new GeneratorListQueryParam());
		strategyContext.executeStrategy(context, config);


		// 生成 MapStruct
		strategyContext.setGeneratorStrategy(new GeneratorMapStruct());
		strategyContext.executeStrategy(context, config);

		// 生成 ResponseDTO
		strategyContext.setGeneratorStrategy(new GeneratorResponseDTO());
		strategyContext.executeStrategy(context, config);

		// 生成 Controller
		strategyContext.setGeneratorStrategy(new GeneratorController());
		strategyContext.executeStrategy(context, config);

		// 生成 MapperTest
		strategyContext.setGeneratorStrategy(new GeneratorMapperTest());
		strategyContext.executeStrategy(context, config);

		// 生成 ServiceTest
		strategyContext.setGeneratorStrategy(new GeneratorServiceTest());
		strategyContext.executeStrategy(context, config);

		// 生成 ControllerTest
		strategyContext.setGeneratorStrategy(new GeneratorControllerTest());
		strategyContext.executeStrategy(context, config);

		// 生成 PostgreSQL
		strategyContext.setGeneratorStrategy(new GeneratorPostgreSQL());
		strategyContext.executeStrategy(context, config);

		// 不是中间表的情况下
		if (!isRelationTable) {
			// 生成 PermissionSQL
			strategyContext.setGeneratorStrategy(new GeneratorPermissionSQL());
			strategyContext.executeStrategy(context, config);

			// 生成 Gatling
			strategyContext.setGeneratorStrategy(new GeneratorGatling());
			strategyContext.executeStrategy(context, config);
		}

		// 是中间表的情况下
		if (isRelationTable) {
			strategyContext.setGeneratorStrategy(new GeneratorRelationTableKeyToDeleteMapperBO());
			strategyContext.executeStrategy(context, config);

			strategyContext.setGeneratorStrategy(new GeneratorRelationTableKeyToDeleteServiceBO());
			strategyContext.executeStrategy(context, config);

			strategyContext.setGeneratorStrategy(new GeneratorRelationTableKeyToDeleteParam());
			strategyContext.executeStrategy(context, config);
		}

	}

	// =====================================业务 end=====================================
	// =====================================私有方法 start=====================================

	private static List<ColumnEntity> createRequestParamColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("tenant_id");
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
		foreachIgnoreColumns.add("tenant_id");
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
		foreachIgnoreColumns.add("tenant_id");
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

	private static List<ColumnEntity> easymockResponseDTOColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("tenant_id");
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

	private static List<ColumnEntity> initCreateBasicParamColumns(List<ColumnEntity> columnEntityList) {
		List<String> foreachIgnoreColumns = new ArrayList<>();
		foreachIgnoreColumns.add("id");
		foreachIgnoreColumns.add("tenant_id");
		foreachIgnoreColumns.add("create_date");
		foreachIgnoreColumns.add("create_user_id");
		foreachIgnoreColumns.add("update_date");
		foreachIgnoreColumns.add("update_user_id");
		foreachIgnoreColumns.add("delete_enum");
		foreachIgnoreColumns.add("delete_date");
		foreachIgnoreColumns.add("delete_user_id");
		foreachIgnoreColumns.add("ranking");
		foreachIgnoreColumns.add("state_enum");
		foreachIgnoreColumns.add("parent_id");

		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (!foreachIgnoreColumns.contains(columnEntity.getColumnName())) {
				if (StringUtil.isNotBlank(columnEntity.getColumnDefault())) {
					columns.add(columnEntity);
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

	private static Set<ColumnEntity> oneQueryParamColumns(List<ColumnEntity> columnEntityList) {
		Set<ColumnEntity> columns = new HashSet<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "oneParam")) {
				columns.add(columnEntity);
			}

		}
		return columns;
	}

	private static Set<ColumnEntity> listQueryParamColumns(List<ColumnEntity> columnEntityList) {
		Set<ColumnEntity> columns = new HashSet<>();
		for (ColumnEntity columnEntity : columnEntityList) {
			if (StringUtil.containsIgnoreCase(columnEntity.getComment(), "listParam")) {
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

	private static List<ColumnEntity> postgreSQLColumns(List<ColumnEntity> columnEntityList) {
		List<ColumnEntity> columns = new ArrayList<>();
		for (ColumnEntity columnEntity : columnEntityList) {

			String dataType = columnEntity.getDataType();
			if (StringUtil.equalsIgnoreCase(dataType, "tinyint")) {
				columnEntity.setDataType("smallint");
			}

			columns.add(columnEntity);
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
