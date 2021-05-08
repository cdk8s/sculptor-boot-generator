package com.cdk8s.code.gen.strategy.frontend;


import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.CollectionUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.util.Set;

public class GeneratorApiConfig implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");
		String frontendRootPath = config.getString("frontendRootPath");
		if (StringUtil.containsIgnoreCase(frontendRootPath, "generator-output")) {
			return;
		}

		String fileFullPath = getFilePath(config);
		String fileContent;

		try {
			fileContent = FileUtil.readFileToString(fileFullPath);
		} catch (Exception e) {
			return;
		}

		String lowerClassName = tableEntity.getLowerClassName();
		if (StringUtil.containsIgnoreCase(fileContent, "/api/" + lowerClassName + "/")) {
			// 已包含
			return;
		}

		Boolean isIncludeStateEnum = (Boolean) context.get("isIncludeStateEnum");
		Boolean isIncludeParentId = (Boolean) context.get("isIncludeParentId");
		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		Set<ColumnEntity> likeParamColumns = (Set<ColumnEntity>) context.get("likeParamColumns");

		String replaceValue = "// 必须配置项:api地址(不能修改该注释)\n" +
				"  " + lowerClassName + "PageApiUrl: `${apiServer}/api/" + lowerClassName + "/page`,\n" +
				"  " + lowerClassName + "DetailApiUrl: `${apiServer}/api/" + lowerClassName + "/detail`,\n" +
				"  " + lowerClassName + "CreateApiUrl: `${apiServer}/api/" + lowerClassName + "/create`,\n" +
				"  " + lowerClassName + "UpdateApiUrl: `${apiServer}/api/" + lowerClassName + "/update`,\n" +
				"  " + lowerClassName + "DeleteApiUrl: `${apiServer}/api/" + lowerClassName + "/batchDelete`,\n" +
				"  " + lowerClassName + "CacheEvictApiUrl: `${apiServer}/api/" + lowerClassName + "/cacheEvict`,\n";

		if (CollectionUtil.isNotEmpty(likeParamColumns)) {
			for (ColumnEntity entity : likeParamColumns) {
				replaceValue = replaceValue +
						"  " + lowerClassName + "ListBy" + entity.getUpperAttrName() + "WhereLikeApiUrl: `${apiServer}/api/" + lowerClassName + "/listBy" + entity.getUpperAttrName() + "WhereLike`,\n";
			}
		}
		if (isRelationTable) {
			replaceValue = replaceValue +
					"  " + lowerClassName + "BatchCreateApiUrl: `${apiServer}/api/" + lowerClassName + "/batchCreate`,\n";
		}
		if (isIncludeStateEnum) {
			replaceValue = replaceValue +
					"  " + lowerClassName + "UpdateStateApiUrl: `${apiServer}/api/" + lowerClassName + "/batchUpdateState`,\n";
		}
		if (isIncludeParentId) {
			replaceValue = replaceValue +
					"  " + lowerClassName + "TreeListApiUrl: `${apiServer}/api/" + lowerClassName + "/listTreeByParentId`,\n";
		}

		fileContent = StringUtil.replaceOnce(fileContent, "// 必须配置项:api地址(不能修改该注释)", replaceValue);

		GeneratorCommonUtil.generatorFileToOverrideContent(context, tableEntity, config, fileContent, fileFullPath);
	}


	private String getFilePath(Configuration config) {
		return config.getString("apiConfigPath");
	}
}
