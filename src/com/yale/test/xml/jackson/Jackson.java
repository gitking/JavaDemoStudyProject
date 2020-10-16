package com.yale.test.xml.jackson;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/*
 * 使用Jackson
 * 前面我们介绍了DOM和SAX两种解析XML的标准接口。但是，无论是DOM还是SAX，使用起来都不直观。
 * 观察XML文档的结构：
 * ?xml version="1.0" encoding="UTF-8" ?>
	<book id="1">
	    <name>Java核心技术</name>
	    <author>Cay S. Horstmann</author>
	    <isbn lang="CN">1234567</isbn>
	    <tags>
	        <tag>Java</tag>
	        <tag>Network</tag>
	    </tags>
	    <pubDate/>
	</book>
 * 我们发现，它完全可以对应到一个定义好的JavaBean中：
 * 如果能直接从XML文档解析成一个JavaBean，那比DOM或者SAX不知道容易到哪里去了。
 * 幸运的是，一个名叫Jackson的开源的第三方库可以轻松做到XML到JavaBean的转换。我们要使用Jackson，先添加两个Maven的依赖：
 * com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.10.1
 * org.codehaus.woodstox:woodstox-core-asl:4.4.1
 * stax2-api-4.2.jar
 * jackson-module-jaxb-annotations-2.11.2.jar
 * 然后，定义好JavaBean，就可以用下面几行代码解析：
 * 注意到XmlMapper就是我们需要创建的核心对象，可以用readValue(InputStream, Class)直接读取XML并返回一个JavaBean。运行上述代码，就可以直接从Book对象中拿到数据：
 * 如果要解析的数据格式不是Jackson内置的标准格式，那么需要编写一点额外的扩展来告诉Jackson如何自定义解析。这里我们不做深入讨论，可以参考Jackson的官方文档（https://github.com/FasterXML/jackson）。
 * 小结
 * 使用Jackson解析XML，可以直接把XML解析为JavaBean，十分方便。
 * @JacksonXmlProperty注解是标注该属性在xml中对应的节点名称，这就是为了解决某某系统接口字段命名不规范的利器。
 * @JacksonXmlProperty(localName = "dizhi")
    private String address;
    @JacksonXmlRootElement 这个注解是指名根节点，见名知意不用多说。
    @JacksonXmlRootElement(localName = "student")
	public class Student<T> extends Base{
		重点是@JacksonXmlElementWrapper将某节点下单元素解析为Array或者Collection类型，因为除了公共字段外，其他字段都可能是变化的所以在集合哪儿使用了泛型。
		String xml = "<?xml version=\"1.0\" encoding=\"gb2312\" standalone=\"yes\"?><student><dizhi>北京三里屯</dizhi><studentName>张三</studentName><nainling>25</nainling><dh>18858863333</dh><rs><array><acc01>89.50</acc01><acc02>100.00</acc02><acc03>91.00</acc03></array><array><acc01>78.50</acc01><acc02>69.00</acc02><acc03>88.90</acc03></array></rs></student>";
			链接：https://www.jianshu.com/p/0c2220d88924
		@JacksonXmlProperty(localName = "array")
	    @JacksonXmlElementWrapper(localName = "rs")
	    private List<T> grades;
	}
	要想灵活地进行转换，需要在实体上使用到Jackson提供的四个注解(https://github.com/FasterXML/jackson-dataformat-xml/wiki/Jackson-XML-annotations)：
　　 @JacksonXmlElementWrapper：可用于指定List等集合类，外围标签名；
　　 @JacksonXmlProperty：指定包装标签名，或者指定标签内部属性名；
　　 @JacksonXmlRootElement：指定生成xml根标签的名字；
　　 @JacksonXmlText：指定当前这个值，没有xml标签包裹。
         参考资料:
   Jackson官网xml文档(https://github.com/FasterXML/jackson-dataformat-xml)
   Jackson_XML注解(https://github.com/FasterXML/jackson-dataformat-xml/wiki/Jackson-XML-annotations)
   https://blog.csdn.net/u014746965/article/details/78647616
 */
public class Jackson {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		InputStream input = Jackson.class.getResourceAsStream("/jcakbook.xml");
		JacksonXmlModule module = new JacksonXmlModule();
		// to default to using "unwrapped" Lists:
		//module.setDefaultUseWrapper(false);
		XmlMapper mapper = new XmlMapper(module);
		//自动忽略无法对应pojo的字段
		//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		//字段为null，自动忽略，不再序列化
		//mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		//XML标签名:使用骆驼命名的属性名，  
		//mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE); 
        
		//设置转换模式 
		//mapper.enable(MapperFeature.USE_STD_BEAN_NAMING); 
        
		Book book = mapper.readValue(input, Book.class);
		
		System.out.println(book.id);
		System.out.println(book.name);
		System.out.println(book.author);
		System.out.println(book.isbn);
		System.out.println("节点isbn的值为:" + book.isbn.value);
		System.out.println("节点isbn的属性lang的值为:" + book.isbn.lang);
		System.out.println(book.tags);
		System.out.println(book.pubDate);
	}
}
