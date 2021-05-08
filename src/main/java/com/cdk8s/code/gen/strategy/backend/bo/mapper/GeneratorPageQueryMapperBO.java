package com.cdk8s.code.gen.strategy.backend.bo.mapper;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorPageQueryMapperBO implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		genMapperBOBase(context, tableEntity, config);
		genMapperBO(context, tableEntity, config);
	}

	private void genMapperBOBase(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/mapper/PageQueryBaseMapperBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + "bases" + File.separator + tableEntity.getUpperClassName() + "PageQueryBaseMapperBO.java";
		String fileName = getFileName(config, fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void genMapperBO(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/bo/mapper/PageQueryMapperBO.java.vm";
		String fileSuffix = StringUtil.lowerCase(tableEntity.getUpperClassName()) + File.separator + tableEntity.getUpperClassName() + "PageQueryMapperBO.java";
		String fileName = getFileName(config, fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String fileSuffix) {
		String srcJavaPath = config.getString("pojoJavaPath");
		String packagePath = config.getString("mapperBOPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + fileSuffix;
	}
}
