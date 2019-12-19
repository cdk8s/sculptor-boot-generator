package com.cdk8s.code.gen.strategy.backend;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeneratorCommonUtil {

	public static void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config, String templatePath, String fileFullPath) {
		StringWriter sw = new StringWriter();
		Template tpl = Velocity.getTemplate(templatePath, CharsetUtil.UTF_8);
		tpl.merge(context, sw);

		try {
			File file = FileUtil.createFile(fileFullPath);
			OutputStream outputStream = new FileOutputStream(file);
			IoUtil.write(outputStream, StandardCharsets.UTF_8, false, sw.toString());
			IoUtil.close(sw);
		} catch (IOException e) {
			throw new RuntimeException("生成文件失败=" + tableEntity.getTableName(), e);
		}
	}

	public static void generatorFileToOverrideContent(VelocityContext context, TableEntity tableEntity, Configuration config, String fileContent, String fileFullPath) {
		StringWriter sw = new StringWriter();
		sw.write(fileContent);

		try {
			OutputStream outputStream = new FileOutputStream(new File(fileFullPath));
			IoUtil.write(outputStream, StandardCharsets.UTF_8, false, sw.toString());
			IoUtil.close(sw);
		} catch (IOException e) {
			throw new RuntimeException("生成文件失败=" + tableEntity.getTableName(), e);
		}
	}
}
