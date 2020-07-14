package com.yale.test.run.jvm.instrument;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

/*
 * 上一小节中，我们给出了Agent类的代码，追根溯源需要先介绍JPDA（Java Platform Debugger Architecture）。
 * 如果JVM启动时开启了JPDA，那么类是允许被重新加载的。在这种情况下，已被加载的旧版本类信息可以被卸载，然后重新加载新版本的类。
 * 正如JDPA名称中的Debugger，JDPA其实是一套用于调试Java程序的标准，任何JDK都必须实现该标准。
 * JPDA定义了一整套完整的体系，它将调试体系分为三部分，并规定了三者之间的通信接口。三部分由低到高分别是Java 虚拟机工具接口（JVMTI），
 * Java 调试协议（JDWP）以及 Java 调试接口（JDI），三者之间的关系如下图所示：
 * 现在回到正题，我们可以借助JVMTI的一部分能力，帮助动态重载类信息。JVM TI（JVM TOOL INTERFACE，JVM工具接口）是JVM提供的一套对JVM进行操作的工具接口。通过JVMTI可以实现对JVM的多种操作，然后通过接口注册各种事件勾子。在JVM事件触发时，同时触发预定义的勾子，以实现对各个JVM事件的响应，事件包括类文件加载、异常产生与捕获、线程启动和结束、进入和退出临界区、成员变量修改、GC开始和结束、方法调用进入和退出、临界区竞争与等待、VM启动与退出等等。
 * 而Agent就是JVMTI的一种实现，Agent有两种启动方式，一是随Java进程启动而启动，经常见到的java -agentlib就是这种方式；二是运行时载入，通过Attach API，将模块（jar包）动态地Attach到指定进程id的Java进程内。
 * Attach API 的作用是提供JVM进程间通信的能力，比如说我们为了让另外一个JVM进程把线上服务的线程Dump出来，会运行jstack或jmap的进程，并传递pid的参数，告诉它要对哪个进程进行线程Dump，这就是Attach API做的事情。在下面，我们将通过Attach API的loadAgent()方法，将打包好的Agent jar包动态Attach到目标JVM上。具体实现起来的步骤如下：
 * 定义Agent，并在其中实现AgentMain方法，如上一小节中定义的代码块7中的TestAgent类；
 * 然后将TestAgent类打成一个包含MANIFEST.MF的jar包，其中MANIFEST.MF文件中将Agent-Class属性指定为TestAgent的全限定名，如下图所示；
 * 最后利用Attach API，将我们打包好的jar包Attach到指定的JVM pid上，代码如下：
 * https://mp.weixin.qq.com/s/CH9D-E7fxuu462Q2S3t0AA
 */
public class Attacher {
	public static void main(String[] args) {
		//传入目标JVM PID,VirtualMachine这个类在jdk的lib目录下的tools.jar
		VirtualMachine vm;
		try {
			/*
			 * 由于在MANIFEST.MF中指定了Agent-Class，所以在Attach后，目标JVM在运行时会走到TestAgent类中定义的agentmain()方法，
			 * 而在这个方法中，我们利用Instrumentation，将指定类的字节码通过定义的类转化器TestTransformer做了Base类的字节码替换
			 * （通过javassist），并完成了类的重新加载。由此，我们达成了“在JVM运行时，改变类的字节码并重新载入类信息”的目的。
			 * 以下为运行时重新载入类的效果：先运行Base中的main()方法，启动一个JVM，可以在控制台看到每隔五秒输出一次"process"。
			 * 接着执行Attacher中的main()方法，并将上一个JVM的pid传入。此时回到上一个main()方法的控制台，
			 * 可以看到现在每隔五秒输出"process"前后会分别输出"start"和"end"，也就是说完成了运行时的字节码增强，并重新载入了这个类。
			 * 至此，字节码增强技术的可使用范围就不再局限于JVM加载类前了。通过上述几个类库，我们可以在运行时对JVM中的类进行修改并重载了。通过这种手段，可以做的事情就变得很多了：
					热部署：不部署服务而对线上服务做修改，可以做打点、增加日志等操作。
					Mock：测试时候对某些服务做Mock。
					性能诊断工具：比如bTrace就是利用Instrument，实现无侵入地跟踪一个正在运行的JVM，监控到类和方法级别的状态信息。
			 * 总结:字节码增强技术相当于是一把打开运行时JVM的钥匙，利用它可以动态地对运行中的程序做修改，也可以跟踪JVM运行中程序的状态。
			 * 此外，我们平时使用的动态代理、AOP也与字节码增强密切相关，它们实质上还是利用各种手段生成符合规范的字节码文件。
			 * 综上所述，掌握字节码增强后可以高效地定位并快速修复一些棘手的问题（如线上性能问题、方法出现不可控的出入参需要紧急加日志等问题），也可以在开发中减少冗余代码，大大提高开发效率。
			 * 资料:https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
			 * https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html
			 * https://docs.oracle.com/javase/8/docs/platform/jvmti/jvmti.html
			 */
			vm = VirtualMachine.attach("");
			vm.loadAgent("agent.jar");
		} catch (AttachNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AgentLoadException e) {
			e.printStackTrace();
		} catch (AgentInitializationException e) {
			e.printStackTrace();
		}
	}
}
