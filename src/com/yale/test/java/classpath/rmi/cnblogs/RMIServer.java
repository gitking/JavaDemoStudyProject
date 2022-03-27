package com.yale.test.java.classpath.rmi.cnblogs;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Java RMI服务端
 * 服务端在创建注册表服务时，在返回的RegistryImpl中调用setup，实际上封装了一个UnicastServerRef，在setup函数中，RegistryImpl由于继承RemoteServer，
 * 又间接继承自RemoteObject，因此其具有RemoteRef这个filed，即this.ref,此时就把UnicastServerRef赋值给ref(子类对象指向父类引用)，
 * 接着调用UnicastServerRefe的xportObject把this.ref传入，跟进exportObject可以发现这里实际是服务端为RegistryImpl用Remteinvocationhandler创建了一个动态代理，
 * 其中被代理的是new的一个UnicastRef，从instanceof RemoteStub可以猜到这个Remote的代理对象应该就是给客户端用的Stub，接着调用this.setSkeleton设置RegisImpl为服务端的skel，即与客户端通信的骨架，流程如下图所示：
 * https://www.cnblogs.com/tr1ple/p/12231677.html 《关于<Java 中 RMI、JNDI、LDAP、JRMP、JMX、JMS那些事儿（上）>看后的一些》
 * @author issuser
 */
public class RMIServer {
	public static void main(String[] args) {
		try {
			//实例化服务端远程对象
			ServicesImpl obj = new ServicesImpl();
			//tip：服务端要绑定到rmi 注册表的对象实现的接口必须继承自remote，而该对象所对应的接口实现类必须继承UnicastRemoteObject，否则需要使用静态方法exportObject处理该对象
			//没有继承UnicastRemoteObject时需要使用静态方法exportObject处理
			Services services = (Services)UnicastRemoteObject.exportObject(obj, 0);
			Registry reg;
			try {
				//createRegistry(9999)方法返回的真实对象为:sun.rmi.registry.RegistryImpl.java
				//rt.jar包的源码
				//RegistryImpl.java的源码在这里https://github.com/openjdk/jdk8u/blob/master/jdk/src/share/classes/sun/rmi/registry/RegistryImpl.java
				//UnicastServerRef.java源码:https://github.com/openjdk/jdk8u/blob/master/jdk/src/share/classes/sun/rmi/server/UnicastServerRef.java
				//https://github.com/openjdk/jdk8u/blob/master/jdk/src/share/classes/sun/rmi/server/Util.java
				/**
				 * 从上面源码就能看到RegisImpl最终传到了createSkeleton函数中，此时得到RegistryImpl类赋给var1，接着拼接上_Skel作为RegistryImpl_skel作为真正服务端接受客户端请求(lookup,rebind等)调用函数的类，对应的文件就是
				 */
				reg = LocateRegistry.createRegistry(9999);
				System.out.println("java RMI registry created. port on 9999...");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Using existing registry");
				reg = LocateRegistry.getRegistry();
			}
			/**
			 * 绑定远程对象到Registry
			 * tip：服务端要绑定到rmi 注册表的对象实现的接口必须继承自remote，而该对象所对应的接口实现类必须继承UnicastRemoteObject，
			 * 否则需要使用静态方法exportObject处理该对象
			 * 在服务端创建注册标服务时调用createRegistry，此时服务端执行后返回的是类RegistryImpl的实例，然后调用其bind
			 * 其中bindings就是一个hashtable，那么实际上在服务端bind的时候就是单纯的执行一个放入远程对象到hashtable中的操作
			 * 那么客户端调用getRegistry实际上根据jdk源码注释可以看到拿到的是远程注册表服务的引用，称作为stub
			 * 然而客户端最终返回的不是RegistryIMPL，返回的是一个动态代理封装过后的Registry对象
			 */
			reg.bind("Services", services);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
}
