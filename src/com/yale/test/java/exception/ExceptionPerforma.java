package com.yale.test.java.exception;
/*
 * https://www.heapdump.cn/article/2573808 太扯了！异常信息突然就没了。
 * https://www.heapdump.cn/article/2624569 获取异常信息里再出异常就找不到日志了，我人傻了
 * https://www.heapdump.cn/article/2615303 卷向字节码-Java异常到底是怎么被处理的？
 * https://mp.weixin.qq.com/s/2A_jhF4a31t8_us8v1zbsQ  神了！异常信息突然就没了？
 * https://mp.weixin.qq.com/s/aOlScg5sfQ4JvHzcRFqeyQ  关于多线程中抛异常的这个面试题我再说最后一次！
 * 为啥？这事就得从 2004 年讲起了。那一年，SUN 公司于 9 月 30 日 18 点发布了 JDK 5。在其 release-notes 中有这样一段话：
 * https://www.oracle.com/java/technologies/javase/release-notes-introduction.html
 * The compiler in the server VM now provides correct stack backtraces for all "cold" built-in exceptions. For performance purposes, 
 * when such an exception is thrown a few times, the method may be recompiled. After recompilation, the compiler may choose a faster tactic using preallocated exceptions that do not provide a stack trace. To disable completely the use of preallocated exceptions, use this new flag: -XX:-OmitStackTraceInFastThrow.
 * 服务器 VM 中的编译器现在为所有“冷”内置异常提供正确的堆栈回溯。 出于性能考虑，当多次抛出此类异常时，可能会重新编译该方法。 重新编译后，编译器可以使用不提供堆栈跟踪的预分配异常选择更快的策略。 
 * 要完全禁用预分配异常，请使用此新标志：-XX:-OmitStackTraceInFastThrow。
 * 它最后提到了一个参数 -XX:-OmitStackTraceInFastThrow，二话不说，先拿来用了，看看效果再说：
 * 不过这个-XX:-OmitStackTraceInFastThrow参数现在只有特定的几种异常类型会折叠异常，怎么设置其他异常也可以自动折叠呢？答:只有自带的几个异常可以，其他异常不行
 * NullPointerException,ArithmeticException,ArrayIndexOutOfBoundsException,ArrayStoreException,ClassCastException
 * 使用这个参数-XX:-OmitStackTraceInFastThrow之后(其实就是将这种优化禁用掉了),不过循环多少次都会将完整的堆栈信息打印出来.默认这个优化市打开的,因为处于性能问题的考虑。
 * 有什么性能问题呢,可以看Throwable.class类里面的public synchronized Throwable fillInStackTrace()方法,还有构造方法
 * 其实fillInStackTrace()最终会调用fillInStackTrace(0);这个native方法, fill In Stack Trace，顾名思义，填入堆栈跟踪。private native Throwable fillInStackTrace(int dummy);
 * 这个方法会去爬堆栈，而这个过程就是一个相对比较消耗性能的过程。
 * 更加深入一点的研究对比，你可以看看这个链接：http://java-performance.info/throwing-an-exception-in-java-is-very-slow
 * 重载 Throwable.fillInStackTrace() 方法以提高Java性能这样的做法对吗？ https://www.zhihu.com/question/21405047
 * 大家都不约而同的提到了重写 fillInStackTrace 方法，这个性能优化小技巧，也就是我们可以这样去自定义异常：
 * 重写了 fillInStackTrace 方法，直接返回 this 的对象，比调用了爬栈方法的原始方法，快了不是一星半点儿。其实除了重写 fillInStackTrace 方法之外，JDK 7 之后还提供了这样的一个方法：
 * protected Throwable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)可以通过writableStackTrace这个参数来控制是否使用fillInStackTrace方法.
 * 可以通过 writableStackTrace 入参来控制是否需要去爬栈。那么到底什么时候才应该去用这样的一个性能优化手段呢？其实R大的回答里面说的很清楚了：
 * > 但注意好前提就是了——要确定真的肯定绝对不需要stack trace的话。一个简单的例子是，“滥用”异常来实现某些特殊控制流结构的场景，此时stack trace肯定是没用的，那个异常对象本身其实也没用，只有它的类型和抛出它带来的控制流跳转才有用，那就应该覆写fillInStackTrace()。
 * 其实我们写业务代码的，异常信息打印还是非常有必要的。但是对于一些追求性能的框架，就可以利用这个优势。比如我在 disruptor 和 kafka 的源码里面都找到了这样的优化落地源码。先看 disruptor 的：
 * com.lmax.disruptor.AlertException,这个类重写了fillInStackTrace()方法,方法上面的注释为:Overridden so the stack trace is not filled in for this exception for performance reasons.由于性能的原因，重载后的堆栈跟踪不会被填入这个异常。
 * 再看 kafka 的：
 * org.apache.kafka.common.errors.ApiException,这个类重写了fillInStackTrace()方法,方法上面的注释为:avoid the expensive and useless stack trace for api exceptions避免对api异常进行昂贵而无用的堆栈跟踪
 * 而且你注意到了吗，上面着两个框架中，直接把 synchronized 都干掉了。如果你也打算重写，那么也可以分析一下你的场景中是否可以去掉 synchronized，性能又可以来一点提升。另外，R大的回答里面还提到了这个优化是 C2 的优化。我们可以简单的证明一下。
 * 分层编译
 * 前面提到的 C2，其实还有一个对应的 C1。这里说的 C1、C2 都是即时编译器。你要是不熟悉 C1、C2，那我换个说法。
 * C1 其实就是 Client Compiler，即客户端编译器，特点是编译时间较短但输出代码优化程度较低。
 * C2 其实就是 Server Compiler，即服务端编译器，特点是编译耗时长但输出代码优化质量也更高。
 * 大家常常提到的 JVM 帮我们做的很多“激进”的为了提升性能的优化，比如内联、快慢速路径分析、窥孔优化，包括本文说的“不显示异常堆栈”，都是 C2 搞的事情。
 * 多说一句，在 JDK 10 的时候呢，又推出了 Graal 编译器，其目的是为了替代 C2。至于为什么要替换 C2，额，原因之一是这样的…http://icyfenix.cn/tricks/2020/graalvm/graal-compiler.html
 * C2 的历史已经非常长了，可以追溯到 Cliff Click 大神读博士期间的作品，这个由 C++ 写成的编译器尽管目前依然效果拔群，但已经复杂到连 Cliff Click 本人都不愿意继续维护的程度。
 * 你看前面我说的 C1、C1 的特点，刚好是互补的。所以为了在程序启动、响应速度和程序运行效率之间找到一个平衡点，在 JDK 6 之后，JVM 又支持了一种叫做分层编译的模式。也是为什么大家会说：“Java 代码运行起来会越来越快、Java 代码需要预热”的根本原因和理论支撑。
 * 在这里，我引用《深入理解Java虚拟机HotSpot》一书中 7.2.1 小节[分层编译]的内容，让大家简单了解一下这是个啥玩意。
 * 首先，我们可以使用 -XX:+TieredCompilation 开启分层编译，它额外引入了四个编译层级。
 * 第 0 级：解释执行。
 * 第 1 级：C1 编译，开启所有优化（不带 Profiling）。Profiling 即剖析。
 * 第 2 级：C1 编译，带调用计数和回边计数的 Profiling 信息（受限 Profiling).
 * 第 3 级：C1 编译，带所有Profiling信息（完全Profiling).
 * 第 4 级：C2 编译。
 * 常见的分层编译层级转换路径如下图所示：
 * 0→3→4：常见层级转换。用 C1 完全编译，如果后续方法执行足够频繁再转入 4 级。
 * 0→2→3→4：C2 编译器繁忙。先以 2 级快速编译，等收集到足够的 Profiling 信息后再转为3级，最终当 C2 不再繁忙时再转到 4 级。
 * 0→3→1/0→2→1：2/3级编译后因为方法不太重要转为 1 级。如果 C2 无法编译也会转到 1 级。
 * 0→(3→2)→4：C1 编译器繁忙，编译任务既可以等待 C1 也可以快速转到 2 级，然后由 2 级转向 4 级。
 * 如果你之前不知道分层编译这回事，没关系，现在有这样的一个概念就行了。面试不会考的，放心。接下来，就要提到一个参数了：-XX:TieredStopAtLevel=___
 * 看名字你也知道了，这个参数的作用是让分层编译停在某一层，默认值为 4，也就是到 C2 编译。那我把该值修改为 -XX:TieredStopAtLevel=3，岂不是就只能用 C1 了，那就不能利用 C2 帮我优化异常啦？果然如此，R大诚不欺我。
 * 
 * R大的回答:然后从反面举个例子。HotSpot VM有个许多人觉得“匪夷所思”的优化，叫做fast throw：有些特定的隐式异常类型（NullPointerException、ArithmeticException（ / 0）之类）如果在代码里某个特定位置被抛出过多次的话，
 * HotSpot Server Compiler（C2）会透明的决定用fast throw来优化这个抛出异常的地方——直接抛出一个事先分配好的、类型匹配的异常对象。这个对象的message和stack trace都被清空。
 * 抛出这个异常的速度是非常快，不但不用额外分配内存，而且也不用爬栈；但反面就是可能正好是需要知道哪里出问题的时候看不到stack trace了。
 * 从Sun JDK5开始要避免C2做这个优化还得额外传个VM参数：-XX:-OmitStackTraceInFastThrow。覆写fillInStackTrace()为直接返回this就像是人肉做C2所做的那种优化…的一部分效果。
 * 反正肯定会有人抱怨这样不好的啦，是不是要顶住压力硬上就看到底在特定场景里带来的性能好处是不是真的那么重要了。
 */
public class ExceptionPerforma {
	public static void main(String[] args) {
		String msg = null;
		long startTime = System.currentTimeMillis();
		for (int i=0;i<500000;i++) {
			try {
				/*
				 * 循环报空指针异常500000次。你会发现刚开始报错的时候会把堆栈信息都打印出来，像这样：
				 * java.lang.NullPointerException
				 * at com.yale.test.java.exception.ExceptionPerforma.main(ExceptionPerforma.java:14)
				 * 等报错的次数太多了,就会变成下面这种没有异常堆栈信息的样子,看不出来到底是第几行代码报错了.在抛出一定次数的空指针异常后，异常堆栈没了。
				 * java.lang.NullPointerException
				 * 使用这个参数-XX:-OmitStackTraceInFastThrow之后(其实就是将这种优化禁用掉了),不过循环多少次都会将完整的堆栈信息打印出来.默认这个优化市打开的,因为处于性能问题的考虑。
				 */
				msg.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long takeTime = System.currentTimeMillis() - startTime;
		//在不加-XX:-OmitStackTraceInFastThrow参数的时候,500000次空指针异常,花费时间5047毫秒,2021年7月24日14:49:34
		//右键->Run as -> Run Configurations - > Arguments ->VM arguments里面加上-XX:-OmitStackTraceInFastThrow参数的时候,500000次空指针异常,花费时间9686毫秒,2021年7月24日14:49:34
		//-XX:TieredStopAtLevel=3 使用这个参数也可以达到同样的效果，右键->Run as -> Run Configurations - > Arguments ->VM arguments里面加上-XX:TieredStopAtLevel=3,使用这个参数花费时间12654毫秒,2021年7月24日14:49:34
		System.out.println("花费时间" + takeTime + "毫秒");
	}
}
