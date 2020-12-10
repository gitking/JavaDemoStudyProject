package com.yale.test.web.servlet.lxf;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/*
 * 这样，我们直接运行main()方法，即可启动嵌入式Tomcat服务器，然后，通过预设的tomcat.addWebapp("", new File("src/main/webapp")，
 * Tomcat会自动加载当前工程作为根webapp，可直接在浏览器访问http://localhost:8080/：
 * 通过main()方法启动Tomcat服务器并加载我们自己的webapp有如下好处：
 * 1.启动简单，无需下载Tomcat或安装任何IDE插件；
 * 2.调试方便，可在IDE中使用断点调试；
 * 3.使用Maven创建war包后，也可以正常部署到独立的Tomcat服务器中。
 * 对SpringBoot有所了解的童鞋可能知道，SpringBoot也支持在main()方法中一行代码直接启动Tomcat，并且还能方便地更换成Jetty等其他服务器。
 * 它的启动方式和我们介绍的是基本一样的，后续涉及到SpringBoot的部分我们还会详细讲解。
 * 小结
 * 开发Servlet时，推荐使用main()方法启动嵌入式Tomcat服务器并加载当前工程的webapp，便于开发调试，且不影响打包部署，能极大地提升开发效率。
 */
public class TomcatMain {
	public static void main(String[] args) throws LifecycleException {
		
		/*
		 * 经验1:一定要在自己编写的Main 类里RUN，不要在根目录下RUN，根目录下运行Eclipse会运行错误的main方法的
		 * NoClassDefFoundError错误,我在运行main()方法时，出现问题:错误: 无法初始化主类 Main,原因: java.lang.NoClassDefFoundError: org/apache/catalina/WebResourceRoot
		 * 解决办法1:
		 * 1，问题产生的原因：廖大佬用的eclipse，我们用的IDEA，我们在IDEA中，maven配置<scope>provided</scope>，就告诉了IDEA程序会在运行的时候提供这个依赖，但是实际上却并没有提供这个依赖。
		 * 2，解决方法：
		 * 去掉<scope>provided</scope>，改<scope>complie</scope>，然后reimport就可以了。
		 * 廖雪峰回答:
		 * 那是idea的问题，如果你把provided改成compile，生成的war包会很大，因为把tomcat打包进去了
		 * 解决方案:打开idea的Run/Debug Configurations:选择Application - Main,右侧Configuration：Use classpath of module
		 * 钩上☑︎Include dependencies with "Provided" scope
		 * 
		 * 问题1:我在命令行下搞了很久都没试出来，幸好看到各位的回复，否则这一天时间可能就静悄悄的溜走了😂
		 * 不过真要在命令行操作，可以把war改成jar,且provided改成compile，并导入maven-shade-plugin依赖，执行mvn clean package 后运行jar包即可
		 * 之前没会弄，这里回来作下说明😁
		 */
		//启动Tomcat
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();
		
		//创建webapp
		Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		ctx.setResources(resources);
		tomcat.start();
		tomcat.getServer().await();
	}
}
