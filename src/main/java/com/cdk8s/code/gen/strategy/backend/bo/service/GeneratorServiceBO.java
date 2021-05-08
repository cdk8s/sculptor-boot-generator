package com.cdk8s.code.gen.strategy.backend.bo.service;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorServiceBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");
		createRequestBaseServiceBO(context, tableEntity, config);
		createRequestServiceBO(context, tableEntity, config);

		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		if (isRelationTable) {
			batchCreateRequestServiceBO(context, tableEntity, config);
		}

		updateRequestBaseServiceBO(context, tableEntity, config);
		updateRequestServiceBO(context, tableEntity, config);
		pageQueryBaseServiceBO(context, tableEntity, config);
		pageQueryServiceBO(context, tableEntity, config);
	}

	private void createRequestBaseServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/CreateBaseServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "CreateBaseServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void createRequestServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/CreateServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "CreateServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchCreateRequestServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/BatchCreateServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "BatchCreateServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateRequestBaseServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/UpdateBaseServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "UpdateBaseServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateRequestServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/UpdateServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "UpdateServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryBaseServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/PageQueryBaseServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "PageQueryBaseServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryServiceBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/service/PageQueryServiceBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "PageQueryServiceBO.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("serviceBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
