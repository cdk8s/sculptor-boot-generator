package com.cdk8s.code.gen.strategy.frontend;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.FileUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeneratorPage implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config) {
		String template = "templates/velocity/front/pages/index.tsx.vm";
		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	public static String getFileName(Configuration config, String className) {
		String filePath = config.getString("srcPagesPath");
		return filePath + File.separator + className + File.separator + "index.tsx";
	}
}
