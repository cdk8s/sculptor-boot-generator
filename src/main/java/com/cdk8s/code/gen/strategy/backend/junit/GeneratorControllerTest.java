package com.cdk8s.code.gen.strategy.backend.junit;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorControllerTest implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/junit/controller/ControllerTest.java.vm";
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String testSrcJavaPath = config.getString("testSrcJavaPath");
		String packagePath = config.getString("controllerPackage");

		testSrcJavaPath = StringUtils.replace(testSrcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return testSrcJavaPath + File.separator + packagePath + File.separator + className + "ControllerTest.java";
	}
}
