package com.yale.test.java.classpath.rmi.demo.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import com.yale.test.java.classpath.rmi.demo.Service;
import com.yale.test.java.classpath.rmi.demo.ServiceServer;

/*
 * 终极任务：通用服务浏览器
 * 我们要做一个没有Jini功能的程序,但很容易实现。它会让你体验Jini,却只有用到RMI。事实上我们的程序与Jini应用程序的主要差别只在于服务是如何探索的。
 * 相对于Jini的查询服务会自动地对所处的网络做广告。我们使用的是必须与远程服务在同一台机器上执行的RMI registry,这当然不会自动的声明。
 * 且服务也不会自动的向查询服务注册,我们必须将它注册给RMI registry.
 * 一旦用户在RMI registry找到服务,应用程序其余的部分及跟Jini的方式几乎一模一样(当然还少了租约和续约机制这回事)
 * 通用服务浏览器就像是个特殊化的网页浏览器,只是所展示的并非HTML网页。此服务浏览器会下载并显示出交互的Java图形界面。
 * 它的运作方式:
 * 1,用户启动并查询在RMI registry上注册为ServiceServer的服务,然后取回stub.
 * 2,客户端对Stub调用getServiceList().它会返回服务的数组。
 * 3,客户端以GUI显示出服务对象的清单。
 * 4,用户从清单选择,因此客户端调用getService()取得实际服务的序列化对象然后在客户端的浏览器上执行。
 * 5,客户端调用刚取得序列化对象的getGuiPanel().此服务的GUI会显示在浏览器中,且用户可与它在本机上交互。此时就不需要远程服务了。
 */
public class ServiceServerImpl extends UnicastRemoteObject implements ServiceServer{
	HashMap serviceList;
	
	/*
	 * ServiceServerImpl这个类实现了ServiceServer.
	 * 这是实际的RMI远程服务(有继承UnicastRemoteObject),它的任务是初始化并存储全部的服务(会被传送给客户端的东西),
	 * 并把自己登记给RMI registry.
	 */
	public ServiceServerImpl() throws RemoteException {
		setUpServices();
	}
	
	private void setUpServices() {
		serviceList = new HashMap();
		serviceList.put("DiceRollingService", new DiceService());
		serviceList.put("DayofTheWeekService", new DayOfTheWeekService());
		serviceList.put("VisualMusicService", new MiniMusicServices());
	}
	
	public Object[] getServiceList() {
		System.out.println("in remote");
		return serviceList.keySet().toArray();
	}
	
	public Service getService(Object serviceKey) {
		Service theService = (Service)serviceList.get(serviceKey);
		return theService;
	}
	
	/*
	 * 运行这个main方法执行必须先执行
	 * 1,D:\GitWorkSpace\JavaDemoStudyProject\WebContent\WEB-INF\classes>rmic com.yale.te
	 * st.java.classpath.rmi.demo.impl.ServiceServerImpl 产生stub和skeleton(注意java8只会产生stub.class文件,skeleton不在是必须的了)
	 * 2,rmiregistry要确定你是从可以存取到该类的目录来启动,最简单的方法就是从这个类的目录来运行
	 * D:\GitWorkSpace\JavaDemoStudyProject\WebContent\WEB-INF\classes>rmiregistry(这个命令必须一直运行着)
	 * 3,启动这个main方法,然后Naming.rebind会把service初始化并注册到RMI registry.
	 */
	public static void main(String[] args) {
		try {
			Naming.rebind("ServiceServer", new ServiceServerImpl());
			System.out.println("Remote Service is running");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
