package com.yale.test.spring.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/*
 * 使用Resource
 * 在Java程序中，我们经常会读取配置文件、资源文件等。使用Spring容器时，我们也可以把“文件”注入进来，方便程序读取。
 * 例如，AppService需要读取logo.txt这个文件，通常情况下，我们需要写很多繁琐的代码，主要是为了定位文件，打开InputStream。
 * Spring提供了一个org.springframework.core.io.Resource（注意不是javax.annotation.Resource），它可以像String、int一样使用@Value注入：
 * 注入Resource最常用的方式是通过classpath，即类似classpath:/logo.txt表示在classpath中搜索logo.txt文件，然后，我们直接调用Resource.getInputStream()就可以获取到输入流，避免了自己搜索文件的代码。
 * 也可以直接指定文件的路径，例如：
 * @Value("file:/path/to/logo.txt")
 * private Resource resource;
 * 但使用classpath是最简单的方式。上述工程结构如下：
 * spring-ioc-resource
	├── pom.xml
	└── src
	    └── main
	        ├── java
	        │   └── com
	        │       └── itranswarp
	        │           └── learnjava
	        │               ├── AppConfig.java
	        │               └── AppService.java
	        └── resources
	            └── logo.txt
 * 使用Maven的标准目录结构，所有资源文件放入src/main/resources即可。
 * 练习
 * 使用Spring的Resource注入app.properties文件，然后读取该配置文件。
 * 小结
 * Spring提供了Resource类便于注入资源文件。
 * 最常用的注入是通过classpath以classpath:/path/to/file的形式注入。
 * 我在网上看了下，有人说在maven工程下classpath一般就是指src/main/java，src/main/resources，src/main/webapp这几个路径。不知道对不对啊
 * 	           
 */
@Component
public class AppService {
	
	@Value("1")
	private int version;
	
	@Value("classpath:/logo.txt")
	private Resource resource;
	
	@Value("classpath:/app.properties")
	private Resource properties;
	
	private String logo;
	
	@PostConstruct
	public void init() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))){
			this.logo = reader.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (InputStreamReader input = new InputStreamReader(properties.getInputStream(), StandardCharsets.UTF_8)){
			Properties props = new Properties();
			props.load(input);
			String name = props.getProperty("app.name", "Cannot fetch the property[app.name]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printLogo() {
		System.out.println(logo);
		System.out.println("app.version: " + version);
	}
}
