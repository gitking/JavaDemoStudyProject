package com.yale.test.json;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class IsbnDeserializer extends JsonDeserializer<BigInteger>{

	/*
	 * 问：这里面的 DeserializationContext ctxt,这个参数并未使用，传入的目的是什么？
	 * 廖雪峰答:覆写方法不能修改方法签名,在这个方法内用不到但其他情况可能会用到，比如DeserializationContext可以获取一些设置，比如遇到[1,2,3]的时候是反序列化成数组还是List
	 */
	@Override
	public BigInteger deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String s = p.getValueAsString();
		if (s != null) {
			try {
				return new BigInteger(s.replace("-", ""));
			} catch (NumberFormatException e) {
				throw new JsonParseException(p, s, e);
			}
		}
		return null;
	}
}
