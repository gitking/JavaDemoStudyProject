package com.yale.test.design.structural.adapter.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/*
 * 适配器
 * 将一个类的接口转换成客户希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * 适配器模式是Adapter，也称Wrapper，是指如果一个接口需要B接口，但是待传入的对象却是A接口，怎么办？
 * 我们举个例子。如果去美国，我们随身带的电器是无法直接使用的，因为美国的插座标准和中国不同，所以，我们需要一个适配器：
 * 在程序设计中，适配器也是类似的。我们已经有一个Task类，实现了Callable接口：
 */
public class Task implements Callable<Long>{
	private long num;
	
	public Task(long num) {
		this.num = num;
	}
	
	@Override
	public Long call() throws Exception {
		long r=0;
		for (long n=1; n<= this.num; n++) {
			r = r + n;
		}
		System.out.println("Result:" + r);
		return r;
	}
	
	public static void main(String[] args) throws InterruptedException {
		//现在，我们想通过一个线程去执行它：
		Callable<Long> callable = new Task(123456789L);
//		Thread thread = new Thread(callable);编译报错
		/*
		 * 发现编译不过！因为Thread接收Runnable接口，但不接收Callable接口，肿么办？
		 * 一个办法是改写Task类，把实现的Callable改为Runnable，但这样做不好，因为Task很可能在其他地方作为Callable被引用，改写Task的接口，会导致其他正常工作的代码无法编译。
		 * 另一个办法不用改写Task类，而是用一个Adapter，把这个Callable接口“变成”Runnable接口，这样，就可以正常编译：
		 * 这个RunnableAdapter类就是Adapter，它接收一个Callable，输出一个Runnable。怎么实现这个RunnableAdapter呢？我们先看完整的代码：
		 * 编写一个Adapter的步骤如下：
		 * 1.实现目标接口，这里是Runnable；
		 * 2.内部持有一个待转换接口的引用，这里是通过字段持有Callable接口；
		 * 3.在目标接口的实现方法内部，调用待转换接口的方法。
		 * 这样一来，Thread就可以接收这个RunnableAdapter，因为它实现了Runnable接口。Thread作为调用方，它会调用RunnableAdapter的run()方法，在这个run()方法内部，
		 * 又调用了Callable的call()方法，相当于Thread通过一层转换，间接调用了Callable的call()方法。
		 */
		Thread thread = new Thread(new RunnableAdapter(callable));
		thread.start();
		thread.join();
		
		/*
		 * 适配器模式在Java标准库中有广泛应用。比如我们持有数据类型是String[]，但是需要List接口时，可以用一个Adapter：
		 * 注意到List<T> Arrays.asList(T[])就相当于一个转换器，它可以把数组转换为List。
		 */
		String[] exist = new String[]{"Good", "morning", "Bob", "and", "Alice"};
		Set<String> set = new HashSet<String>(Arrays.asList(exist));
		
		/*
		 * 我们再看一个例子：假设我们持有一个InputStream，希望调用readText(Reader)方法，但它的参数类型是Reader而不是InputStream，怎么办？
		 * 当然是使用适配器，把InputStream“变成”Reader：
		 * InputStream input = Files.newInputStream(Paths.get("/path/to/file"));
		 * Reader reader = new InputStreamReader(input, "UTF-8");
		 * readText(reader);
		 * InputStreamReader就是Java标准库提供的Adapter，它负责把一个InputStream适配为Reader。类似的还有OutputStreamWriter。
		 * 如果我们把readText(Reader)方法参数从Reader改为FileReader，会有什么问题？这个时候，因为我们需要一个FileReader类型，就必须把InputStream适配为FileReader：
		 * FileReader reader = new InputStreamReader(input, "UTF-8"); // compile error!编译错误
		 * 直接使用InputStreamReader这个Adapter是不行的，因为它只能转换出Reader接口。事实上，要把InputStream转换为FileReader也不是不可能，但需要花费十倍以上的功夫。
		 * 这时，面向抽象编程这一原则就体现出了威力：持有高层接口readText(Reader)不但代码更灵活，而且把各种接口组合起来也更容易。一旦持有某个具体的子类类型，要想做一些改动就非常困难。
		 * 小结
		 * Adapter模式可以将一个A接口转换为B接口，使得新的对象符合B接口规范。
		 * 编写Adapter实际上就是编写一个实现了B接口，并且内部持有A接口的类：
		 * public BAdapter implements B {
			    private A a;
			    public BAdapter(A a) {
			        this.a = a;
			    }
			    public void b() {
			        a.a();
			    }
			}
		 * 在Adapter内部将B接口的调用“转换”为对A接口的调用。
		 * 只有A、B接口均为抽象接口时，才能非常简单地实现Adapter模式。
		 */
	}
}
