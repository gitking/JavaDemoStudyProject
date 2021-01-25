package com.yale.test.java.classpath.rmi.impl;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.yale.test.java.classpath.rmi.MyRemote;

/*
 * 实现Remote这个接口
 * 你的服务必须要实现Remote--就是客户端会调用的方法。
 * 为了要成为远程服务对象,你的对象必须要有与远程有关的功能。其中最简单的方式就是继承UnicastRemoteObject(来自java.rmi.server)以让这个
 * 父类处理这些工作。
 * 远程服务应该要继承UnicastRemoteObject(技术上也有其他的方法可以创建远程对象,但这是最简单的方式)
 * 2,实现远程Remote接口
 */
public class MyRemoteImpl extends UnicastRemoteObject implements MyRemote{
	
	/*
	 * UnicastRemoteObject有个小问题,它的构造函数会抛出RemoteException异常。
	 * 处理它的唯一方式就是对你的实现类声明一个构造函数,并且抛出RemoteException异常。
	 * 要记得当类被初始化的时候,父类的构造函数一定会被调用,如果父类的构造函数抛出异常,你也得声明你的构造函数会抛出异常。
	 */
	public MyRemoteImpl() throws RemoteException{
	}
	
	@Override
	public String sayHello() {
		return "Server says, 'Hey'";
	}
	
	/*
	 * 4,向RMI registry注册服务
	 * 现在你已经有了远程服务,还必须要让远程用户存取。这可以通过将它初始化并加进RMI registry(它一定得要运行起来,不然此行程就会失败)
	 * 当你注册对象,RMI系统会把stub加到registry中,因为这是客户端所需要的。
	 * 使用java.rmi.Naming的rebind()来注册服务。
	 */
	public static void main(String[] args) {
		try {
			MyRemote service = new MyRemoteImpl();
			/*
			 * 3,执行命令rmic MyRemoteImpl 和 rmiregistry
			 * 第一个参数帮服务命名(客户端会靠名字查询registry),并向RMI registry注册,
			 * RMI会将stub做交换并把stub加入registry
			 * 运行这个main方法执行必须先执行
			 * 1,rmic MyRemoteImpl 产生stub和skeleton(注意java8只会产生stub.class文件,skeleton不在是必须的了)
			 * rmic有几个选项,包括1,不产生skeleton,2,观察产出类的源代码3,指定使用IIOP作为通信协议等。
			 * D:\GitWorkSpace\JavaDemoStudyProject\WebContent\WEB-INF\classes>rmic com.yale.test.java.classpath.rmi.impl.MyRemoteImpl
			 * 2,rmiregistry要确定你是从可以存取到该类的目录来启动,最简单的方法就是从这个类的目录来运行
			 * D:\GitWorkSpace\JavaDemoStudyProject\WebContent\WEB-INF\classes>rmiregistry(这个命令必须一直运行着)
			 * 3,启动这个main方法,然后Naming.rebind会把service初始化并注册到RMI registry.
			 * 注意java8Naming.rebind第一个参数"Remote Hello"有空格,会报错
			 * java.net.MalformedURLException: invalid URL String: Remote Hello
			 *	at java.rmi.Naming.parseURL(Unknown Source)
			 *	at java.rmi.Naming.rebind(Unknown Source)
			 *	at com.yale.test.java.classpath.rmi.impl.MyRemoteImpl.main(MyRemoteImpl.java:50)
			 * Caused by: java.net.URISyntaxException: Illegal character in path at index 6: Remote Hello
			 *	at java.net.URI$Parser.fail(Unknown Source)
			 *	at java.net.URI$Parser.checkChars(Unknown Source)
			 *	at java.net.URI$Parser.parseHierarchical(Unknown Source)
			 *	at java.net.URI$Parser.parse(Unknown Source)
			 *	at java.net.URI.<init>(Unknown Source)
			 *	at java.rmi.Naming.intParseURL(Unknown Source)
			 *	... 3 more
			 */
			Naming.rebind("RemoteHello", service);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
