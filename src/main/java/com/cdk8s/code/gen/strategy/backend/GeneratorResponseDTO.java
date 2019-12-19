package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorResponseDTO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/ResponseDTO.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("responsePackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + "ResponseDTO.java";
	}
}
