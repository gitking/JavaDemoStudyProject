package com.yale.test.json;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/*
 * 有些时候，内置的解析规则和扩展的解析规则如果都不满足我们的需求，还可以自定义解析。
 * 举个例子，假设Book类的isbn是一个BigInteger：
 * public class Book {
		public String name;
		public BigInteger isbn;
	}
 * 但JSON数据并不是标准的整形格式：
 * {
	    "name": "Java核心技术",
	    "isbn": "978-7-111-54742-6"
	}
 * 直接解析，肯定报错。这时，我们需要自定义一个IsbnDeserializer，用于解析含有非数字的字符串：
 * 
 * 然后，在Book类中使用注解标注：
	public class Book {
	    public String name;
	    // 表示反序列化isbn时使用自定义的IsbnDeserializer:
	    @JsonDeserialize(using = IsbnDeserializer.class)
	    public BigInteger isbn;
	}
 * 类似的，自定义序列化时我们需要自定义一个IsbnSerializer，然后在Book类中标注@JsonSerialize(using = ...)即可。
 * 可以自定义JsonSerializer和JsonDeserializer来定制序列化和反序列化。
 */
public class IsbnDeserializer extends JsonDeserializer<BigInteger>{
	/*
	 * 问：这里面的 DeserializationContext ctxt,这个参数并未使用，传入的目的是什么？
	 * 廖雪峰答:覆写方法不能修改方法签名,在这个方法内用不到但其他情况可能会用到，比如DeserializationContext可以获取一些设置，比如遇到[1,2,3]的时候是反序列化成数组还是List
	 */
	@Override
	public BigInteger deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String s = p.getValueAsString();// 读取原始的JSON字符串内容:
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
