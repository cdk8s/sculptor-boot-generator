package com.cdk8s.code.gen.strategy.backend.service;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorService implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		genServiceBase(context, tableEntity, config);
		genService(context, tableEntity, config);
	}

	private void genServiceBase(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/service/ServiceBase.java.vm";
		String fileSuffix = "bases" + File.separator + tableEntity.getUpperClassName() + "ServiceBase.java";
		String fileName = getFileName(config, fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void genService(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/service/Service.java.vm";
		String fileSuffix = tableEntity.getUpperClassName() + "Service.java";
		String fileName = getFileName(config, fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private String getFileName(Configuration config, String fileSuffix) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("servicePackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
