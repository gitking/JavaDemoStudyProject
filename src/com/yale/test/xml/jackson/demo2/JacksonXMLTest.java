package com.yale.test.xml.jackson.demo2;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


/*
 * 概述
　　Jackson是一个强大工具，可用于Json、XML、实体之间的相互转换。此篇博客主要着重于 实体和XML之间的转换。
	XML转换核心
	　　 要想灵活地进行转换，需要在实体上使用到Jackson提供的四个注解：
	　　 @JacksonXmlElementWrapper：可用于指定List等集合类，外围标签名；
	　　 @JacksonXmlProperty：指定包装标签名，或者指定标签内部属性名；
	　　 @JacksonXmlRootElement：指定生成xml根标签的名字；
	　　 @JacksonXmlText：指定当前这个值，没有xml标签包裹。
	实例展示
	XML结构：一个班级—>一个老师(卡卡西)—>三个学生(小樱，佐助，鸣人)
	班级类：Group
	https://blog.csdn.net/u014746965/article/details/78647616
	参考资料
	Jackson官网xml文档(https://github.com/FasterXML/jackson-dataformat-xml)
	Jackson_XML注解(https://github.com/FasterXML/jackson-dataformat-xml/wiki/Jackson-XML-annotations)
 */
public class JacksonXMLTest {
	public static void main(String[] args) throws JsonProcessingException {
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setDefaultUseWrapper(false);
        //字段为null，自动忽略，不再序列化
		xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		//XML标签名:使用骆驼命名的属性名，
		xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
		//设置转换模式
		xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
		
		//序列化 bean-->xml
		Group group = new Group();//班级
		Teacher teacher = new Teacher();
		teacher.setTeacherTypeCode(new TeacherType("0","严师"));
		teacher.setName("卡卡西");
		teacher.setAge("25");
		teacher.setGender("1");
		
		Student student1 = new Student();
		student1.setId("001");
		student1.setName("鸣人");
		student1.setAge("18");
		student1.setGender("1");
		
		Student student2 = new Student();
		student2.setId("002");
		student2.setName("佐助");
		student2.setAge("18");
		student2.setGender("1");
		
		Student student3 = new Student();
		student3.setId("003");
		student3.setName("小樱");
		student3.setAge("18");
		student3.setGender("0");
		
		group.setTeacher(teacher);
		group.setStudent(Arrays.asList(student1, student2, student3));
		
		String result = xmlMapper.writeValueAsString(group);
		System.out.println("序列化结果:java对象转换成xml:" + result);
	}
}
