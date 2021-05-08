package com.cdk8s.code.gen.strategy.backend.bo.mapper;


import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.List;

public class GeneratorForeignKeyListToDeleteMapperBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/bo/mapper/ForeignKeyListToDeleteMapperBO.java.vm";

		Object foreignKeyColumns = context.get("foreignKeyColumns");
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		if (null != foreignKeyColumns) {
			List<ColumnEntity> columnEntityList = (List<ColumnEntity>) foreignKeyColumns;
			for (ColumnEntity entity : columnEntityList) {
				context.put("foreignKeyColumn", entity.getLowerAttrName());
				context.put("ForeignKeyColumn", tableEntity.getUpperClassName() + entity.getUpperAttrName());
				context.put("attrType", entity.getAttrType());
				String fileName = getFileName(config, tableEntity.getUpperClassName(), entity.getUpperAttrName(), "ListToDeleteMapperBO");
				GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
			}
		}
	}

	private String getFileName(Configuration config, String className, String fileName, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("mapperBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileName + fileSuffix + ".java";
	}
}
