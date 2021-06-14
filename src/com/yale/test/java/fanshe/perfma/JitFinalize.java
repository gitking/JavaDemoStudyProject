package com.yale.test.java.fanshe.perfma;

/*
 * https://club.perfma.com/article/2462327 一个 println 竟然比 volatile 还好使？
 * https://club.perfma.com/question/2079981
 * JIT（Just-in-Time） 的优化
 * 众所周知，JAVA 为了实现跨平台，增加了一层 JVM，不同平台的 JVM 负责解释执行字节码文件。虽然有一层解释会影响效率，但好处是跨平台，字节码文件是平台无关的。
 * 在 JAVA 1.2 之后，增加了 即时编译（Just-in-Time Compilation，简称 JIT） 的机制，在运行时可以将执行次数较多的热点代码编译为机器码，这样就不需要 JVM 再解释一遍了，可以直接执行，增加运行效率。
 * 但 JIT 编译器在编译字节码时，可不仅仅是简单的直接将字节码翻译成机器码，它在编译的同时还会做很多优化，比如循环展开、方法内联等等……
 * JIT()优化之 提前置空
 * 这个提前回收的机制，还是有点风险的，在某些场景下会引起 BUG，比如《一个JDK线程池BUG引发的GC机制思考》https://juejin.cn/post/6844904004569464840#comment
 * JIT 编译器的优化技术之:
 * 强调一下，本文所说的编译器优化都是在JIT编译器中做的，而不是在Java源码到字节码的编译器（javac / ECJ之类）做的。并不是前端编译器不能做，只是javac不做而已。
 * 1,表达式提升（expression hoisting）
 * 2,表达式下沉（expression sinking）
 * 3,循环展开（Loop unwinding/loop unrolling）
 * 4,内联优化（Inling）
 * 5,表达式提升（expression hoisting）
 * 6,表达式提升（expression hoisting）
 * 7,表达式提升（expression hoisting）
 * 如何避免因 JIT 导致的问题？
 * 小伙伴：“JIT 这么多优化机制，很容易出问题啊，我平时写代码要怎么避开这些呢”
 * 平时在编码的时候，不用刻意的去关心 JIT 的优化，就比如上面那个VolatileDemo.java里面的println 问题，JMM 本来就不保证修改对其他线程可见，如果按照规范去加锁或者用 volatile 修饰，根本就不会有这种问题。
 * 而那个提前置空导致的问题，出现的几率也很低，只要你规范写代码基本不会遇到的。
 * 我：所以，这不是 JIT 的锅，是你的……
 * 小伙伴：“懂了，你这是说我菜，说我代码写的屎啊……”
 * 总结:
 * 在日常编码过程中，不用刻意的猜测 JIT 的优化机制，JVM 也不会完整的告诉你所有的优化。而且这种东西不同版本效果不一样，就算搞明白了一个机制，可能到下个版本就完全不一样了。
 * 所以，如果不是搞编译器开发的话，JIT 相关的编译知识，作为一个知识储备就好。
 * 也不用去猜测 JIT 到底会怎么优化你的代码，你（可能）猜不准……
 * 参考连接:
 * 《JSR-133 Java Memory Model and Thread Specification 1.0 Proposed Final Draft》https://jcp.org/en/jsr/detail?id=133
 * 《Oracle JVM Just-in-Time Compiler (JIT)》https://docs.oracle.com/en/database/oracle/oracle-database/19/jjdev/Oracle-JVM-JIT.html#GUID-9466BE4E-E7EE-486F-9DF8-D331B316359D
 * 《JVM JIT-compiler overview - Vladimir Ivanov HotSpot JVM Compiler Oracle Corp.》http://cr.openjdk.java.net/~vlivanov/talks/2015_JIT_Overview.pdf
 * 《JVM JIT optimization techniques - part 2》https://advancedweb.hu/jvm-jit-optimization-techniques-part-2/
 * 《The Java platform - WikiBook》https://en.wikibooks.org/wiki/Java_Programming/The_Java_Platform
 * 《R 大的知乎百科》https://www.zhihu.com/question/39458585/answer/81521474
 * 请问R大 有没有什么工具可以查看正在运行的类的c/汇编代码 https://hllvm-group.iteye.com/group/topic/34932#post-232535
 * X86 asm代码 是怎么才能看到？https://hllvm-group.iteye.com/group/topic/21769
 * 本例来自:https://stackoverflow.com/questions/24376768/can-java-finalize-an-object-when-it-is-still-in-scope
 * https://stackoverflow.com/questions/58714980/rejectedexecutionexception-inside-single-executor-service
 */
public class JitFinalize {
	
	@Override//对象被回收前,会触发finalize
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println(this + " was finalized! 我被GC回收掉了,这是因为JIT做了优化导致的.");
	}
	
	
	public static void main(String[] args) {
		JitFinalize jitFinalize = new JitFinalize();
		System.out.println("jitFinalize这个对象在创建出来之后除了这个println会把jitFinalize对象打印出来,就再也没有被使用过了" + jitFinalize);
		jitFinalize = null;//手动置null
		for (int i=0; i <1_000_000_000; i++) {
			if (i % 1_000_00 == 0) {
				System.gc();
			}
		}
		System.out.println("这行代码会在finalize()执行之后再执行,这是因为jit的优化导致的");
		/*
		 * 从例子中可以看到，如果a在循环完成后已经不再使用了，则会出现先执行finalize的情况；虽然从对象作用域来说，方法没有执行完，栈帧并没有出栈，但是还是会被提前执行。
		 * 现在来增加一行代码，在最后一行打印对象a，让编译器/代码生成器认为后面有对象a的引用
		 * System.out.println(jitFinalize);加上这行代码之后打印结果如下:
		 * >>jitFinalize这个对象在创建出来之后除了这个println会把jitFinalize对象打印出来,就再也没有被使用过了com.yale.test.java.fanshe.perfma.JitFinalize@15db9742
		 * >>这行代码会在finalize()执行之后再执行,这是因为jit的优化导致的
		 * >>com.yale.test.java.fanshe.perfma.JitFinalize@15db9742
		 * 从结果上看，finalize方法都没有执行（因为main方法执行完成后进程直接结束了），更不会出现提前finalize的问题了
		 * 基于上面的测试结果，再测试一种情况，在循环之前先将对象a置为null，并且在最后打印保持对象a的引用,打印结果如下:
		 * >> jitFinalize这个对象在创建出来之后除了这个println会把jitFinalize对象打印出来,就再也没有被使用过了com.yale.test.java.fanshe.perfma.JitFinalize@15db9742
		 * >> com.yale.test.java.fanshe.perfma.JitFinalize@15db9742 was finalized! 我被GC回收掉了,这是因为JIT做了优化导致的.
		 * >> 这行代码会在finalize()执行之后再执行,这是因为jit的优化导致的
		 * >> null
		 * 从结果上看，手动置null的话也会导致对象被提前回收，虽然在最后还有引用，但此时引用的也是null了
		 */
		System.out.println(jitFinalize);
	}
	
	/*
	 * JIT 还有那些常见优化？
	 * 表达式下沉（expression sinking）
	 * 和表达式提升类似的，还有个表达式下沉的优化，比如下面这段代码：
	 * 由于在 else 分支里，并没有使用 result 的值，可每次不管什么分支都会先计算 result，这就没必要了。JIT 会把 result 的计算表达式移动到 if 分支里，这样就避免了每次对 result 的计算，这个操作就叫表达式下沉：
	 */
	public void sinking(int i) {
		int result = 543 * i;
		if (i % 2 == 0) {
			//使用result值的一些代码逻辑
			//所以应该把int result = 543 * i;写到if里面,而不应该写到if外面
		} else {
			//一些不使用result的值的逻辑代码
		}
	}
	
	/*
	 * JIT 还有那些常见优化？
	 * 循环展开（Loop unwinding/loop unrolling）
	 * 下面这个 for 循环，一共要循环 10w 次，每次都需要检查条件。
	 * 在编译器的优化后，会删除一定的循环次数，从而降低索引递增和条件检查操作而引起的开销：
	 * for (int i = 0; i < 20000; i+=5) {
		    delete(i);
		    delete(i + 1);
		    delete(i + 2);
		    delete(i + 3);
		    delete(i + 4);
		}
	 * 除了循环展开，循环还有一些优化机制，比如循环剥离、循环交换、循环分裂、循环合并……
	 */
	public void loopUnwinding() {
		for (int i=0; i<20000; i++) {
			//delete(i);
		}
	}
	
	/*
	 * 内联优化（Inling）
	 * JVM 的方法调用是个栈的模型，每次方法调用都需要一个压栈（push）和出栈（pop）的操作，编译器也会对调用模型进行优化，将一些方法的调用进行内联。
	 * 内联就是抽取要调用的方法体代码，到当前方法中直接执行，这样就可以避免一次压栈出栈的操作，提升执行效率。比如下面这个方法：
	 * 在编译器内联优化后，会将 calculate 的方法体抽取到 inline 方法中，直接执行，而不用进行方法调用：
	 * public  void inline(){
			int a = 5;
		    int b = 10;
		    int c = a + b;
		    
		    // 使用 c 处理……
		}
	 * 不过这个内联优化是有一些限制的，比如 native 的方法就不能内联优化
	 */
	public void inline() {
		int a = 5;
		int b =10;
		int c = calculate(a, b);
		//下面是使用c的一些代码
	}
	
	public int calculate(int a, int b) {
		return a + b;
	}
}
