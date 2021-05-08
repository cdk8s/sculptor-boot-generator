package com.cdk8s.code.gen.strategy.frontend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.strategy.backend.GeneratorCommonUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorManyToManyThirdPage implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");

		if (StringUtil.startsWith(tableEntity.getTableName(), "rel_")) {
			// 中间表不需要生成
			return;
		}

		String template = "templates/velocity/front/manyToMany/indexByThird.tsx.vm";

		String fileName = getFileName(config, tableEntity.getUpperClassName());
		GeneratorCommonUtil.generatorFile(context, tableEntity, config, template, fileName);
	}


	public static String getFileName(Configuration config, String className) {
		String filePath = config.getString("srcPagesPath");
		return filePath + File.separator + className + File.separator + "index.tsx";
	}
}
