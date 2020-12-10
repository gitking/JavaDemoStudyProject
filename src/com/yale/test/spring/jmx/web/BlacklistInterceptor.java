package com.yale.test.spring.jmx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yale.test.spring.jmx.mbean.BlacklistMBean;

/*
 * 集成JMX
 * 在Spring中，可以方便地集成JMX。
 * 那么第一个问题来了：什么是JMX？
 * JMX是Java Management Extensions，它是一个Java平台的管理和监控接口。为什么要搞JMX呢？因为在所有的应用程序中，对运行中的程序进行监控都是非常重要的，Java应用程序也不例外。我们肯定希望知道Java应用程序当前的状态，例如，占用了多少内存，分配了多少内存，当前有多少活动线程，有多少休眠线程等等。如何获取这些信息呢？
 * 为了标准化管理和监控，Java平台使用JMX作为管理和监控的标准接口，任何程序，只要按JMX规范访问这个接口，就可以获取所有管理与监控信息。
 * 实际上，常用的运维监控如Zabbix、Nagios等工具对JVM本身的监控都是通过JMX获取的信息。
 * 因为JMX是一个标准接口，不但可以用于管理JVM，还可以管理应用程序自身。下图是JMX的架构：
	 *  ┌─────────┐  ┌─────────┐
	    │jconsole │  │   Web   │
	    └─────────┘  └─────────┘
	         │            │
	┌ ─ ─ ─ ─│─ ─ ─ ─ ─ ─ ┼ ─ ─ ─ ─
	 JVM     ▼            ▼        │
	│   ┌─────────┐  ┌─────────┐
	  ┌─┤Connector├──┤ Adaptor ├─┐ │
	│ │ └─────────┘  └─────────┘ │
	  │       MBeanServer        │ │
	│ │ ┌──────┐┌──────┐┌──────┐ │
	  └─┤MBean1├┤MBean2├┤MBean3├─┘ │
	│   └──────┘└──────┘└──────┘
	 ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * JMX把所有被管理的资源都称为MBean（Managed Bean），这些MBean全部由MBeanServer管理，如果要访问MBean，可以通过MBeanServer对外提供的访问接口，例如通过RMI或HTTP访问。
 * 注意到使用JMX不需要安装任何额外组件，也不需要第三方库，因为MBeanServer已经内置在JavaSE标准库中了。JavaSE还提供了一个jconsole程序，用于通过RMI连接到MBeanServer，这样就可以管理整个Java进程。
 * 除了JVM会把自身的各种资源以MBean注册到JMX中，我们自己的配置、监控信息也可以作为MBean注册到JMX，这样，管理程序就可以直接控制我们暴露的MBean。因此，应用程序使用JMX，只需要两步：
 * 1.编写MBean提供管理接口和监控数据；
 * 2.注册MBean。
 * 在Spring应用程序中，使用JMX只需要一步：
 * 1.编写MBean提供管理接口和监控数据。
 * 第二步注册的过程由Spring自动完成。我们以实际工程为例，首先在AppConfig中加上@EnableMBeanExport注解，告诉Spring自动注册MBean：
 * 剩下的全部工作就是编写MBean。我们以实际问题为例，假设我们希望给应用程序添加一个IP黑名单功能，凡是在黑名单中的IP禁止访问，传统的做法是定义一个配置文件，启动的时候读取：
 * 如果要修改黑名单怎么办？修改配置文件，然后重启应用程序。
 * 但是每次都重启应用程序实在是太麻烦了，能不能不重启应用程序？可以自己写一个定时读取配置文件的功能，检测到文件改动时自动重新读取。
 * 上述需求本质上是在应用程序运行期间对参数、配置等进行热更新并要求尽快生效。如果以JMX的方式实现，我们不必自己编写自动重新读取等任何代码，只需要提供一个符合JMX标准的MBean来存储配置即可。
 * 还是以IP黑名单为例，JMX的MBean通常以MBean结尾，因此我们遵循标准命名规范，首先编写一个BlacklistMBean：
 * 这个MBean没什么特殊的，它的逻辑和普通Java类没有任何区别。
 * 下一步，我们要使用JMX的客户端来实时热更新这个MBean，所以要给它加上一些注解，让Spring能根据注解自动把相关方法注册到MBeanServer中：
 */
@Order(1)
@Component
public class BlacklistInterceptor implements HandlerInterceptor{
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BlacklistMBean blacklistMBean;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ip = request.getRemoteAddr();
		logger.info("check ip address {}...", ip);
		if (blacklistMBean.shouldBlock(ip)) {
			logger.warn("wiil block ip {} for it is in blacklist.", ip);
			response.sendError(403);
			return false;
		}
		return true;
	}
}
