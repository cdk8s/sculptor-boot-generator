package com.cdk8s.code.gen.strategy.backend.sql;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorPostgreSQL implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		String template = "templates/velocity/backend/sql/PostgreSQL.sql.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String filePath = config.getString("postgreSQLPath");
		return filePath + File.separator + className + "PostgreSQL.sql";
	}
}
