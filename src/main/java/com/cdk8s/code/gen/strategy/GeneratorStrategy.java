package com.cdk8s.code.gen.strategy;

import com.cdk8s.code.gen.dto.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

public interface GeneratorStrategy {

	void generatorFile(VelocityContext context, TableEntity tableEntity, Configuration config);
}
