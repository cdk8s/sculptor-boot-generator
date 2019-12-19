package com.cdk8s.code.gen.strategy.frontend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

public class GeneratorApiConfig implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
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

		String upperClassName = tableEntity.getUpperClassName();
		String lowerClassName = tableEntity.getLowerClassName();
		if (StringUtil.containsIgnoreCase(fileContent, upperClassName)) {
			// 已包含
			return;
		}

		Boolean isIncludeStateEnum = (Boolean) context.get("isIncludeStateEnum");

		String replaceValue = "// 必须配置项:api地址(不能修改该注释)\n" +
				"  " + lowerClassName + "PageApiUrl: `${apiServer}/api/" + lowerClassName + "/page`,\n" +
				"  " + lowerClassName + "DetailApiUrl: `${apiServer}/api/" + lowerClassName + "/detail`,\n" +
				"  " + lowerClassName + "CreateApiUrl: `${apiServer}/api/" + lowerClassName + "/create`,\n" +
				"  " + lowerClassName + "UpdateApiUrl: `${apiServer}/api/" + lowerClassName + "/update`,\n" +
				"  " + lowerClassName + "DeleteApiUrl: `${apiServer}/api/" + lowerClassName + "/batchDelete`,\n" +
				"  " + lowerClassName + "CacheEvictApiUrl: `${apiServer}/api/" + lowerClassName + "/cacheEvict`,\n";
		if (isIncludeStateEnum) {
			replaceValue = replaceValue +
					"  " + lowerClassName + "UpdateStateApiUrl: `${apiServer}/api/" + lowerClassName + "/batchUpdateState`,\n";
		}

		fileContent = StringUtil.replaceOnce(fileContent, "// 必须配置项:api地址(不能修改该注释)", replaceValue);

		GeneratorCommonUtil.generatorFileToOverrideContent(context, tableEntity, config, fileContent, fileFullPath);
	}


	private String getFilePath(Configuration config) {
		return config.getString("apiConfigPath");
	}
}
