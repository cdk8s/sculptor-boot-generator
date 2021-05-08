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

public class GeneratorListQueryParam implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		String template = "templates/velocity/backend/param/ListQueryParam.java.vm";
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		Object listQueryParamColumns = context.get("listQueryParamColumns");
		if (null != listQueryParamColumns) {
			Set<ColumnEntity> columnEntityList = (Set<ColumnEntity>) listQueryParamColumns;
			for (ColumnEntity entity : columnEntityList) {
				context.put("paramColumn", entity.getLowerAttrName());
				context.put("ParamColumn", tableEntity.getUpperClassName() + entity.getUpperAttrName());
				context.put("attrType", entity.getAttrType());
				String fileName = getFileName(config, tableEntity.getUpperClassName(), entity.getUpperAttrName(), "ListRequestParam");
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
