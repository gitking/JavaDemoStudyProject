package com.yale.test.xml.jackson.demo3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/*
 * https://juejin.cn/post/6961271701271216141?share_token=b126d332-8bd5-40df-9c7e-45089baca931
 * 定制 Jackson 解析器来完成对复杂格式 XML 的解析 ｜ Java Debug 笔记
 * 使用Jackson解析下面这个XML字符串并将这个XML与一个实体类映射:
 * <Root> 
	  <extendInfos> 
	    <extendInfo key="birthday" value="19870101"/>  
	    <extendInfo key="address" value="北京"/>  
	    <extendInfo key="gender" value="M"/>  
	    <extendInfo key="userName" value="周关"/> 
	  </extendInfos> 
	</Root>
 * 像上面这种复杂一点的报文，也是有解决方案的，只是有点不太优雅。比如我可以创建一个 ExtendInfo 类，定义 key/value 两个属性，然后再将 extendInfos 定义为一个 List，也可以完成解析：
 * public class Root {
	    private List<ExtendInfo> extendInfos;
	    
	    // getter and setters....
	}
 * 可是这个数据格式很明显是个键值对格式，弄个 List 来存储，是不是有点太傻了？要是能用 Map 来接收 extendInfos 的数据该多好……
 * Jackson 是个功能非常强大的序列化库，除了支持 JSON 以外，还支持很多其他格式，比如 XML。而且 Jackson 还可以自定义对解析器的增强，通过对 JsonDeserializer 接口的扩展，可以完成更复杂数据的解析：
 * 基于 Jackson，可以定制化一下解析器，来完成上面复杂数据的解析，将 extendInfos 解析为一个 Map，方便程序的处理
 * 先定义一个 AttrMap ，用来标记我们这个特殊的数据类型，直接继承 HashMap 就好：
 */
public class JacksonXmlTest {
	public static void main(String[] args) {
		String body = "<Root> \n" +
		        "  <extendInfos> \n" +
		        "    <extendInfo key=\"birthday\" value=\"19870101\"/>  \n" +
		        "    <extendInfo key=\"address\" value=\"北京\"/>  \n" +
		        "    <extendInfo key=\"gender\" value=\"M\"/>  \n" +
		        "    <extendInfo key=\"userName\" value=\"周关\"/> \n" +
		        "  </extendInfos> \n" +
		        "</Root>";
		JacksonXmlModule jsonXmlModule = new JacksonXmlModule();
		jsonXmlModule.addDeserializer(AttrMap.class, new AttrMapDeserializer());
		
		ObjectMapper objectMapper = new XmlMapper(jsonXmlModule);
		
		Root root;
		try {
			root = objectMapper.readValue(body, Root.class);
			System.out.println("使用Jackson将复杂的xml转换为Java对象:" + root);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
