package com.cdk8s.code.gen.strategy.backend.param;


import com.cdk8s.code.gen.dto.ColumnEntity;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.Set;

public class GeneratorOneQueryParam implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/param/OneQueryParam.java.vm";
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		Object oneQueryParamColumns = context.get("oneQueryParamColumns");
		if (null != oneQueryParamColumns) {
			Set<ColumnEntity> columnEntityList = (Set<ColumnEntity>) oneQueryParamColumns;
			for (ColumnEntity entity : columnEntityList) {
				context.put("paramColumn", entity.getLowerAttrName());
				context.put("ParamColumn", tableEntity.getUpperClassName() + entity.getUpperAttrName());
				context.put("attrType", entity.getAttrType());
				context.put("maxValue", entity.getMaxValue());
				context.put("characterMaximumLength", entity.getCharacterMaximumLength());
				context.put("dataType", entity.getDataType());
				context.put("shortComment", entity.getShortComment());
				context.put("boolIsNullable", entity.getBoolIsNullable());
				String fileName = getFileName(config, tableEntity.getUpperClassName(), entity.getUpperAttrName(), "RequestParam");
				GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
			}
		}
	}

	private String getFileName(Configuration config, String className, String fileName, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("paramPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileName + fileSuffix + ".java";
	}
}
