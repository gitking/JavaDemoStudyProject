package com.yale.test.java.classpath.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.yale.test.java.classpath.rmi.MyRemote;

/*
 * 客户端如何取得stub对象?
 * 客户端必须取得stub对象,因为客户端必须要调用它的方法.这就得靠RMI registry了。
 * 客户端会像查询电话簿一样地搜索,找出上面有想法名称的服务。
 * RMI 允许一个应用程序访问另外一个服务器或虚拟机上的对象，方法和服务，它使远程方法调用就像在本地调用一样简单。它为用户屏蔽了底层的网络传输细节，使用的时候只需适当处理异常即可。所以 RMI 是非常容易使用的，但同时是非常强大的。
 * RMI 协议的数据序列化目前支持以下两种模式：
 * 1.基于 JDK 本身的对象序列化
 * 2.基于 HTTP 协议的数据序列化
 */
public class MyRemoteClient {
	public static void main(String[] args) {
		try {
			/*
			 * 
			 * rmi://127.0.0.1/RemoteHello使用rmi协议,RemoteHello是服务端注册的服务名称
			 * MyRemote客户端必须使用与服务相同的类型,事实上客户端不需要知道服务实际上的类名称。
			 * Exception in thread "main" java.lang.ClassCastException: com.yale.test.java.classpath.rmi.impl.MyRemoteImpl_Stub cannot be cast to com.yale.test.java.classpath.rmi.MyRemote2
			 * at com.yale.test.java.classpath.rmi.client.MyRemoteClient.main(MyRemoteClient.java:23)
			 * 1,客户端向registry查询服务Naming.lookup("rmi://127.0.0.1/RemoteHello");
			 * 2,RMI registry会返回stub对象。RMI会自动将服务器端的stub对象解序列化到客户端的堆上面。
			 * 所以客户端必须要有rmic所产生的stub类(包路径都必须一样),否则客户端的stub不会被解序列化.
			 * 3,客户端就像取用真正的服务一样的调用stub上的方法。
			 * 4,用户如何取得stub的类?
			 * 最简单的方法是,直接把rmic产生的stub class文件交给用户,让用户放在指定的包路径下面。
			 * 但更酷的方法称为"dynamic class downloading" 动态下载。使用动态类下载时,stub对象会被加上注明RMI可以去哪里找到该对象
			 * 的类文件的URL标记。之后再解序列化的过程中,RMI会在本机上找不到类,所以就使用HTTP的GET来从该URL取得类文件.因此你会需要
			 * 一个Web服务器来提供类文件,并且也得改变客户端的某些安全性设定。这里面还有些特别的问题,不过我们就先看过动态类下载的概念就行。
			 * 使用RMI时程序员最常犯的3个错误:
			 * 1,忘记在启动远程服务前启动rmiregistry(使用Naming.rebind()注册服务前rmiregistry必须启动)
			 * 2,忘记把参数和返回类型做成可序列化的了(编译器不会检测到,执行时才会发现)
			 * 3,忘记将stub类交给客户端
			 * 注意虽然客户端必须要有stub类,但客户端不会在程序代码中引用到stub类,客户端总是通过接口来操作真正的远程对象。
			 * stub是个处理底层网络细节的辅助性对象,它会把方法的调用包装起来发送到服务器上。
			 * rmiregistry必须在同一台机器上与远程服务一块执行,且必须在对象的注册之前启动。
			 */
			//MyRemote2 service = (MyRemote2)Naming.lookup("rmi://127.0.0.1/RemoteHello");
			MyRemote service = (MyRemote)Naming.lookup("rmi://127.0.0.1/RemoteHello");
			String s = service.sayHello();
			System.out.println("RMI远程调用接口结果:" + s);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
