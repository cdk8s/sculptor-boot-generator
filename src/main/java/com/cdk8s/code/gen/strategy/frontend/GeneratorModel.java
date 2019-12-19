package com.cdk8s.code.gen.strategy.frontend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorModel implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/front/models/Model.ts.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String filePath = config.getString("srcModelsPath");
		return filePath + File.separator + className + "Model.ts";
	}
}
