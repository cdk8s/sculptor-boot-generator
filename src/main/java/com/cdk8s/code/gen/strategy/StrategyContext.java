package com.cdk8s.code.gen.strategy;

import org.apache.commons.configuration.Configuration;
import org.apache.velocity.VelocityContext;

public class StrategyContext {

	private GeneratorStrategy generatorStrategy;

	public StrategyContext() {

	}

	public void setGeneratorStrategy(GeneratorStrategy generatorStrategy) {
		this.generatorStrategy = generatorStrategy;
	}

	public void executeStrategy(VelocityContext context, Configuration config) {
		generatorStrategy.generatorFile(context, config);
	}


}
