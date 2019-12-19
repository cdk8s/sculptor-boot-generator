package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.List;
import java.util.Set;

public class GeneratorParamMapperBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/bo/mapper/ParamMapperBO.java.vm";

		Object paramMapperBOColumns = context.get("paramMapperBOColumns");
		if (null != paramMapperBOColumns) {
			Set<ColumnEntity> columnEntityList = (Set<ColumnEntity>) paramMapperBOColumns;
			for (ColumnEntity entity : columnEntityList) {
				context.put("paramColumn", entity.getLowerAttrName());
				context.put("ParamColumn", tableEntity.getUpperClassName() + entity.getUpperAttrName());
				context.put("attrType", entity.getAttrType());
				String fileName = getFileName(config, tableEntity.getUpperClassName(), entity.getUpperAttrName(), "MapperBO");
				GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
			}
		}
	}

	private String getFileName(Configuration config, String className, String fileName, String fileSuffix) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("mapperBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileName + fileSuffix + ".java";
	}
}
