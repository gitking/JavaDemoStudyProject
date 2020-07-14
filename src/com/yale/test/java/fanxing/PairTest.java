package com.yale.test.java.fanxing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairTest {
	public static void main(String[] args) {
		Pair<Number> p1 = new Pair<>(12.3, 4.56);
		Pair<Integer> p2 = new Pair<>(123, 456);
		setSame(p1, 100);
		System.out.println(p1.getFirst() + "," + p1.getLast());
		setSame(p2, 200);
		System.out.println(p2.getFirst() + "," + p2.getLast());
		
		int sum = PairTest.addNum(new Pair<Number>(1, 2));//注意：传入的类型是Pair<Number>，实际参数类型是(Integer, Integer)。
		System.out.println("sum:" + sum);
		
		//int res = PairTest.addNum(new Pair<Integer>(1, 2));编译错误，因为Pair<Integer>跟Pair<Number>没有任何关系
		//System.out.println(res);

	}
	
	static int addNum(Pair<Number> p) {
		Number first = p.getFirst();
		Number last = p.getLast();
		return first.intValue() + last.intValue();
	}
	
	
	/*
	 * 使用Pair<? extends Number>使得方法接收所有泛型类型为Number或Number子类的Pair类型
	 * 这样一来，给方法传入Pair<Integer>类型时，它符合参数Pair<? extends Number>类型。
	 * 这种使用<? extends Number>的泛型定义称之为上界通配符（Upper Bounds Wildcards），即把泛型类型T的上界限定在Number了。
	 * 除了可以传入Pair<Integer>类型，我们还可以传入Pair<Double>类型，Pair<BigDecimal>类型等等，
	 * 因为Double和BigDecimal都是Number的子类。
	 */
	static int add(Pair<? extends Number> p) {
		//如果我们考察对Pair<? extends Number>类型调用getFirst()方法，实际的方法签名变成了：
		//即返回值是Number或Number的子类，因此，可以安全赋值给Number类型的变量：
		Number first = p.getFirst();
		Number last = p.getLast();
		//然而，我们不可预测实际类型就是Integer，例如，下面的代码是无法通过编译的：
		//Integer sd = p.getFirst();
		//这是因为实际的返回类型可能是Integer，也可能是Double或者其他类型，
		//编译器只能确定类型一定是Number的子类（包括Number类型本身），但具体类型无法确定。
		//p.setFirst(new Integer(first.intValue() + 100));编译报错
		/*
		 * 编译错误发生在p.setFirst()传入的参数是Integer类型。有些童鞋会问了，既然p的定义是Pair<? extends Number>，那么setFirst(? extends Number)为什么不能传入Integer？
		 * 原因还在于擦拭法。如果我们传入的p是Pair<Double>，显然它满足参数定义Pair<? extends Number>，然而，Pair<Double>的setFirst()显然无法接受Integer类型。
		 * 这就是<? extends Number>通配符的一个重要限制：方法参数签名setFirst(? extends Number)无法传递任何Number类型给setFirst(? extends Number)。
		 * 这里唯一的例外是可以给方法参数传入null：
		 */
		p.setFirst(null);
		return first.intValue() + last.intValue();
	}
	/*
	 * 和extends通配符相反，这次，我们希望接受Pair<Integer>类型，以及Pair<Number>、Pair<Object>，
	 * 因为Number和Object是Integer的父类，setFirst(Number)和setFirst(Object)实际上允许接受Integer类型。
	 * 我们使用super通配符来改写这个方法：
	 * 注意到Pair<? super Integer>表示，方法参数接受所有泛型类型为Integer或Integer父类的Pair类型。
	 */
	static void setSame(Pair<? super Integer> p, Integer n){
		/*
		 * 考察Pair<? super Integer>的setFirst()方法，它的方法签名实际上是：
		 * void setFirst(? super Integer);
		 */
		p.setFirst(n);
		/*
		 * 这里注意到我们无法使用Integer类型来接收getFirst()的返回值，即下面的语句将无法通过编译：
		 * 注意：虽然Number是一个抽象类，我们无法直接实例化它。但是，即便Number不是抽象类，这里仍然无法通过编译。此外，传入Pair<Object>类型时，编译器也无法将Object类型转型为Integer。
		 * Integer x =  p.getFirst();
		 * 唯一可以接收getFirst()方法返回值的是Object类型：
		 * 因此，使用<? super Integer>通配符表示：
		         允许调用set(? super Integer)方法传入Integer的引用；
	   	         不允许调用get()方法获得Integer的引用。
		          唯一例外是可以获取Object的引用：Object o = p.getFirst()。
		         换句话说，使用<? super Integer>通配符作为方法参数，表示方法内部代码对于参数只能写，不能读
		         对比extends和super通配符
			我们再回顾一下extends通配符。作为方法参数，<? extends T> 类型和<? super T>类型的区别在于：
			    <? extends T>允许调用读方法T get()获取T的引用，但不允许调用写方法set(T)传入T的引用（传入null除外）；
			    <? super T>允许调用写方法set(T)传入T的引用，但不允许调用读方法T get()获取T的引用（获取Object除外）
		 */
		Object obj = p.getFirst();
		p.setLast(n);
		
		/*
		 * 可以看下Collections.copy的源码,
		 * 它的作用是把一个List的每个元素依次添加到另一个List中。它的第一个参数是List<? super T>，表示目标List，第二个参数List<? extends T>，
		 * 表示要复制的List。我们可以简单地用for循环实现复制。在for循环中，我们可以看到，对于类型<? extends T>的变量src，
		 * 我们可以安全地获取类型T的引用，而对于类型<? super T>的变量dest，我们可以安全地传入T的引用。
		 * 这个copy()方法的定义就完美地展示了extends和super的意图：
	     * copy()方法内部不会读取dest，因为不能调用dest.get()来获取T的引用；
	     * copy()方法内部也不会修改src，因为不能调用src.add(T)。
		 * 这是由编译器检查来实现的。如果在方法代码中意外修改了src，或者意外读取了dest，就会导致一个编译错误：
		 * 这个copy()方法的另一个好处是可以安全地把一个List<Integer>添加到List<Number>，但是无法反过来添加：
		 * 而这些都是通过super和extends通配符，并由编译器强制检查来实现的。
		 * PECS原则
		 * 何时使用extends，何时使用super？为了便于记忆，我们可以用PECS原则：Producer Extends Consumer Super。
		 * 即：如果需要返回T，它是生产者（Producer），要使用extends通配符；如果需要写入T，它是消费者（Consumer），要使用super通配符。
		 * 还是以Collections的copy()方法为例：
		 * 需要返回T的src是生产者，因此声明为List<? extends T>，需要写入T的dest是消费者，因此声明为List<? super T>。
		 */
		List<String> src = new ArrayList<String>();
		List<String> dest = new ArrayList<String>();
		Collections.copy(dest, src);
	}
	
	/*
	 * 无限定通配符
	 * 我们已经讨论了<? extends T>和<? super T>作为方法参数的作用。实际上，Java的泛型还允许使用无限定通配符（Unbounded Wildcard Type），即只定义一个?：
	 * 因为<?>通配符既没有extends，也没有super，因此：
	 * 不允许调用set(T)方法并传入引用（null除外）；
	 * 不允许调用T get()方法并获取T引用（只能获取Object引用）
	 * 换句话说，既不能读，也不能写，那只能做一些null判断：
	 * <?>通配符有一个独特的特点，就是：Pair<?>是所有Pair<T>的超类：
	 */
	static boolean sample(Pair<?> p) {
		Pair<Integer> pt = new Pair<>(123, 456);
		Pair<?> pj = pt; // 安全地向上转型,因为Pair<Integer>是Pair<?>的子类，可以安全地向上转型。
		System.out.println(pj.getFirst() + "," + pj.getLast());
		//String s = p.getFirst();
		//p.setFirst("");
	    return p.getFirst() == null || p.getLast() == null;
	}
	
	//大多数情况下，可以引入泛型参数<T>消除<?>通配符：
	//无限定通配符<?>很少使用，可以用<T>替换，同时它是所有<T>类型的超类。
	static <T> boolean isNull(Pair<T> p) {
		Pair<Integer> pt = new Pair<>(123, 456);
		Pair<?> pj = pt; // 安全地向上转型,因为Pair<Integer>是Pair<?>的子类，可以安全地向上转型。
		System.out.println(pj.getFirst() + "," + pj.getLast());
		//String s = p.getFirst();
		//p.setFirst("");
		return p.getFirst() == null || p.getLast() == null;
	}
}
