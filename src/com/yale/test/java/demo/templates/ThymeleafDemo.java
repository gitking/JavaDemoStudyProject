package com.yale.test.java.demo.templates;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

/**
 * https://www.thymeleaf.org/
 * https://blog.csdn.net/weishaoqi2/article/details/102558339 《Spring Boot 2.x整合模版引擎(1)-Thymeleaf的XML模式，自定义方言属性》
 * 我自己写的《Spring.docx》文档里面也有相关的thymeleaf的资料
 * thymeleaf-3.0.14.RELEASE.jar
 * attoparser-2.0.5.RELEASE.jar
 * log4j-1.2.17.jar
 * unbescape-1.1.6.RELEASE.jar
 * <dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-thymeleaf</artifactId>
	</dependency>
 * 参考encrypt-machine项目
 * https://www.felord.cn/_doc/_springboot/2.1.5.RELEASE/_book/pages/spring-boot-features.html#boot-features-spring-mvc-template-engines
 * SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
 * SLF4J: Defaulting to no-operation (NOP) logger implementation
 * SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
 * 
 * thymeleaf th:text 中文  被转义了，我擦
 * 回家看阿里云尚硅谷 thymeleaf的视频，或者慕课网上面的thymeleaf相关视频
 * th:text 为什么会乱码？在知乎和perfma上面提问
 * 很有可能是th:text一开始就设计为html用的,所以她编译成html entities编码了
 * 所以那到底thmeythef到底是怎么解析XML文件的？
 * 把thmeythel的源码下载下来，搜一下是在哪里转换成html entities编码的，th:text就是给html用的？
 * thymeleaf 怎么给xml 怎么添加一个中文属性？？
 * 下一个简单的themywy demo 给老外提issues,让他解决
 * 问波波thymeleaf 的事情
 * 晚上看尚硅谷thmeleaf的视频，把他项目启动起来，看下SpringBoot返回给前端的是不是html 编码之后的东西
 * @author issuser
 */
public class ThymeleafDemo {
	
	final TemplateEngine templateEngine = new TemplateEngine();

	public static void main(String[] args) {
		ThymeleafDemo  et = new ThymeleafDemo();
		System.out.println("ssssssssss" + et.getClass().getClassLoader().getSystemResource(".").toString());
		System.out.println("ssssssssss" + et.getClass().getClassLoader().getResource(".").toString());
		File file = null;
		try {
			file = new File(et.getClass().getClassLoader().getResource(".").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setTemplateMode("XML");
		templateResolver.setCharacterEncoding("utf-8");
		System.out.println(TemplateMode.XML);
//		templateResolver.setPrefix("classpath:/templates/");
//		templateResolver.setOrder(null);
		templateResolver.setPrefix(file.getPath() + File.separator);
		templateResolver.setSuffix(".xml");
//		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		Context ctx = new Context();
		ctx.setLocale(Locale.CHINA);
		ctx.setLocale(Locale.CHINESE);
		ctx.setVariable("errorMsg", "抱歉，暂时不只支持该加密类型或者您的加密类型不存在.");
		System.out.println(ctx.getVariable("errorMsg"));
		et.templateEngine.setTemplateResolver(templateResolver);
		//et.templateEngine.addDialect();
		final String result = et.templateEngine.process("EncryptResponseErrorXMl", ctx);
		System.out.println("ss" + result);
	}
}
