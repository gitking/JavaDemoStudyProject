package com.yale.test.web.servlet.lxf;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Servlet开发
 * 在上一节中，我们看到，一个完整的Web应用程序的开发流程如下：
 * 1.编写Servlet；
 * 2.打包为war文件；
 * 3.复制到Tomcat的webapps目录下；
 * 4.启动Tomcat。
 * 这个过程是不是很繁琐？如果我们想在IDE中断点调试，还需要打开Tomcat的远程调试端口并且连接上去。
 * 许多初学者经常卡在如何在IDE中启动Tomcat并加载webapp，更不要说断点调试了。
 * 我们需要一种简单可靠，能直接在IDE中启动并调试webapp的方法。
 * 因为Tomcat实际上也是一个Java程序，我们看看Tomcat的启动流程：
 * 1.启动JVM并执行Tomcat的main()方法；
 * 2.加载war并初始化Servlet；
 * 3.正常服务。
 * 启动Tomcat无非就是设置好classpath并执行Tomcat某个jar包的main()方法，我们完全可以把Tomcat的jar包全部引入进来，然后自己编写一个main()方法，先启动Tomcat，然后让它加载我们的webapp就行。
 * 我们新建一个web-servlet-embedded工程，编写pom.xml如下：
 * <project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itranswarp.learnjava</groupId>
    <artifactId>web-servlet-embedded</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <java.version>11</java.version>
        <tomcat.version>9.0.26</tomcat.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>${tomcat.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>${tomcat.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
 * 其中，<packaging>类型仍然为war，引入依赖tomcat-embed-core和tomcat-embed-jasper，引入的Tomcat版本<tomcat.version>为9.0.26。
 * 不必引入Servlet API，因为引入Tomcat依赖后自动引入了Servlet API。因此，我们可以正常编写Servlet如下：
 * 然后，我们编写一个main()方法，启动Tomcat服务器：
 */
@WebServlet(urlPatterns="/tomcat")
public class TomcatDemo extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		String name = req.getParameter("name");
		if (name == null) {
			name = "world!";
		}
		PrintWriter pw = resp.getWriter();
		pw.write("<h1>Hello, " + name + "!</h1>");
		pw.flush();
	}
}
