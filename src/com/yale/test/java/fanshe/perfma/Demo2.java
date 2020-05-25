package com.yale.test.java.fanshe.perfma;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 为什么java中反射在调用方法超过15次时会生成类调用？
 * https://www.iteye.com/blog/rednaxelafx-727938
 * https://www.iteye.com/blog/rednaxelafx-548536
 * https://club.perfma.com/question/507734?from=timeline#/question/507734?from=timeline
 * @author dell
 */
public class Demo2 {
	public static void main(String[] args) {
		try {
			Method method = System.out.getClass().getMethod("println", String.class);
			for (int i=0; i<16; i++) {
				method.invoke(System.out, "demo" + i);
				//其实这里的invoke方法最终调用的是NativeMethodAccessorImpl类里面的invoke方法
				//
			}
			System.in.read();//把线程阻塞在这里
			
			/**
			 * https://www.iteye.com/blog/rednaxelafx-548536
			 * https://www.iteye.com/blog/rednaxelafx-477934
			 * 我们来重点关注invoke方法,通过invoke的源码可以看到,invoke将反射调用委托给sun.reflect.MethodAccessor类了
			 * 那么Method类中的属性MethodAccessor是在哪里复制的呢？是通过方法acquireMethodAccessor复制的。
			 * acquireMethodAccessor方法中是通过sun.reflect.ReflectionFactory类的方法newMethodAccessor(this)返回一个MethodAccessor对象
			 * 看ReflectionFactory.newMethodAccessor的源码就有意思了
			 * 这里就可以看到有趣的地方了。如注释所述，实际的MethodAccessor实现有两个版本，一个是Java实现的，另一个是native code实现的。
			 * Java实现的版本在初始化时需要较多时间，但长久来说性能较好；native版本正好相反，启动时相对较快，但运行时间长了之后速度就比不过Java版了。这是HotSpot的优化方式带来的性能特性，
			 * 同时也是许多虚拟机的共同点：跨越native边界会对优化有阻碍作用，它就像个黑箱一样让虚拟机难以分析也将其内联，于是运行时间长了之后反而是托管版本的代码更快些。
			 * 为了权衡两个版本的性能，Sun的JDK使用了“inflation”的技巧：让Java方法在被反射调用时，开头若干次使用native版，
			 * 等反射调用次数超过阈值时则生成一个专用的MethodAccessor实现类，生成其中的invoke()方法的字节码，以后对该Java方法的反射调用就会使用Java版。
			 * Sun的JDK是从1.4系开始采用这种优化的，主要作者是Ken Russell(http://blogs.sun.com/kbr/entry/brief_introduction)
			 * 上面看到了ReflectionFactory.newMethodAccessor()生产MethodAccessor的逻辑，在“开头若干次”时用到的DelegatingMethodAccessorImpl代码如下：
			 * 这是一个间接层，方便在native与Java版的MethodAccessor之间实现切换。
			 * 然后下面就是native版MethodAccessor的Java一侧的声明： sun.reflect.NativeMethodAccessorImpl：
			 * 每次NativeMethodAccessorImpl.invoke()方法被调用时，都会增加一个调用次数计数器，看超过阈值没有；一旦超过，则调用MethodAccessorGenerator.generateMethod()
			 * 来生成Java版的MethodAccessor的实现类，并且改变DelegatingMethodAccessorImpl所引用的MethodAccessor为Java版。
			 * 后续经由DelegatingMethodAccessorImpl.invoke()调用到的就是Java版的实现了。
			 * 注意到关键的NativeMethodAccessorImpl.invoke0()方法是个native方法。它在HotSpot VM里是由JVM_InvokeMethod()函数所支持的：
			 * 
			 * 回到Java的一侧。MethodAccessorGenerator长啥样呢？由于代码太长，这里就不完整贴了，有兴趣的可以到OpenJDK 6的Mercurial仓库看：OpenJDK 6 build 17的MethodAccessorGenerator(http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/b202c79afde3/src/share/classes/sun/reflect/MethodAccessorGenerator.java)。
			 * 它的基本工作就是在内存里生成新的专用Java类，并将其加载。就贴这么一个方法：
			 * 
			 * 去阅读源码的话，可以看到MethodAccessorGenerator是如何一点点把Java版的MethodAccessor实现类生产出来的。
			 * 也可以看到GeneratedMethodAccessor+数字这种名字是从哪里来的了，就在上面的generateName()方法里。
			 * 对本文开头的例子的A.foo()，生成的Java版MethodAccessor大致如下：
		     * 就反射调用而言，这个invoke()方法非常干净（然而就“正常调用”而言这额外开销还是明显的）。注意到参数数组被拆开了，把每个参数都恢复到原本没有被Object[]包装前的样子，然后对目标方法做正常的invokevirtual调用。由于在生成代码时已经循环遍历过参数类型的数组，生成出来的代码里就不再包含循环了。
			 * 当该反射调用成为热点时，它甚至可以被内联到靠近Method.invoke()的一侧，大大降低了反射调用的开销。而native版的反射调用则无法被有效内联，因而调用开销无法随程序的运行而降低。
			 * 虽说Sun的JDK这种实现方式使得反射调用方法成本比以前降低了很多，但Method.invoke()本身要用数组包装参数；而且每次调用都必须检查方法的可见性（在Method.invoke()里），也必须检查每个实际参数与形式参数的类型匹配性（在NativeMethodAccessorImpl.invoke0()里或者生成的Java版MethodAccessor.invoke()里）；而且Method.invoke()就像是个独木桥一样，各处的反射调用都要挤过去，在调用点上收集到的类型信息就会很乱，影响内联程序的判断，使得Method.invoke()自身难以被内联到调用方。
			 * 相比之下JDK 7里新的MethodHandle(https://www.iteye.com/blog/rednaxelafx-477934)则更有潜力，在其功能完全实现后能达到比普通反射调用方法更高的性能。在使用MethodHandle来做反射调用时，MethodHandle.invoke()的形式参数与返回值类型都是准确的，所以只需要在链接方法的时候才需要检查类型的匹配性，而不必在每次调用时都检查。而且MethodHandle是不可变值，在创建后其内部状态就不会再改变了；JVM可以利用这个知识而放心的对它做激进优化，例如将实际的调用目标内联到做反射调用的一侧。
			 * 到本来Java的安全机制使得不同类之间不是任意信息都可见，但Sun的JDK里开了个口，有一个标记类专门用于开后门：MagicAccessorImpl
			 */
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
