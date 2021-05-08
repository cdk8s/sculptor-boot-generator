package com.cdk8s.code.gen.strategy.backend.controller;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorController implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		genControllerBase(context, tableEntity, config);
		genController(context, tableEntity, config);
	}

	private void genControllerBase(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/controller/ControllerBase.java.vm";
		String fileSuffix = "bases" + File.separator + tableEntity.getUpperClassName() + "ControllerBase.java";
		String fileName = getFileName(config, fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void genController(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/controller/Controller.java.vm";
		String fileSuffix = tableEntity.getUpperClassName() + "Controller.java";
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
		String packagePath = config.getString("controllerPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
