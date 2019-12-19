package com.cdk8s.code.gen.strategy.backend;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeneratorMapperTest implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/junit/mapper/MapperTest.java.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	private String getFileName(Configuration config, String className) {
		String testSrcJavaPath = config.getString("testSrcJavaPath");
		String packagePath = config.getString("mapperPackage");

		testSrcJavaPath = StringUtils.replace(testSrcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return testSrcJavaPath + File.separator + packagePath + File.separator + className + "MapperTest.java";
	}
}
