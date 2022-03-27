package com.yale.test.java.fanshe.perfma.zhihu;

/*
 * https://www.zhihu.com/question/51244545/answer/126055789
 * Java 中, 为什么一个对象的实例方法在执行完成之前其对象可以被 GC 回收?
 * RednaxelaFX 的回答
 * http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/util/concurrent/Executors.java#l677 《 Executors$DelegatedExecutorService.submit() 》
 */
public class FinalizeGC {
	public static int foo() {
		A a = new A();
		return a.val();
	}
	
	/*
	 * 再看一组例子来帮助同学们热身。这跟题主最初举例问的问题不完全一致，但是简单一些好理解其性质。然后再去分析完整的例子就比较容易。
	 * 本例的关注点在于：从Test.foo()出发，为何能调用A.val()在里面能遇到NullPointerException。
	 * 我在我的Mac OS X上跑Oracle JDK8u101，得到的结果是：
	 * NPE 发生在第259次循环
	 * java.lang.NullPointerException
		at com.yale.test.java.fanshe.perfma.zhihu.B.val(B.java:16)
		at com.yale.test.java.fanshe.perfma.zhihu.A.val(A.java:16)
		at com.yale.test.java.fanshe.perfma.zhihu.FinalizeGC.foo(FinalizeGC.java:10)
		at com.yale.test.java.fanshe.perfma.zhihu.FinalizeGC.main(FinalizeGC.java:21)
	 * “按道理说”A.val()是个实例方法，它在执行的时候它的“this”肯定是活着的对不对？
	 * ——上面我们已经讨论过了，不对。“this”没用了就可以领便当了。此例正是如此。
	 * 这个例子有若干种方式可以触发到那个NPE，例如说既可以以Test.foo()为根来JIT编译，也可以以A.val()为根，都可以达到同样的效果。
	 * 首先，Test.foo()并没有持续持有A类实例的引用，在new A()之后就直接调用它上面的实例方法，把刚创建出来的A类实例的引用当作隐式“this”参数传递给A.val()了。所以在GC发生的时候，Test.foo()已甩锅——那个A类实例是活是死不关我事。
	 * 然后我们看看A.val()是怎么回事。
	 * 为了简化讨论，先来看完全不开内联时的情况。给启动参数加上 -XX:CompileCommand=dontinline,*,* 可以禁用所有方法的内联（除了intrinsic以外），然后加上 -XX:-BackgroundCompilation 会禁用HotSpot VM默认开启的后台编译功能——这样触发JIT编译后总是会等到编译结束再继续跑，而不是边继续在解释器里跑程序边等编译。禁用后台编译可以让实验输出结果更加稳定。
	 * java -XX:CompileCommand=dontinline,*,* -XX:+PrintCompilation -XX:-BackgroundCompilation Test
	 * 可以发现这个实验总是到A.val()被JIT编译后就抛NPE。还可以更激进地配置参数，只允许A.val()被JIT编译（也不允许A.val()内联任何其它方法），也可以达到同样的效果：
	 * java -XX:CompileCommand=compileonly,A,val -XX:+PrintCompilation -XX:-BackgroundCompilation Test
	 * 显然，A.val()在解释器里跑的时候，这个实验就不发生NPE。
	 * 这是因为在HotSpot VM的解释器里跑的时候，“this”参数总是在局部变量表里，而解释器不会对代码做活跃分析（liveness analysis），这个“this”参数会在A.val()解释执行过程中一直被当作是活的。而HotSpot VM的GC在扫描A.val()的解释器栈帧时，借助GenerateOopMap对代码做的分析又不做完整的活跃分析，导致GC把这个“this”看作一个根集合里的活的强引用。
	 * 附上 -XX:+TraceNewOopMapGenerationDetailed 的输出，可见“this”（在slot 0）确实在A.val()中从头到为都被解释器对应的GenerateOopMap认为是活的。
	 */
	public static void main(String[] args) {
		for (int i=0; i<2000; i++) {
			try {
				foo();
			} catch(NullPointerException npe) {
				System.out.println("NPE 发生在第" + i + "次循环");
				npe.printStackTrace(System.out);
				return;
			}
		}
	}
}
