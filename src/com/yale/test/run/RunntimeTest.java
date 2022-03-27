package com.yale.test.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Runtime描述的是运行时的状态,也就是说在整个的JVM之中,Runtime类是唯一一个与JVM运行状态有关的类,
 * 并且都会默认提供有一个该类的实例化对象。
 * 由于在每一个JVM进程里面只允许提供一个Runtime类的对象,所以这个类的构造方法被默认私有化了,那么就证明该类
 * 使用的是单例设计模式
 * 阿里云 魔乐科技的JAVA教程视频
 * https://edu.aliyun.com/lesson_36_446?spm=5176.8764728.0.0.3ead521ct2pu1e#_446
 * https://edu.aliyun.com/course/explore/gentech?spm=5176.10731491.category.12.78904d8aB9zl2v&subCategory=java&filter%5Btype%5D=all&filter%5Bprice%5D=all&filter%5BcurrentLevelId%5D=all&orderBy=studentNum&title=
 * https://www.ruanyifeng.com/blog/2012/05/internet_protocol_suite_part_i.html 《互联网协议入门（一）》
 * https://www.ruanyifeng.com/blog/2012/06/internet_protocol_suite_part_ii.html 《互联网协议入门（二）》
 * https://mp.weixin.qq.com/s/D_Zrq6L9m4cjS6oji0c9sw《Mac 地址会重复吗？Mac 地址也会耗尽吗？ 》
 * MAC地址（物理地址、硬件地址）是实实在在的网络设备出身地址，它是由厂商写入网络设备的bios中。
 * 网络设备厂商也并不能随意的使用Mac地址，需要向IEEE申请，当然厂商申请需要付费。
 * Mac地址通常表示为12个16进制数，每2个16进制数之间用冒号隔开，前6位16进制数代表了网络硬件制造商的编号，由IEEE分配，而后3位16进制数是由网络产品制造产商自行分配。
 * 这样就可以保证世界上每个网络设备具有唯一的MAC地址，比如一台电脑的网卡坏掉了之后，更换一块网卡之后MAC地址就会变。
 * step1：源主机首先会向局域网中发送ARP的广播请求，只要目标mac地址是FF:FF:FF:FF:FF:FF，局域网内的所有设备都会受到这个请求。
 * @author dell
 */
public class RunntimeTest {
	private static final int CPUTIME = 5000;  
    
    private static final int PERCENT = 100;  
  
    private static final int FAULTLENGTH = 10; 
    
	public static void main(String[] args) throws InterruptedException {
		RunntimeTest sdf = new RunntimeTest();
		sdf = null;//等待垃圾回收
		Runtime run = Runtime.getRuntime();
		System.out.println("获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		/**
		 * https://www.liaoxuefeng.com/article/895889887461120
		 * 避免OutOfMemoryError
		 * 对于MIDP应用程序来说，由于手机设备上的资源非常有限，较弱的CPU计算能力，有限的内存（从几十KB到几百KB，虽然少数高端手机拥有超过1M的动态内存），很小的屏幕尺寸，
		 * 因此，为了让一个MIDP应用程序能够不加改动地在多种不同手机上运行，程序必须有能力根据系统配置自动调整运行时的参数。比如，对于内存非常小的手机，如果从网络下载一幅较大的图像，需要分配巨大的缓冲区，
		 * 就可能导致OutOfMemoryError错误，使应用程序直接终止，这会使用户感到不知所措，或者丢失用户的重要数据。因此，在试图分配一块大内存之前，
		 * 首先使用System.gc()尝试让垃圾收集器释放无用对象占用的内存，然后，使用Runtime.getRuntime().freeMemory()方法获得可用的内存空间。
		 * 如果可用空间太小，给用户一个“内存不足，无法完成操作”的Alert提示，从而尽可能地避免OutOfMemoryError错误。
		 * // 示例代码：
			System.gc();
			int max_size = 102400; // 100KB
			int free_size = (int)Runtime.getRuntime().freeMemory();
			if(max_size<free_size*2/3) {
			    // TODO: Alert!
			}
			else {
			    byte[] buffer = new byte[max_size];
			    // TODO: Download image...
			}
		 * 减少图片以减小JAR文件大小
		 * 许多手机会因为JAR文件太大而无法运行MIDP应用程序，而减小JAR文件尺寸的有效方法之一是减少不必要的图片，例如，启动时的LOGO图片可以用文字来代替，列表项可以只显示文字而不显示图片。
		 * 为了能适应不同配置的手机，我们的代码就应该编写得更加灵活。例如，从JAR包中加载图片时：
		 * Image image = null;
			try {
			    image = Image.createImage("/logo.png");
			}
			catch(Exception ioe) {}
			if(image==null) {
			    g.setColor(0);
			    g.drawString("info", getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.BASELINE);
			}
			else {
			    g.drawImage(image, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);
			}
		 * 如果加载失败，程序会以文字方式显示，这样，对于低配置的手机，只需要把美化界面的图片删除掉，再重新打包即可得到一个可发布的尺寸较小的JAR包，同时应用程序的代码并没有改动。
		 * 类似的，在加载List之类的UI组件时：
		 * Image image = null;
			try {
			    image = Image.createImage("/logo.png");
			}
			catch(Exception ioe) {}
			append("label", image);
		 * 这使得有无图片仅仅影响界面美观，而不影响应用程序的功能。
		 */
		System.out.println("获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		String str = "垃圾空间";
		for (int i=0;i <30000; i++) {
			str = str + i;
		}
		System.out.println("[2]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[2]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[2]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		Thread.sleep(2000);
		run.gc();//手工调用垃圾回收
		System.gc();//System.gc内部调用的就是run.gc()
		System.runFinalization(); // A's finalize() is ensured to run
		Runtime.getRuntime().runFinalization();//System.runFinalization();调的就是Runtime这个类里面的getRuntime().runFinalization();方法
		/*
		 * https://www.hollischuang.com/archives/4823
		 * 我们都知道，想要在Linux中终止一个进程有两种方式，如果是前台进程可以使用Ctrl+C键进行终止；如果是后台进程，那么需要使用kill命令来终止。（其实Ctrl+C也是kill命令）
		 * 我们都知道，在Linux中，Java应用是作为一个独立进程运行的，Java程序的终止运行是基于JVM的关闭实现的，JVM关闭方式分为3种：
		 * 正常关闭：当最后一个非守护线程结束或者调用了System.exit或者通过其他特定平台的方法关闭（接收到SIGINT（2）、SIGTERM（15）信号等）
		 * 强制关闭：通过调用Runtime.halt方法或者是在操作系统中强制kill（接收到SIGKILL（9）信号)
		 * 异常关闭：运行中遇到RuntimeException异常等。
		 * JVM进程在接收到kill -15信号通知的时候，是可以做一些清理动作的，比如删除临时文件等。当然，开发者也是可以自定义做一些额外的事情的，比如让tomcat容器停止，让dubbo服务下线等。
		 * 而这种自定义JVM清理动作的方式，是通过JDK中提供的shutdown hook实现的。JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，可以注册一个JVM关闭的钩子。
		 */
		run.halt(10);
		System.exit(1);//背后实际上调用的就是Runtime.getRuntime().exit(status);
		
		/*
		 * JVM进程在接收到kill -15信号通知的时候，是可以做一些清理动作的，比如删除临时文件等。当然，开发者也是可以自定义做一些额外的事情的，比如让tomcat容器停止，让dubbo服务下线等。
		 * 而这种自定义JVM清理动作的方式，是通过JDK中提供的shutdown hook实现的。JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，可以注册一个JVM关闭的钩子。
		 * 例子如下：
		 * 然后等程序运行起来之后去CMD命令窗口上面执行命令：
		 * ➜ jps
			6520 ShutdownHookTest
			6521 Jps
			➜ kill 6520
		 * 控制台输出内容：
		 * hook execute...,我被你使用kill 命令杀死了,但是你不能使用kill -9,kill -9这个命令会强制结束进程,我就没有机会执行了.
		 * Process finished with exit code 143 (interrupted by signal 15: SIGTERM)
		 * 可以看到，当我们使用kill（默认kill -15）关闭进程的时候，程序会先执行我注册的shutdownHook，然后再退出，并且会给出一个提示：interrupted by signal 15: SIGTERM
		 * 如果我们执行命令kill -9：
		 * kill -9 6520
		 * 控制台输出内容：
		 * Process finished with exit code 137 (interrupted by signal 9: SIGKILL)
		 * 可以看到，当我们使用kill -9 强制关闭进程的时候，程序并没有执行shutdownHook，而是直接退出了，并且会给出一个提示：interrupted by signal 9: SIGKILL
		 * 总结
		 * kill命令用于终止Linux进程，默认情况下，如果不指定信号，kill 等价于kill -15。
		 * kill -15执行时，系统向对应的程序发送SIGTERM（15）信号，该信号是可以被执行、阻塞和忽略的，所以应用程序接收到信号后，可以做一些准备工作，再进行程序终止。
		 * 有的时候，kill -15无法终止程序，因为他可能被忽略，这时候可以使用kill -9，系统会发出SIGKILL（9）信号，该信号不允许忽略和阻塞，所以应用程序会立即终止。
		 * 这也会带来很多副作用，如数据丢失等，所以，在非必要时，不要使用kill -9命令，尤其是那些web应用、提供RPC服务、执行定时任务、包含长事务等应用中，因为kill -9 没给spring容器、tomcat服务器、dubbo服务、流程引擎、状态机等足够的时间进行收尾。
		 * https://juejin.cn/post/7033192046449917959 《咱们从头到尾说一次优雅关闭 》
		 * JAVA 监听关闭
		 * JAVA 提供了一个简单的关闭事件的监听机制，可以接收到正常关闭信号的事件，比如命令行程序下的 Ctrl+C 退出信号。
		 * Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			    @Override
			    public void run() {
			        System.out.println("Before shutdown...");
			    }
			}));
		 * 在这段配置完成后，正常关闭前，ShutdownHook的线程就会被启动执行，输出 Before shutdown。当然你要是直接强制关闭，比如Windows下的结束进程，Linux 下的 Kill -9……神仙都监听不到
		 * 进程停止
		 * JAVA 进程停止的机制是，所有非守护线程都已经停止后，进程才会退出。那么直接给JAVA进程发一个关闭信号，进程就能关闭吗？肯定不行！
		 * JAVA 里的线程默认都是非阻塞线程，非守护线程会只要不停，JVM 进程是不会停止的。所以收到关闭信号后，得自行关闭所有的线程，比如线程池……
		 * 线程中断
		 * 线程怎么主动关闭？抱歉，这个真关不了（stop 方法从JAVA 1.1就被废弃了），只能等线程自己执行完成，或者通过软状态加 interrupt 来实现：
		 * private volatile boolean stopped = false;
			@Override
			public void run() {
			    while (!stopped && Thread.interrupted()){
			    	// do sth...
			    }
			}
			
			public void stop(){
				stopped = true;
				interrupt();
			}
		 * 当线程处于 WAITTING 状态时，interrupt 方法会中断这个 WAITTING 的状态，强制返回并抛出 InterruptedException 。比如我们的线程正在卡在 Socket Read 操作上，或者 Object.wait/JUC 下的一些锁等待状态时，调用 interrupt 方法就会中断这个等待状态，直接抛出异常。
		 * 但如果线程没卡在 WAITING 状态，而且还是在线程池中创建的，没有软状态，那上面这个关闭策略可就不太适用了。
		 * 线程池的关闭策略
		 * ThreadPoolExecutor 提供了两个关闭方法：
		 * shutdown - interrupt 空闲的 Worker线程，等待所有任务（线程）执行完成。因为空闲 Worker 线程会处于 WAITING 状态，所以interrupt 方法会直接中断 WAITING 状态，停止这些空闲线程。
		 * shutdownNow - interrupt 所有的 Worker 线程，不管是不是空闲。对于空闲线程来说，和 shutdown 方法一样，直接就被停止了，可以对于正在工作中的 Worker 线程，不一定处于 WAITING状态，所以 interrupt 就不能保证关闭了。
		 * 注意：大多数的线程池，或者调用线程池的框架，他们的默认关闭策略是调用 shutdown，而不是 shutdownNow，所以正在执行的线程并不一定会被 Interrupt
		 * 但作为业务线程，一定要处理 **InterruptedException**。不然万一有shutdownAll，或者是手动创建线程的中断，业务线程没有及时响应，可能就会导致线程彻底无法关闭了
		 * 三方框架的关闭策略
		 * 除了 JDK 的线程池之外，一些三方框架/库，也会提供一些正常关闭的方法。
			Netty 里的 EventLoopGroup.shutdownGracefully/shutdown  - 关闭线程池等资源
			Reddsion 里的 Redisson.shutdown - 关闭连接池的连接，销毁各种资源
			Apache HTTPClient 里的 CloseableHttpClient.close - 关闭连接池的连接，关闭 Evictor 线程等
		 * 这些主流的成熟框架，都会给你提供一个优雅关闭的方法，保证你在调用关闭之后，它可以销毁资源，关闭它自己创建的线程/池。
		 * 尤其是这种涉及到创建线程的三方框架，必须要提供正常关闭的方法，不然可能会出现线程无法关闭，导致最终 JVM 进程不能正常退出的情况。
		 * Tomcat 里的优雅关闭
		 * Tomcat 的关闭脚本（sh 版本）设计的很不错，直接手摸手的告诉你应该怎么关：
		 * commands:
    		stop              Stop Catalina, waiting up to 5 seconds for the process to end
    		stop n            Stop Catalina, waiting up to n seconds for the process to end
    		stop -force       Stop Catalina, wait up to 5 seconds and then use kill -KILL if still running
    		stop n -force     Stop Catalina, wait up to n seconds and then use kill -KILL if still running
    	 * 这个设计很灵活，直接提供 4 种关闭方式，任你随便选择。
    	 * 在 force 模式下，会给进程发送一个 SIGTERM Signal（kill -15），这个信号是可以被 JVM 捕获到的，会执行注册的 ShutdownHook 线程。等待5秒后如果进程还在，就 Force Kill，流程如下图所示：
    	 * 接着 Tomcat 里注册的 ShutdownHook 线程会被执行，手动的关闭各种资源，比如 Tomcat 自己的连接，线程池等等。 ​
    	 * 当然还有最重要的一步，关闭所有的 APP：
    	 * // org.apache.catalina.core.StandardContext#stopInternal
    	 * // 关闭所有应用下的所有 Filter - filter.destroy();
		 * filterStop();
		 * // 关闭所有应用下的所有 Listener - listener.contextDestroyed(event);
		 * listenerStop();
		 * 借助这俩关闭前的 Hook，应用程序就可以自行处理关闭了，比如在 XML 时代时使用的Servlet Context Listener：
		 * <listener>
			        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
			</listener>
		 * Spring 在这个 Listener 内，自行调用 Application Context 的关闭方法：
		 * public void contextDestroyed(ServletContextEvent event) {
			    // 关闭 Spring Application Context
				this.closeWebApplicationContext(event.getServletContext());
				ContextCleanupListener.cleanupAttributes(event.getServletContext());
			}
		 * Spring Boot 优雅关闭
		 * 到了 Spring Boot 之后，这个关闭机制发生了一点点变化。因为之前是 Spring 项目部署在 Tomcat 里运行，由Tomcat 来启动 Spring。
		 * 可在 Spring Boot（Executeable Jar 方式）中，顺序反过来了，因为是直接启动 Spring ，然后在 Spring 中来启动 Tomcat（Embedded）。启动方式变了，那么关闭方式肯定也变了，shutdownHook 由 Spring 来负责，最后 Spring 去关闭 Tomcat。
		 * 如下图所示，这是两种方式的启动/停止顺序： 
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				System.out.println("hook execute...,我被你使用kill 命令杀死了,但是你不能使用kill -9,kill -9这个命令会强制结束进程,我就没有机会执行了.");}
			));
		
		/**
		 * 
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				System.out.println("hook execute...");}
			));
		 */
		
		System.out.println("[3]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[3]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[3]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		
//		RunntimeTest sdf = new RunntimeTest();
//		try {
//			sdf.finalize();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		try {
			run.exec("calc");//这里可以执行CMD命令,calc打开windows的计算机
			run.exec("notepad");//这里可以执行CMD命令,notepad打开记事本
			run.exec("CMD");//这里可以执行CMD命令,cmd打开cmd命令窗口
			run.exec("mspaint");//这里可以执行CMD命令,mspaint打开windows的画图程序
			//testGetSysInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * finailize方法是Object类中定义的方法，意味着任何一个对象都有这个方法。但这个方法只会调用一次，如果把这个对象复活后再次让这个对象死亡，.
	 * 那第2次回收该对象的时候是不会调用finailize方法的，而且优先级比较低，并不能保证一定会被执行，因此不建议使用finalize方法
	 */
	@Override
	protected void finalize() throws Throwable {
		/**
		 * 但是从JDK1.9开始,这一操作已经不建议使用了,而对于对象回收释放.从JDK 
		 * 或者使用java.lang.ref.Cleaner类进行回收(Cleaner也支持有AutoCloseable处理).
		 * 1、GC之前被调用 。2、只会被调用一次。3、不可靠，不能保证被执行，不建议使用。
		 */
		System.out.println("finalize方法会在JAVA进行垃圾回收前,被自动调用,这个方法抛出的异常类型为Throwable,但是这个类即使抛出异常了,也不影响后面代码的执行" );
		throw new Exception("我不影响后面代码的继续运行,因为我这个类马上就死了");
	}
	
	/**
	 * java获取硬件信息
	 * Java程序如果需要获取系统的硬件信息，如CPU、硬盘序列号，网卡MAC地址等，一般需要使用JNI程序调用，不过还有另外一种方法也可以实现这个功能，
	 * 并且不需要JNI调用，原理是根据操作系统调用系统命令command，再根据命令行返回的信息分析获取硬件信息，这个方法需要程序判断操作系统类型来进行相应的调用，
	 * 可以调用任何操作系统提供的命令，下面是Windows下面获取网卡MAC地址的例子：
	 * http://blog.sina.com.cn/s/blog_5ca9fdd80100bd59.html
	 */
	public static void testGetSysInfo() {
		String address = "";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c ipconfig /all";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println("*****" + line);
					if (line.indexOf("Physical Address") > 0 || line.indexOf("物理地址") > 0) {
						int index = line.indexOf(":");
						index += 2;
						address = line.substring(index);
						break;
					}
				}
				br.close();
				System.out.println("電腦的MAC地址為:mac address:" + address.trim());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
   /**
    * 获得CPU使用率.
    * @return 返回cpu使用率
    * @author amg     * Creation date: 2008-4-25 - 下午06:05:11
	* wmic很强大，网上有很多wmic的命令，
	* eg:wmic 获取物理内存
	* wmic memlogical get TotalPhysicalMemory
	* wmic 获取进程信息，很详细
	* wmic process
	* https://www.cnblogs.com/Rozdy/p/5522873.html
    */  
   private double getCpuRatioForWindows() {
       try { 
    	   /**
    	    * wmic很强大，网上有很多wmic的命令，
    	    * eg:wmic 获取物理内存
			* wmic memlogical get TotalPhysicalMemory
			* https://superuser.com/questions/334641/whats-the-equivalent-command-of-wmic-memlogical-in-windows-7
			* wmic 获取进程信息，很详细
			* wmic process
			* wmic 删除指定进程(根据进程PID):
			* wmic process where pid="123" delete
			* https://www.cnblogs.com/scotth/p/9434412.html
    	    */
           String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                   + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";  
           // 取进程信息  
           long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));  
           Thread.sleep(CPUTIME);  
           long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));  
           if (c0 != null && c1 != null) {  
               long idletime = c1[0] - c0[0];  
               long busytime = c1[1] - c0[1];  
               return Double.valueOf(  
                       PERCENT * (busytime) / (busytime + idletime))  
                       .doubleValue();  
           } else {  
               return 0.0;  
           }  
       } catch (Exception ex) {  
           ex.printStackTrace();  
           return 0.0;  
       }  
   }
   
   /**
    * 读取CPU信息.
    * @param proc
    * @return
    * @author amg     * Creation date: 2008-4-25 - 下午06:10:14
    */  
   private long[] readCpu(final Process proc) {  
       long[] retn = new long[2];  
       try {  
           proc.getOutputStream().close();  
           InputStreamReader ir = new InputStreamReader(proc.getInputStream());  
           LineNumberReader input = new LineNumberReader(ir);  
           String line = input.readLine();  
           if (line == null || line.length() < FAULTLENGTH) {  
               return null;  
           }  
           int capidx = line.indexOf("Caption");  
           int cmdidx = line.indexOf("CommandLine");  
           int rocidx = line.indexOf("ReadOperationCount");  
           int umtidx = line.indexOf("UserModeTime");  
           int kmtidx = line.indexOf("KernelModeTime");  
           int wocidx = line.indexOf("WriteOperationCount");  
           long idletime = 0;  
           long kneltime = 0;  
           long usertime = 0;  
           while ((line = input.readLine()) != null) {  
               if (line.length() < wocidx) {  
                   continue;  
               }  
               // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,  
               // ThreadCount,UserModeTime,WriteOperation  
               String caption = Bytes.substring(line, capidx, cmdidx - 1)  
                       .trim();  
               String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();  
               if (cmd.indexOf("wmic.exe") >= 0) {  
                   continue;  
               }  
               // log.info("line="+line);  
               if (caption.equals("System Idle Process")  
                       || caption.equals("System")) {  
                   idletime += Long.valueOf(  
                           Bytes.substring(line, kmtidx, rocidx - 1).trim())  
                           .longValue();  
                   idletime += Long.valueOf(  
                           Bytes.substring(line, umtidx, wocidx - 1).trim())  
                           .longValue();  
                   continue;  
               }  
 
               kneltime += Long.valueOf(  
                       Bytes.substring(line, kmtidx, rocidx - 1).trim())  
                       .longValue();  
               usertime += Long.valueOf(  
                       Bytes.substring(line, umtidx, wocidx - 1).trim())  
                       .longValue();  
           }  
           retn[0] = idletime;  
           retn[1] = kneltime + usertime;  
           return retn;  
       } catch (Exception ex) {  
           ex.printStackTrace();  
       } finally {  
           try {  
               proc.getInputStream().close();  
           } catch (Exception e) {  
               e.printStackTrace();  
           }  
       }  
       return null;  
   }  
}

class Bytes {   
    /** *//**  
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在  
     * 包含汉字的字符串时存在隐患，现调整如下：  
     * @param src 要截取的字符串  
     * @param start_idx 开始坐标（包括该坐标)  
     * @param end_idx   截止坐标（包括该坐标）  
     * @return  
     */  
    public static String substring(String src, int start_idx, int end_idx){   
        byte[] b = src.getBytes();   
        String tgt = "";   
        for(int i=start_idx; i<=end_idx; i++){   
            tgt +=(char)b[i];   
        }   
        return tgt;   
    }   
}  
