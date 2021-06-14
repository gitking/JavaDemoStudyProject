package com.yale.test.xml.jackson.demo1;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/*
 * Base
 * 这里为了测试集成的属性是否可以被解析，最后的结果是可以的。
 * @JacksonXmlProperty注解是标注该属性在xml中对应的节点名称，这就是为了解决某某系统接口字段命名不规范的利器。
 * 《定制 Jackson 解析器来完成对复杂格式 XML 的解析 ｜ Java Debug 笔记》https://juejin.cn/post/6961271701271216141?share_token=b126d332-8bd5-40df-9c7e-45089baca931
 * 《将海量动态数据以 JSON 格式导出 | Java Debug 笔记》https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
 * https://www.jianshu.com/p/0c2220d88924
 * Jackson 可不止 JSON
 * Jackson 可能有些人认为它只是个 JSON 库，其实不止。它除了支持 JSON 序列化以外，还支持 XML/YAML/Properties/CSV 等等，而且 Jackson 是将基础功能进行了抽象，每一种序列化都是扩展的形式存在；
 * 所以上面的增量输出 SequenceWriter，也是支持其他格式的！
 */
public class Base {
	@JacksonXmlProperty(localName="dizhi")
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}	
