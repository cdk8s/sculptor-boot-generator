package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorParam implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		createRequestParam(context, tableEntity, config);

		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		if (isRelationTable) {
			batchCreateRequestParam(context, tableEntity, config);
		}

		updateRequestParam(context, tableEntity, config);
		pageQueryParam(context, tableEntity, config);
	}

	private void createRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/param/CreateRequestParam.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "CreateRequestParam");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchCreateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/param/BatchCreateRequestParam.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "BatchCreateRequestParam");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/param/UpdateRequestParam.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "UpdateRequestParam");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/param/PageQueryParam.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "PageQueryParam");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className, String fileSuffix) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("paramPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileSuffix + ".java";
	}
}
