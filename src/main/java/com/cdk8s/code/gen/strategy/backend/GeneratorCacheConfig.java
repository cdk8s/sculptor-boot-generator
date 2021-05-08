package com.cdk8s.code.gen.strategy.backend;


import com.cdk8s.code.gen.dto.TableEntity;
import com.cdk8s.code.gen.strategy.GeneratorStrategy;
import com.cdk8s.code.gen.util.FileUtil;
import com.cdk8s.code.gen.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;

public class GeneratorCacheConfig implements GeneratorStrategy {

	@Override
	public void generatorFile(VelocityContext context, Configuration config) {
		TableEntity tableEntity = (TableEntity) context.get("tableEntity");
		String backendRootPath = config.getString("backendRootPath");
		Boolean boolEnableCache = config.getBoolean("boolEnableCache");

		if (!boolEnableCache) {
			return;
		}

		Boolean isRelationTable = (Boolean) context.get("isRelationTable");
		if (isRelationTable) {
			// 中间表不加缓存
			return;
		}

		if (StringUtil.containsIgnoreCase(backendRootPath, "generator-output")) {
			return;
		}

		String fileFullPath = getFilePath(config);
		String fileContent;

		try {
			fileContent = FileUtil.readFileToString(fileFullPath);
		} catch (Exception e) {
			throw new RuntimeException("找不到缓存配置文件");
		}

		String classServiceName = tableEntity.getUpperClassName() + "Service";
		if (StringUtil.containsIgnoreCase(fileContent, classServiceName)) {
			// 已包含
			return;
		}

		String replaceValue = "// 必须配置项:RedisCacheConfiguration(不能修改该注释)\n" +
				"\t\tRedisCacheConfiguration " + classServiceName + " = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)).serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer())).disableCachingNullValues().prefixKeysWith(\"SPRING_CACHE:" + classServiceName + ":\");";

		fileContent = StringUtil.replaceOnce(fileContent, "// 必须配置项:RedisCacheConfiguration(不能修改该注释)", replaceValue);


		String replaceValue2 = "// 必须配置项:cacheNamesMap(不能修改该注释)\n" +
				"\t\t\t\tput(\"" + classServiceName + "\", " + classServiceName + ");";

		fileContent = StringUtil.replaceOnce(fileContent, "// 必须配置项:cacheNamesMap(不能修改该注释)", replaceValue2);

		GeneratorCommonUtil.generatorFileToOverrideContent(context, tableEntity, config, fileContent, fileFullPath);
	}


	private String getFilePath(Configuration config) {
		String srcJavaPath = config.getString("srcJavaPath");
		String packagePath = config.getString("cacheConfigPackage");

		srcJavaPath = StringUtils.replace(srcJavaPath, "/", File.separator);
		packagePath = StringUtils.replace(packagePath, ".", File.separator);
		return srcJavaPath + File.separator + packagePath + File.separator + "RedisCacheConfig.java";
	}
}
