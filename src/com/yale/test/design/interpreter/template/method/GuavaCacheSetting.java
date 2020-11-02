package com.yale.test.design.interpreter.template.method;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/*
 * <dependency>
	  <groupId>com.google.guava</groupId>
	  <artifactId>guava</artifactId>
	  <version>28.2-jre</version>
	</dependency>
 */
public class GuavaCacheSetting extends AbstractSetting{
	//Guava Cache使用了生成器模式啊
	private Cache<String, String> cache = CacheBuilder.newBuilder().build();
	@Override
	protected String lookupCache(String key) {
		return cache.getIfPresent(key);
	}

	@Override
	protected void putIntoCache(String key, String value) {
		cache.put(key, value);
	}
}
