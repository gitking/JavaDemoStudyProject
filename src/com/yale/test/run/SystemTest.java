package com.yale.test.run;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * https://www.liaoxuefeng.com/article/895889887461120
 * 获取设备支持的可选API
 * J2ME规范包括了许多可选包，如支持多媒体功能的MMAPI，支持消息接收和发送的WMA，支持3D游戏的M3G API。如果某一款手机支持某个可选API，MIDP应用程序就可以使用它。
 * 但是，让用户回答“本机是否支持MMAPI”是不友好的，发布几个不同版本不但增加了开发的工作量，也让用户难以选择。因此，应用程序应该自己检测手机是否支持某一API，从而在运行期决定是否可以使用此API。
 * MIDP 1.0和2.0应用程序都可以通过System.getProperty(String key)检测某一个属性的信息。如果该属性有效，将返回对应的字符串，否则，返回null，表示系统不支持此功能。
 * 例如，System.getProperty("microedition.profiles")可能的返回值是"MIDP-1.0"或"MIDP-2.0"。
 * 以下是常见的系统属性和可选API的属性，右侧列出了可能的返回值：
 * 
 * JDK17竟然有个大Bug：ForkJoinPool在单核机器上会挂起，平时开发都是4核8核，结果扔到AWS的单核跑就挂了：
 * https://bugs.openjdk.java.net/browse/JDK-8274349
 * 临时解决方案：
 * 加启动参数
 * -Djava.util.concurrent.ForkJoinPool.common.parallelism=1
 * 或者在main()的第一行开始写
 * if (Runtime.getRuntime().availableProcessors() <= 1) {
 *  	System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
 * }
 * 看了一眼，这bug不10月份就修了。。。不过的确奇葩
 * 源码修了，各个发行版还得自己编译发行，至少我测试的Ubuntu 20.04的apt还没修
 * https://zhuanlan.zhihu.com/p/455322898 《ForkJoinPool在JDK17上挂起的bug》
 * 
 * https://www.felord.cn/_doc/_springboot/2.1.5.RELEASE/_book/pages/using-spring-boot.html#using-boot-devtools-restart-disable
 * 20.2.4、禁用重启
 * 您如果不想使用重启功能，可以使用 spring.devtools.restart.enabled 属性来禁用它。一般情况下，您可以在 application.properties 中设置此属性（重启类加载器仍将被初始化，但不会监视文件更改）。
 * 如果您需要完全禁用重启支持（例如，可能它不适用于某些类库），您需要在调用 SpringApplication.run(​...) 之前将 System 属性 spring.devtools.restart.enabled System 设置为 false。例如：
 * public static void main(String[] args) {
	    System.setProperty("spring.devtools.restart.enabled", "false");
	    SpringApplication.run(MyApp.class, args);
 * }
 * 
 * https://blog.csdn.net/zhaominpro/article/details/82630528
 * 在Linux系统中0 1 2是一个文件描述符
 * 		名称				代码			操作符				Java中表示			Linux 下文件描述符（Debian 为例)
	标准输入(stdin)		0		< 或 <<				   System.in		/dev/stdin -> /proc/self/fd/0 -> /dev/pts/0
	标准输出(stdout)		1		>, >>, 1> 或 1>>		   System.out		/dev/stdout -> /proc/self/fd/1 -> /dev/pts/0
	标准错误输出(stderr)	2		2> 或 2>>			  System.err		/dev/stderr -> /proc/self/fd/2 -> /dev/pts/0
 * 简单做个试验测试下上面的想法：
 *  java代码如下：
 * public class Htest {
	    public static void main(String[] args) {
	        System.out.println("out1");
	        System.err.println("error1");
	    }
	}
 * javac编译后运行下面指令：
 * java Htest 2>&1 > log 这是错误的用法
 * 你会在终端上看到只输出了"error1"，log文件中则只有"out1"
 * 正确的用法是这样的:nohup java -jar app.jar >log 2>&1 &
 * @author issuser
 */
public class SystemTest {

	public static void main(String[] args) {
		
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");

		System.out.println(System.getProperty("java.version"));
		System.out.println(System.getProperty("java.runtime.version"));
		System.out.println(System.getProperty("java.vm.name"));
		System.out.println(System.getProperty("os.name"));
		
		//System.arraycopy(src, srcPos, dest, destPos, length);数组复制
		System.out.println(System.currentTimeMillis());
		
		/**
		 * 常见的编码有下面这些
		 * GBK,GB2312:表示的是国标编码,GBK包含的有简体中文和繁体中文,而GB2312只包含 有简体中文,也就是说这俩个编码都是描述中文的编码;
		 * UNICODE编码:是java提供的十六进制编码,可以描述世界上任意的文字信息,但是如果每个字符都用十六进制编码太浪费了。UTF-8应运而生
		 * UTF-8:是可变的编码
		 * ISO8859-1:是国际通用编码,但是所有的编码都需要进行转换。
		 * https://edu.aliyun.com/lesson_36_484#_484  阿里云 课程《【名师课堂】Java高级开发 》MLDN魔乐科技 课时82 字符编码（常用字符编码）
		 * 2020年4月19日16:37:33
		 */
		System.out.println("java文件编码：" + System.getProperty("file.encoding"));
		System.out.println("" + System.getProperty("sun.reflect.noInflation"));
		System.out.println("" + System.getProperty("sun.reflect.inflationThreshold"));
		
		System.out.println("System.getProperty获取的是JVM启动时通过-D指定的属性:" + System.getProperty("fuckoff"));

		
		
		System.out.println("打印JAVA所有的配置信息,打印JVM所有的配置信息,打印JVM所有的环境变量信息");
		System.getProperties().list(System.out);
		
		System.out.println("--------------------------------------------");
		
		String procCmd = System.getenv("windir");
		System.out.println("windows的默认目录为:" + procCmd);
		
		System.out.println("--------------------------------------------");

		Set<Entry<String, String>> envEntry = System.getenv().entrySet();
		Iterator iterator = envEntry.iterator();
		while(iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			System.out.println("windows环境变量:" + entry.getKey() + ":" + entry.getValue());
		}
		
		System.out.println("--------------------------------------------***");
		System.out.println();
		for(Map.Entry<String, String> me: System.getenv().entrySet()) {
			System.out.println("windows环境变量:" + me.getKey() + ":" + me.getValue());
		}
		
		
 		System.out.println(System.identityHashCode(20)); 
 		
 		/*
 		 * https://bbs.csdn.net/topics/370066482
 		 * 话说，6 年前，我去 myjavaserver.com 注册了一个账户，然后测试了一下，在里面上传一个 hello.jsp，里面写着下面这句话，           <% System.exit(1); %>大家都懂的，第二天，我的账户被设置成只读的了，不能再上传文件了 ^_^ ，
 		 * 楼主想，人家做一个共享的服务器，一句话都让所有程序退出就那足以证明它用的是同一个 jvm 进程。我之所以写这个 jsp 是为了测试它是否安装了合适的 java.security.policy。
 		 * 以前我还在里面上传了一个让 Eclipse 3.0 支持 Eclipse 3.4 里面的右下角那个 GC 功能的插件呢，我没留源码，想去下载这个插件，不过现在我再想起来这个网站时，它已经关闭数年了。 ^_^
 		 * 为什么不是exit(250)啊
 		 * java.security.policy和System.exit(1)这个俩个之间有什么关系吗?
 		 * -Djava.security.policy=/data/weblogic/wls/wlserver_10.3/server/lib/weblogic.policy 我们公司指定的java.security.policy为这个
 		 */
 		System.exit(1);//退出JVM虚拟进程System.exit(1);背后实际上调用的就是Runtime.getRuntime().exit(status);
 		System.load("libarcsoft_face.dll");
        System.load("libarcsoft_face_engine.dll");
        System.load("libarcsoft_face_engine_jni.dll");
	}
	
	public static void testExit() {
		try {
			System.out.println("代码碰见System.exit之后,JVM就自动退出了.finally也不会执行,finally就没机会执行了.");
			System.exit(0);
		} finally {
			System.out.println("+++++代码碰见System.exit之后,JVM就自动退出了.finally也不会执行,finally就没机会执行了.");
		}
	}
}
