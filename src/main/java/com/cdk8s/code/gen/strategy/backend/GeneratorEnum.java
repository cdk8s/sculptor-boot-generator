package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.EnumEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorEnum implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/Enum.java.vm";
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");
		EnumEntity enumEntity = (EnumEntity) context.get("enumClassEntity");

		String fileName = getFileName(config, enumEntity.getUpperAttrName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String srcJavaPath = config.getString("srcEnumJavaPath");
		String packagePath = config.getString("enumPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + className + ".java";
	}
}
