package com.yale.test.spring.jmx.mbean;

import java.util.HashSet;
import java.util.Set;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/*
 * 还是以IP黑名单为例，JMX的MBean通常以MBean结尾，因此我们遵循标准命名规范，首先编写一个BlacklistMBean：
 * 这个MBean没什么特殊的，它的逻辑和普通Java类没有任何区别。
 * 下一步，我们要使用JMX的客户端来实时热更新这个MBean，所以要给它加上一些注解，让Spring能根据注解自动把相关方法注册到MBeanServer中：
 * 观察上述代码，BlacklistMBean首先是一个标准的Spring管理的Bean，其次，添加了@ManagedResource表示这是一个MBean，将要被注册到JMX。objectName指定了这个MBean的名字，通常以company:name=Xxx来分类MBean。
 * 对于属性，使用@ManagedAttribute注解标注。上述MBean只有get属性，没有set属性，说明这是一个只读属性。
 * 对于操作，使用@ManagedOperation注解标准。上述MBean定义了两个操作：addBlacklist()和removeBlacklist()，其他方法如shouldBlock()不会被暴露给JMX。
 * 使用MBean和普通Bean是完全一样的。例如，我们在BlacklistInterceptor对IP进行黑名单拦截：
 * 下一步就是正常启动Web应用程序，不要关闭它，我们打开另一个命令行窗口，输入jconsole启动JavaSE自带的一个JMX客户端程序：
 * 通过jconsole连接到一个Java进程最简单的方法是直接在Local Process中找到正在运行的AppConfig，点击Connect即可连接到我们当前正在运行的Web应用，在jconsole中可直接看到内存、CPU等资源的监控。
 * 我们点击MBean，左侧按分类列出所有MBean，可以在java.lang查看内存等信息：
 * 细心的童鞋可以看到HikariCP连接池也是通过JMX监控的。
 * 在sample中可以看到我们自己的MBean，点击可查看属性blacklist：
 * 点击Operations-addBlacklist，可以填入127.0.0.1并点击addBlacklist按钮，相当于jconsole通过JMX接口，调用了我们自己的BlacklistMBean的addBlacklist()方法，传入的参数就是填入的127.0.0.1：
 * 再次查看属性blacklist，可以看到结果已经更新了：
 * 我们可以在浏览器中测试一下黑名单功能是否已生效：
 * 可见，127.0.0.1确实被添加到了黑名单，后台日志打印如下：
 * 注意：如果使用IPv6，那么需要把0:0:0:0:0:0:0:1这个本机地址加到黑名单。
 * 如果从jconsole中调用removeBlacklist移除127.0.0.1，刷新浏览器可以看到又允许访问了。
 * 使用jconsole直接通过Local Process连接JVM有个限制，就是jconsole和正在运行的JVM必须在同一台机器。如果要远程连接，首先要打开JMX端口。我们在启动AppConfig时，需要传入以下JVM启动参数：
 * -Dcom.sun.management.jmxremote.port=19999
 * -Dcom.sun.management.jmxremote.authenticate=false
 * -Dcom.sun.management.jmxremote.ssl=false
 * 第一个参数表示在19999端口监听JMX连接，第二个和第三个参数表示无需验证，不使用SSL连接，在开发测试阶段比较方便，生产环境必须指定验证方式并启用SSL。
 * 详细参数可参考Oracle官方文档(https://docs.oracle.com/javase/8/docs/technotes/guides/management/agent.html#gdeum)。这样jconsole可以用ip:19999的远程方式连接JMX。连接后的操作是完全一样的。
 * 许多JavaEE服务器如JBoss的管理后台都是通过JMX提供管理接口，并由Web方式访问，对用户更加友好。
 * 练习
 * 编写一个MBean统计当前注册用户数量，并在jconsole中查看：
 * 小结
 * 在Spring中使用JMX需要：
 * 通过@EnableMBeanExport启用自动注册MBean；
 * 编写MBean并实现管理属性和管理操作。
 */
@Component
@ManagedResource(objectName="sample:name=blacklist", description = "Blacklist of IP addresses")
public class BlacklistMBean {
	private Set<String> ips = new HashSet<String>();
	
	@ManagedAttribute(description="Get IP address in blacklist")
	public String[] getBlacklist() {
		//return ips.toArray(String[]::new);
		return ips.toArray(new String[ips.size()]);
	}
	
	@ManagedOperation
	@ManagedOperationParameter(name="ip", description="Target IP address that will be added to blacklist")
	public void addBlacklist(String ip) {
		ips.add(ip);
	}
	
	@ManagedOperation
	@ManagedOperationParameter(name="ip", description = "Target IP address that will be removed from blacklist")
	public void removeBlacklist(String ip) {
		ips.remove(ip);
	}
	
	public boolean shouldBlock(String ip) {
		return ips.contains(ip);
	}
}
