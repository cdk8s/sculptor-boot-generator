package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorPermissionSQL implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/PermissionSQL.sql.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String filePath = config.getString("permissionSQLPath");
		return filePath + File.separator + className + "PermissionSQL.sql";
	}
}
