package com.cdk8s.code.gen.strategy.backend.bo.service;


import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorRelationTableKeyToDeleteServiceBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/bo/service/RelationTableKeyToDeleteServiceBO.java.vm";
		ColumnEntity foreignKeyFirstColumn = (ColumnEntity) context.get("foreignKeyFirstColumn");
		ColumnEntity foreignKeySecondColumn = (ColumnEntity) context.get("foreignKeySecondColumn");
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		String filePrefix = foreignKeyFirstColumn.getUpperAttrName() + "And" + foreignKeySecondColumn.getUpperAttrName();
		String fileName = getFileName(config, tableEntity.getUpperClassName(), filePrefix, "ToDeleteServiceBO");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private String getFileName(Configuration config, String className, String fileName, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("serviceBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileName + fileSuffix + ".java";
	}
}
