package com.cdk8s.code.gen.strategy;

import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

public interface GeneratorStrategy {

	void generatorFile(VelocityContext context, Configuration config);
}
