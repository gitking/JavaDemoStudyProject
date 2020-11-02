package com.yale.test.design.interpreter.template.method;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
/*
 * Redis依赖
 * <dependencies>
		<dependency>
			<groupId>io.lettuce</groupId>
			<artifactId>lettuce-core</artifactId>
			<version>5.2.1.RELEASE</version>
		</dependency>
	</dependencies>
 * https://www.cnblogs.com/throwable/p/11601538.html
 */
public class RedisSetting extends AbstractSetting {
	
	private RedisClient client = RedisClient.create("reids://localhost:6379");
	
	@Override
	protected String lookupCache(String key) {
		try(StatefulRedisConnection<String, String> connection = client.connect()) {
			RedisCommands<String, String> commands = connection.sync();
			return commands.get(key);
		}
	}

	@Override
	protected void putIntoCache(String key, String value) {
		try(StatefulRedisConnection<String, String> connection = client.connect()) {
			RedisCommands<String, String> commands = connection.sync();
			commands.set(key, value);
		}
	}
}
