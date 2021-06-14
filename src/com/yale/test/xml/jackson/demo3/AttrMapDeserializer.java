package com.yale.test.xml.jackson.demo3;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/*
 * 定制 Jackson 解析器来完成对复杂格式 XML 的解析 ｜ Java Debug 笔记
 * https://juejin.cn/post/6961271701271216141?share_token=b126d332-8bd5-40df-9c7e-45089baca931
 * 然后是自定义的类型解析器 - AttrMapDeserializer，在这个解析器里将报文和 AttrMap 映射
 */
public class AttrMapDeserializer extends JsonDeserializer<AttrMap<String, String>>{
	@Override
	public AttrMap<String, String> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonToken curToken;
		AttrMap<String, String> attrMap = new AttrMap<>();
		while ((curToken = p.nextToken()) != null && curToken.id() == JsonTokenId.ID_FIELD_NAME) {
			//skip start token 跳过开始的节点
			p.nextToken();
			String key = null, value = null;
			while ((curToken = p.nextToken()) != null && curToken.id() == JsonTokenId.ID_FIELD_NAME) {
				String attrName = p.getCurrentName();
				String attrValue= p.nextTextValue();
				if ("key".equals(attrName)) {
					key = attrValue;
				}
				//处理<attr key="any" value="any"/> 和 <attr key="any">132</attr>俩种形式
				if ("value".equals(attrName) || "".equals(attrName)) {
					value = attrValue;
				}
			}
			attrMap.put(key, value);
		}
		return attrMap;
	}
}
