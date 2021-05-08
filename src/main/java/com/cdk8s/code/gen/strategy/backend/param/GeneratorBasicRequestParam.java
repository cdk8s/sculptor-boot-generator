package com.cdk8s.code.gen.strategy.backend.param;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorBasicRequestParam implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");
		createBaseRequestParam(context, tableEntity, config);
		createRequestParam(context, tableEntity, config);

		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		if (isRelationTable) {
			relationTableBatchCreateRequestParam(context, tableEntity, config);
		}

		batchCreateRequestParam(context, tableEntity, config);
		batchUpdateRequestParam(context, tableEntity, config);
		updateBaseRequestParam(context, tableEntity, config);
		updateRequestParam(context, tableEntity, config);
		pageQueryBaseParam(context, tableEntity, config);
		pageQueryParam(context, tableEntity, config);
	}

	private void createBaseRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/CreateBaseRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "CreateBaseRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void createRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/CreateRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "CreateRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchCreateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/BatchCreateRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "BatchCreateRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchUpdateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/BatchUpdateRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "BatchUpdateRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void relationTableBatchCreateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/RelationTableBatchCreateRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "BatchCreateRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateBaseRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/UpdateBaseRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "UpdateBaseRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void updateRequestParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/UpdateRequestParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "UpdateRequestParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryBaseParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/PageQueryBaseParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "PageQueryBaseParam.java";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void pageQueryParam(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/param/PageQueryParam.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "PageQueryParam.java";
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
		String packagePath = config.getString("paramPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
