package com.yale.test.java.fanshe.perfma;

/**
 * https://www.zhihu.com/question/263528143/answer/270308453
 * 无论在哪种情况，即使是在第二种场景里，stop的可见性其实都没有得到保证。因为即使在第二种场景里，虽然子线程中有对volatile类型变量i的读写+非volatile类型变量stop的读，但是主线程中只有对非volatile类型变量stop的写入，
 * 因此无法建立 （主线程对stop的写) happens-before于 (子线程对stop的读) 的关系。因此你不能指望主线程对stop的写一定能被子线程看到。虽然场景二在实际运行时程序依然正确终止了，
 * 但是这个只能算是运气好，如果换一种JVM实现或者换一种CPU架构，可能场景二也会陷入死循环。可以设想这样的一个场景，主/子线程分别在core1/core2上运行，core1的cache中有stop的副本，core2的cache中有stop与i的副本，而且stop和i不在同一条cacheline里。core1修改了stop变量，但是由于stop不是volatile的，这个改动可以只发生在core1的cache里，而被修改的cacheline理论上可以永远不刷回内存，这样core2上的子线程就永远也看不到stop的变化了。
 * 我们可以试着分析一下程序实际执行时发生了什么。由于run方法里的while循环会被执行很多次，所以必然会触发jit编译下面来分析两种情况下jit编译后的结果（触发了多次jit编译，只贴出最后一次C2等级jit编译后的结果）
 * 一：i为run方法内的局部变量的情况：图片:https://pic2.zhimg.com/v2-d309ff1dd8762823a790c7fc41c50e91_r.jpg
 * 1.在第一个红框处检测stop变量，如果为true，那么跳转到L0001处继续执行（L0001处再往下走函数就退出了），但此时stop为false，所以不会走这个分支
 * 2.L0000，inc %ebp。也就是i++
 * 3.test %eax, -0x239864a(%rip)，轮询SAFEPOINT的操作，可以无视
 * 4.jmp L0000，无条件跳转回L0000处继续执行i++
 * 把jit编译后的代码改写回来，大概是这个样子if(!stop){
 *  while(true){
	          i++;
	    }
 *	}
 * 也就是说JVM觉得每次循环都去访问非volatile类型的stop变量太浪费了，就只在函数执行之初访问一次stop，后续无论stop变量怎么变，都不管了。第一种情况死循环就是这么来的。
 * 二：i为全局的volatile变量的情况：图片:https://pic3.zhimg.com/v2-b2bb9a95c7c88a4256a3f51178e60295_r.jpg?source=1940ef5c
 * 从第一个红框开始看：
 * 1.jmp L0001，无条件跳转到label L0001处
 * 2.movzbl 0x6c(%r10),%r8d;   访问static变量stop，并将其复制到寄存器r8d里
 * 3.test %r8d, %r8d; je L0000;  如果r8d里的值为0，跳转到L0000处，否则继续往下走（函数结束）
 * 4.L000: mov 0x68(%r10), %r8d; 访问static变量i，并将其复制到寄存器r8d里
 * 5.inc %r8d;  自增r8d里的值
 * 6.mov %r8d, 0x68(%r10); 将自增后r8d里的新值复制回static变量i中（上面三行是i++的流程）
 * 7.lock addl $0x0, (%rsp); 给rsp寄存器里的值加0，没有任何效果，关键在于前面的lock前缀，会导致cache line的刷新，从而实现变量i的volatile语义
 * 8.test %eax, -0x242a056(%rip);  轮询SAFEPOINT的操作，可以无视
 * 9.L0001，回到step 2
 * 也就是说，每次循环都会去访问一次stop变量，迟早会访问到stop被修改后的新值（不能确保），导致循环结束。
 * 这两种场景的区别主要在于第二种情况的循环中有对static volatile类型变量i的访问，导致jit编译时JVM无法做出激进的优化，是附加的效果。
 * ---------------------------------------------------
 * 分层编译
 * 前面提到的 C2，其实还有一个对应的 C1。这里说的 C1、C2 都是即时编译器。
	你要是不熟悉 C1、C2，那我换个说法。
	C1 其实就是 Client Compiler，即客户端编译器，特点是编译时间较短但输出代码优化程度较低。
	C2 其实就是 Server Compiler，即服务端编译器，特点是编译耗时长但输出代码优化质量也更高。
	大家常常提到的 JVM 帮我们做的很多“激进”的为了提升性能的优化，比如内联、快慢速路径分析、窥孔优化，包括本文说的“不显示异常堆栈”，都是 C2 搞的事情。
	多说一句，在 JDK 10 的时候呢，又推出了 Graal 编译器，其目的是为了替代 C2。
	至于为什么要替换 C2，额，原因之一是这样的…
	http://icyfenix.cn/tricks/2020/graalvm/graal-compiler.html
	C2 的历史已经非常长了，可以追溯到 Cliff Click 大神读博士期间的作品，这个由 C++ 写成的编译器尽管目前依然效果拔群，但已经复杂到连 Cliff Click 本人都不愿意继续维护的程度。<br>
	你看前面我说的 C1、C1 的特点，刚好是互补的。
	所以为了在程序启动、响应速度和程序运行效率之间找到一个平衡点，在 JDK 6 之后，JVM 又支持了一种叫做分层编译的模式。
	也是为什么大家会说：“Java 代码运行起来会越来越快、Java 代码需要预热”的根本原因和理论支撑。
	在这里，我引用《深入理解Java虚拟机HotSpot》一书中 7.2.1 小节[分层编译]的内容，让大家简单了解一下这是个啥玩意。
	首先，我们可以使用 -XX:+TieredCompilation 开启分层编译，它额外引入了四个编译层级。
	第 0 级：解释执行。
	第 1 级：C1 编译，开启所有优化（不带 Profiling）。Profiling 即剖析。
	第 2 级：C1 编译，带调用计数和回边计数的 Profiling 信息（受限 Profiling).
	第 3 级：C1 编译，带所有Profiling信息（完全Profiling).
	第 4 级：C2 编译。
	常见的分层编译层级转换路径如下图所示：
 * C1和C2的编译请见这篇文章:https://www.heapdump.cn/article/2573808
 * @author issuser
 */
public class VolatileBarrierExample2 {
	private static boolean stop = false;
	private static volatile int i = 0;
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				//int i = 0;//把这行代码的注释放开，代码就会死循环，注释掉就不会死循环
				while(!stop){
					i++;//注意这个i是全局变量
				}
			}
		});
		
		thread.start();//启动线程
		Thread.sleep(1000);//Main线程休眠1秒
		stop= true;//这个可以正常结束循环,VolatileBarrierExample01不会正常结束循环
		System.out.println("Main线程已经把stop修改成true了,子线程的循环会结束吗?");
		thread.join();
	}
}
