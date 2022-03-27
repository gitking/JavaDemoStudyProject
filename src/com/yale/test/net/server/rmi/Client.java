package com.yale.test.net.server.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;

import com.yale.test.net.server.rmi.shared.WorldClock;

/*
 * RMI远程调用
 * Java的RMI远程调用是指，一个JVM中的代码可以通过网络实现远程调用另一个JVM的某个方法。RMI是Remote Method Invocation的缩写。
 * 提供服务的一方我们称之为服务器，而实现远程调用的一方我们称之为客户端。
 * 我们先来实现一个最简单的RMI：服务器会提供一个WorldClock服务，允许客户端获取指定时区的时间，即允许客户端调用下面的方法：
 * LocalDateTime getLocalDateTime(String zoneId);
 * 要实现RMI，服务器和客户端必须共享同一个接口。我们定义一个WorldClock接口，代码如下：
 * Java的RMI规定此接口必须派生自java.rmi.Remote，并在每个方法声明抛出RemoteException。
 * 下一步是编写服务器的实现类，因为客户端请求的调用方法getLocalDateTime()最终会通过这个实现类返回结果。实现类WorldClockService代码如下：
 * 现在，服务器端的服务相关代码就编写完毕。我们需要通过Java RMI提供的一系列底层支持接口，把上面编写的服务以RMI的形式暴露在网络上，客户端才能调用：
 * 上述代码主要目的是通过RMI提供的相关类，将我们自己的WorldClock实例注册到RMI服务上。RMI的默认端口是1099，最后一步注册服务时通过rebind()指定服务名称为"WorldClock"。
 * 下一步我们就可以编写客户端代码。RMI要求服务器和客户端共享同一个接口，因此我们要把WorldClock.java这个接口文件复制到客户端，然后在客户端实现RMI调用：
 * 先运行服务器，再运行客户端。从运行结果可知，因为客户端只有接口，并没有实现类，因此，客户端获得的接口方法返回值实际上是通过网络从服务器端获取的。整个过程实际上非常简单，对客户端来说，
 * 客户端持有的WorldClock接口实际上对应了一个“实现类”，它是由Registry内部动态生成的，并负责把方法调用通过网络传递到服务器端。而服务器端接收网络调用的服务并不是我们自己编写的WorldClockService，
 * 而是Registry自动生成的代码。我们把客户端的“实现类”称为stub，而服务器端的网络服务类称为skeleton，它会真正调用服务器端的WorldClockService，
 * 获取结果，然后把结果通过网络传递给客户端。整个过程由RMI底层负责实现序列化和反序列化：
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ┐         ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	  ┌─────────────┐                                 ┌─────────────┐
	│ │   Service   │ │         │                     │   Service   │ │
	  └─────────────┘                                 └─────────────┘
	│        ▲        │         │                            ▲        │
	         │                                               │
	│        │        │         │                            │        │
	  ┌─────────────┐   Network   ┌───────────────┐   ┌─────────────┐
	│ │ Client Stub ├─┼─────────┼>│Server Skeleton│──>│Service Impl │ │
	  └─────────────┘             └───────────────┘   └─────────────┘
	└ ─ ─ ─ ─ ─ ─ ─ ─ ┘         └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * Java的RMI严重依赖序列化和反序列化，而这种情况下可能会造成严重的安全漏洞，因为Java的序列化和反序列化不但涉及到数据，还涉及到二进制的字节码，
 * 即使使用白名单机制也很难保证100%排除恶意构造的字节码。因此，使用RMI时，双方必须是内网互相信任的机器，不要把1099端口暴露在公网上作为对外服务。
 * 此外，Java的RMI调用机制决定了双方必须是Java程序，其他语言很难调用Java的RMI。如果要使用不同语言进行RPC调用，可以选择更通用的协议，例如gRPC（https://grpc.io）。
 * 问:老师，JMS是否使用了序列化和反序列化，是否也会造成安全漏洞，不推荐使用?
 * 答:序列化/反序列化只要只涉及数据，不涉及代码，理论上就是安全的，JMS的ByteMessage和StringMessage不涉及序列化
 * 问:RMI要求服务端和客户端共享接口，例子给的是同一个项目里建立的接口WorldClock 。那么不同项目里是不是要求两个项目建立的接口要完全一致？
 * 答:把需要共享的接口单独拆个工程，两边都引用这个共享的工程
 * -----------------------
 * 我们也会很快第看过Servlet,Enterprise Java Bean(EJB),Jini以及EJB与Jini是如何运用RMI。
 * 为什么会需要RMI，比如能力有限的便携设备想要拥有运算能力强大的服务器的运算能力。
 * 远程过程调用的设计要创建出4种东西:服务器,客户端,服务器辅助设施和客户端辅助设施。
 * 辅助设施会处理所有客户端和服务器的底层网络输入/输出细节,让你的客户端和程序好像在处理本机调用一样。辅助设施是个在实际执行通信的对象。他们会让客户端感觉上好像是在调用本机的对象。事实上正是这样,
 * 客户端调用辅助设施的方法,就好像客户端就是服务器一样。客户端是真正服务的代理(proxy)。客户端对象看起来像是在调用远程的方法,但实际上它只是在调用本地处理Socket和串流细节的"代理"。
 * 辅助设施会去连接服务器,将调用的信息传送过去(像是方法的名称和参数内容),然后等待服务器的响应。服务器的辅助设施会通过Socket连接来自客户端设施的要求,解析打包送来的消息,然后调用真正的服务,因此对服务对象来说此调用
 * 来自于本地。在RMI中,客户端的辅助设施称为stub,而服务器端的辅助设施称为skeleton。
 * 《Head First Java》第611页,远程部署的RMI
 * 此类可以和com.yale.test.java.classpath.rmi.MyRemote.java结合起来看
 */
public class Client {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		WorldClock worldClock = (WorldClock)registry.lookup(WorldClock.class.getSimpleName());
		LocalDateTime now = worldClock.getLocalDateTime("Asia/Shanghai");
		System.out.println(now);
	}
}
