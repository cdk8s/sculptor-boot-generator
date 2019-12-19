package com.cdk8s.code.gen.strategy.frontend;


import cn.hutool.core.io.IoUtil;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeneratorRouterConfig implements GeneratorStrategy {

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

		String replaceValue = "// 必须配置项:页面路由(不能修改该注释)\n" +
				"            { path: '/" + lowerClassName + "', component: './" + upperClassName + "/index.tsx' },";

		fileContent = StringUtil.replaceOnce(fileContent, "// 必须配置项:页面路由(不能修改该注释)", replaceValue);

		GeneratorCommonUtil.generatorFileToOverrideContent(context, tableEntity, config, fileContent, fileFullPath);
	}


	private String getFilePath(Configuration config) {
		return config.getString("routerConfigPath");
	}
}
