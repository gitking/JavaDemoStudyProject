package com.yale.test.design.singleton;

/**
 * 【推荐】通过双重检查锁（double-checked locking）（在并发场景下）实现延迟初始化的优化问题隐患(可参考 The "Double-Checked Locking is Broken" Declaration)，
 * 推荐解决方案中较为简单一种（适用于JDK5及以上版本），
 * 将目标属性声明为 volatile型（比如修改helper的属性声明为`private volatile Helper helper = null;`）。
 *  反例：
	public class LazyInitDemo {
		private Helper helper = null;
		public Helper getHelper() {
			if (helper == null) {
				synchronized (this) {
					if (helper == null) { helper = new Helper(); }
				}
			}
			return helper;
		}
		// other methods and fields...
	}《阿里巴巴Java开发手册（泰山版）.pdf》
	volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。
	说明：如果是count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1); 
	如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。《阿里巴巴Java开发手册（泰山版）.pdf》
 * @author dell
 *
 */
class SingletonDemo2 {
	private static volatile SingletonDemo2 sd;
	private SingletonDemo2() {
		System.out.println("单例模式构造方法只能被运行一次:" + Thread.currentThread().getName());
	}
	
	public static SingletonDemo2 getInstance() {
		if (sd == null) {
			synchronized (SingletonDemo2.class) {
				if (sd == null) {
					sd = new SingletonDemo2();
				}
			}
		}
		return sd;
	}
	
}
public class SingletonThread2 {
	public static void main(String[] args) {
		/**
		 * 在多线程环境下,SingletonDemo的构造方法会被运行多次,不再试单例模式了
		 */
		new Thread(()->SingletonDemo2.getInstance(),"线程A").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程B").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程C").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程D").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程E").start();
	}
}
