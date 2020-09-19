package com.yale.test.java.fanshe.perfma;

/**
 * https://club.perfma.com/question/267086
 * 按道理，子线程会通过num++操作破坏while循环的条件，从而终止循环，执行最后的输出操作。
 * 但在我的多次运行中，偶尔会出现while循环一直不结束的场合。像我截图一样，程序一直不终止。
 * JDK7、JDK8均已试验，均能偶然触发。
 * 答案由亮哥提供：https://www.zhihu.com/question/263528143/answer/270308453
 * 简单说来，是因为JIT激进编译导致的问题。
 * volatile只是恰巧阻止了JIT的激进编译，所以这里主要的问题不是可见性。因为哪怕变量不是volatile修饰，只要加上-Xint、-XX:-UseOnStackReplacement参数，问题一样不会出现。
 * 和jit也是有一定关系的，-Xint设定解释执行，也可以只关闭OSR看看，-XX:-UseOnStackReplacement
 * 大空翼 加-Xint、-XX:-UseOnStackReplacement都能解决问题。
 * 
 * volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。
	说明：如果是count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1); 
	如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。《阿里巴巴Java开发手册（泰山版）.
 * 
 * 为什么要对线程间共享的变量用关键字volatile声明？这涉及到Java的内存模型。在Java虚拟机中，变量的值保存在主内存中，但是，当线程访问变量时，它会先获取一个副本，并保存在自己的工作内存中。
 * 如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值回写到主内存，但是，这个时间是不确定的！
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	           Main Memory
	│                               │
	   ┌───────┐┌───────┐┌───────┐
	│  │ var A ││ var B ││ var C │  │
	   └───────┘└───────┘└───────┘
	│     │ ▲               │ ▲     │
	 ─ ─ ─│─│─ ─ ─ ─ ─ ─ ─ ─│─│─ ─ ─
	      │ │               │ │
	┌ ─ ─ ┼ ┼ ─ ─ ┐   ┌ ─ ─ ┼ ┼ ─ ─ ┐
	      ▼ │               ▼ │
	│  ┌───────┐  │   │  ┌───────┐  │
	   │ var A │         │ var C │
	│  └───────┘  │   │  └───────┘  │
	   Thread 1          Thread 2
	└ ─ ─ ─ ─ ─ ─ ┘   └ ─ ─ ─ ─ ─ ─ ┘
 * 这会导致如果一个线程更新了某个变量，另一个线程读取的值可能还是更新前的。例如，主内存的变量a = true，线程1执行a = false时，它在此刻仅仅是把变量a的副本变成了false，
 * 主内存的变量a还是true，在JVM把修改后的a回写到主内存之前，其他线程读取到的a的值仍然是true，这就造成了多线程之间共享的变量不一致。
 * 因此，volatile关键字的目的是告诉虚拟机：
 * 	每次访问变量时，总是获取主内存的最新值；
 * 	每次修改变量后，立刻回写到主内存。
 * volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
 * 如果我们去掉volatile关键字，运行上述程序，发现效果和带volatile差不多，这是因为在x86的架构下，JVM回写主内存的速度非常快，但是，换成ARM的架构，就会有显著的延迟。
 * x86架构加不加volatile其实区别不大，其他架构要注意，很可能一个线程改了值几秒内另一个线程读的还是旧的
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * volatile只保证：
 * 读主内存到本地副本；
 * 操作本地副本；
 * 回写主内存。
 * 这3步多个线程可以同时进行。
 * @author dell
 */
public class VolatileDemo {
	static int num = 0;
	public static void main(String[] args) {
		new Thread(()->{
			System.out.println("Child:" + num);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			num ++;
			System.out.println("Child End:" + num);
		}).start();
		
		while (num == 0) {
			System.out.println("Main:" + num);
		}
	}
}
