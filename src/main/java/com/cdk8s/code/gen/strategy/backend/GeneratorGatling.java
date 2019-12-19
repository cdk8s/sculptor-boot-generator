package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorGatling implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		page(context, tableEntity, config);
		create(context, tableEntity, config);
		batchDelete(context, tableEntity, config);
	}

	private void page(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/gatling/Page.scala.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "Page");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void create(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/gatling/Create.scala.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "Create");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void batchDelete(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/gatling/BatchDelete.scala.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName(), "BatchDelete");
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private String getFileName(Configuration config, String className, String fileSuffix) {
		String filePath = config.getString("gatlingPath");
		return filePath + File.separator + className + fileSuffix + ".scala";
	}
}
