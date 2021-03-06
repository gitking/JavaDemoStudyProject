package com.yale.test.java.classpath.jini;

/*
 * 我们认为Jini应该是java最棒的东西,如果EJB是打了类固醇的RMI,那么Jini就是长出翅膀的RMI。
 * Jini也是使用RMI(虽然也可以使用别的协议),但多了几个关键功能:
 * 1,adaptive discovery(自适应探索)
 * 2,self-healing networks(自恢复网络)
 * 要知道RMI的客户端得先取得远程服务的地址和名称。客户端的查询程序代码就要带有远程服务的IP地址或主机名(因为RMI registry就在上面)以及服务所注册的名称。
 * 但使用Jini时,用户只需要知道一件事:服务所实现的接口.就这样就行。哪怎么找到的呢?秘诀就在于Jini的lookup service.
 * Jini的查询服务比RMI registry更强更有适应性。因为Jini会在网络里面进行广播,当查询服务上线时,它会使用IP组播技术送出信息给
 * 整个网络说:"我这里有什么什么样的服务"
 * 不只是这样。如果客户端在Jini查询服务已经广播之后上线,客户端也可以发出信息给整个网络询问:"那个谁在不在?"
 * 其实你感兴趣的不是查询服务,而是已经注册的服务。像RMI远程服务,其他可序列化的Java对象。
 * 更棒的还在后头：当服务上线时,它会动态地探索网络上的Jini查询服务并申请注册.注册时,服务会送出一个序列化的对象给查询服务。此对象可以是RMI远程服务的stub,
 * 网络装置的驱动程序。或者是可以在客户端执行的服务本身,并且注册的是所实现的接口,而不是名称。
 * 一旦取得查询服务的引用,客户端就可以询问"有没有东西实现ScientificCalculator?"。此时查询服务若找到,就会返回该服务所放上了的序列化对象。
 * 自适应探索的运作:
 * 1,Jini查询服务在网络上启动,并使用IP组播技术为自己做宣传。
 * 2,已经启动的另外一个Jini服务(注意不是查询服务)会寻求向刚启动的查询服务注册自己,它注册的是功能而不是名称,
 * 也就是锁实现的接口,然后送出序列化对象给Jini查询服务。
 * 3,网络上的其他客户想要取得实现ScientificCalculator的东西可是不知道哪里有,就会去询问Jini查询服务
 * 4,Jini查询服务把查到的序列化对象响应给查询的客户
 * 自恢复网络的运作
 * 1,某个Jini服务要求注册到Jini查询服务上面,Jini查询服务会给Jini服务一个注册止期,Jini服务必须要定期更新这个注册止期,
 * 不然Jini查询服务会假设改Jini服务已经离线,
 * 2,因为网络或者宕机造成的服务离线,没有在注册止期前更新注册止期,Jini查询服务会把该服务踢掉。
 */
public class JiniDemo {
	public static void main(String[] args) {

	}
}
