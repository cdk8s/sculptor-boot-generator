package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorEntity implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		genEntityBase(context, tableEntity, config);
		genEntity(context, tableEntity, config);
	}

	private void genEntityBase(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/entity/EntityBase.java.vm";
		String fileSuffix = "bases" + File.separator + tableEntity.getUpperClassName() + "Base.java";
		String fileName = getFileName(config, fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void genEntity(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/entity/Entity.java.vm";
		String fileSuffix = tableEntity.getUpperClassName() + ".java";
		String fileName = getFileName(config, fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	public static String getFileName(Configuration config, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("entityPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
