package com.cdk8s.code.gen.strategy.backend.mapper;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorMapperXML implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		genMapperXML(context, tableEntity, config);
		genMapperXMLExt(context, tableEntity, config);
	}

	private void genMapperXML(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/mapper/Mapper.xml.vm";
		String fileSuffix = tableEntity.getUpperClassName() + "Mapper.xml";
		String fileName = getFileName(config, fileSuffix);
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private void genMapperXMLExt(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/backend/mapper/MapperExt.xml.vm";
		String fileSuffix = "ext" + File.separator + tableEntity.getUpperClassName() + "MapperExt.xml";
		String fileName = getFileName(config, fileSuffix);
		Boolean flag = FileUtil.checkFile(fileName, false);
		if (flag) {
			// 自定义文件已存在，则不覆盖
			return;
		}
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}

	private String getFileName(Configuration config, String fileSuffix) {
		String packagePath = config.getString("resourceMapperXmlPath");

		packagePath = StringUtils.replace(packagePath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return packagePath + File.separator + fileSuffix;
	}
}
