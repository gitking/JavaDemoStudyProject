package com.yale.test.java.fanshe.perfma;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ReflectTest {

	public static void main(String[] args) {
		try {
			/**
			 * https://www.iteye.com/blog/rednaxelafx-727938
			 * https://www.iteye.com/blog/rednaxelafx-548536
			 * java 反射调用时，默认超过 15 次就会生成 java类进行调用，那么问题是这个生成的类中为什么可以调用 private 的方法?
			 * 答:这个主要是JVM里会做特殊处理，如果是反射的，也就是sun.reflect.MagicAccessorImpl的子类，就会不去check
		     * 可以看下Reflection::verify_field_access方法，虽然这里是访问的method，当时JVM的方法里命名了field
		     * https://club.perfma.com/question/507734?from=timeline#/question/507734?from=timeline
			 * 测试代码
			 * 
			 * GeneratedMethodAccessor2 这个动态生成的类在怎么查看呢？
			 * 答:HSDB 或者 基于Serviceability Agent（下面简称SA）的自定义ClassFilter都行(https://www.iteye.com/blog/rednaxelafx-727938)
			 * 大家熟悉的jstack、jmap等工具在使用-F参数启动时其实就是通过SA来实现功能的。
			 * 
			 * 问题是，上述方式其实只是借助ClassLoader把它在classpath上能找到的Class文件复制了一份而已。如果我们想dump的类在加载时被修改过（例如说某些AOP的实现会这么做），
			 * 或者在运行过程中被修改过（通过HotSwap），或者干脆就是运行时才创建出来的，那就没有现成的Class文件了。
			 * 需要注意，java.lang.Class<T>这个类虽然实现了java.io.Serializable接口，但直接将一个Class对象序列化是得不到对应的Class文件的。
			 * 参考src/share/classes/java/lang/Class.java里的注释：
			 * 
			 * HotSpot有一套私有API提供了对JVM内部数据结构的审视功能，称为Serviceability Agent。它是一套Java API，虽然HotSpot是用C++写的，但SA提供了HotSpot中重要数据结构的Java镜像类，所以可以直接写Java代码来查看一个跑在HotSpot上的Java进程的内部状态。它也提供了一些封装好的工具，可以直接在命令行上跑，包括下面提到的ClassDump工具。
		     * SA的一个重要特征是它是“进程外审视工具”。也就是说，SA并不运行在要审视的目标进程中，而是运行在一个独立的Java进程中，通过操作系统上提供的调试API来连接到目标进程上。这样，SA的运行不会受到目标进程状态的影响，因而可以用于审视一个已经挂起的Java进程，或者是core dump文件。当然，这也就意味这一个SA进程不能用于审视自己。
			 * 一个被调试器连接上的进程会被暂停下来。所以在SA连接到目标进程时，目标进程也是一直处于暂停状态的，直到SA解除连接。如果需要在线上使用SA的话需要小心，
			 * 不要通过SA做过于耗时的分析，宁可先把数据都抓出来，把SA的连接解除掉之后再离线分析。目前的使用经验是，连接上一个小Java进程的话很快就好了，
			 * 但连接上一个“大”的Java进程（堆比较大、加载的类比较多）可能会在连接阶段卡住好几分钟，线上需要慎用。
			 * 目前（JDK6）在Windows上SA没有随HotSpot一起发布，所以无法在Windows上使用；在Linux、Solaris、Mac上使用都没问题。
			 * 从JDK7 build 64开始Windows版JDK也带上SA，如果有兴趣尝鲜JDK7的话可以试试（http://dlc.sun.com.edgesuite.net/jdk7/binaries/index.html），
			 * 当前版本是build 103；正式的JDK7今年10月份应该有指望吧。
			 * 在Windows版JDK里带上SA的相关bug是：
		     * Bug 6743339: Enable building sa-jdi.jar and sawindbg.dll on Windows with hotspot build(http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6743339)
			 * Bug 6755621: Include SA binaries into Windows JDK(https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6755621)
			 * 前面废话了那么多，接下来回到正题，介绍一下ClassDump工具。
		     * SA自带了一个能把当前在HotSpot中加载了的类dump成Class文件的工具，称为ClassDump。它的全限定类名是sun.jvm.hotspot.tools.jcore.ClassDump，有main()
		     * 方法，可以直接从命令行执行；接收一个命令行参数，是目标Java进程的进程ID，可以通过JDK自带的jps工具查找Java进程的ID。要执行该工具需要确保SA的JAR包在classpath上，位于$JAVA_HOME/lib/sa-jdi.jar。
			 * 默认条件下执行ClassDump会把当前加载的所有Java类都dump到当前目录下，如果有全限定名相同但内容不同的类同时存在于一个Java进程中，
			 * 那么dump的时候会有覆盖现象，实际dump出来的是同名的类的最后一个（根据ClassDump工具的遍历顺序）。
			 * 如果需要指定被dump的类的范围，可以自己写一个过滤器，在启动ClassDump工具时指定-Dsun.jvm.hotspot.tools.jcore.filter=filterClassName，
			 * 具体方法见下面例子；如果需要指定dump出来的Class文件的存放路径，可以用-Dsun.jvm.hotspot.tools.jcore.outputDir=path来指定，path替换为实际路径。
			 * 以下演示在Linux上进行。大家或许已经知道，Sun JDK对反射调用方法有一些特别的优化，会在运行时生成专门的“调用类”来提高反射调用的性能。这次演示就来看看生成的类是什么样子的
			 * 首先编写一个自定义的过滤器。只要实现sun.jvm.hotspot.tools.jcore.ClassFilter接口即可
			 * 
			 * 这次就写到这里吧～
			 *A. Sundararajan有篇不错的文章也是讲如何从Java进程dump出Class文件的，使用的是JVMTI系的API：
	 		 * Retrieving .class files from a running app
		     * 然后也有一篇使用SA从core dump文件中dump出Class文件的文章：
	         * Retrieving .class files from a Java core dump
	         * 
	         * 问: 我用windows下操作的时候 报了一个异常："please start SwDbgSrv.exe" 看了windows的services也没有这个服务  不知道是不是要加载什么类呢？
			 * 答: Serviceability Agent在Windows上一直杯具。
			 * 如果你在用JDK6的话那没指望，完全用不了SA。
			 * 如果你想试用JDK7的话，那可以试试自己用OpenJDK7的源码build一个完整的OpenJDK出来，然后到hotspot/agent/make目录里去根据那边的README.txt提示来把SA的一些JAR包build出来，然后那边就会生成出SwDbgSrv.exe出来。然后试试把它注册成NT服务，重启机器，再试试能不能连接吧。
			 *	顺带一说，我以前试过但是没成功…源码里有些地方会编译不过去，修改之后总算能build出SwDbgSrv.exe，但注册成服务却没反应。我肯定是有什么步骤没弄对…
			 * 但是我决定不折磨自己，回到Linux上用SA。反正我自己的机器和公司的服务器都是Linux的，现在就只有公司的工作机上还装着Windows…
	         * Serviceability Agent:http://openjdk.java.net/groups/hotspot/docs/Serviceability.html
	         * OpenJdk:http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/2d585507a41b/src/share/classes/java/lang/Class.java
	         * 在运行时生成专门的“调用类”来提高反射调用的性能：https://www.iteye.com/blog/rednaxelafx-548536
			 */
			Method priStr = ReflectTest.class.getDeclaredMethod("priStr");
			priStr.setAccessible(true);//设置为true就可以访问private属性
			for (int i=0; i<20; i++) {
				String s = priStr.invoke(null).toString();
				System.out.println(s + i);
			}
			TimeUnit.SECONDS.sleep(1000000000);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static String priStr(){
		return "private string";
	}
}
