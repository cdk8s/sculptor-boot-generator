package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorServiceBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		createRequestServiceBO(context, tableEntity, config);

		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		if (isRelationTable) {
			batchCreateRequestServiceBO(context, tableEntity, config);
		}

		updateRequestService(context, tableEntity, config);
		pageQueryServiceBO(context, tableEntity, config);
	}

	private void createRequestServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/bo/service/CreateServiceBO.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "CreateServiceBO");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchCreateRequestServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/bo/service/BatchCreateServiceBO.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "BatchCreateServiceBO");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateRequestService(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/bo/service/UpdateServiceBO.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "UpdateServiceBO");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/bo/service/PageQueryServiceBO.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "PageQueryServiceBO");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className, String fileSuffix) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("serviceBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + StringUtil.lowerCase(className) + File.separator + className + fileSuffix + ".java";
	}
}
