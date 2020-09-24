package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 【强制】必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal变量，
 * 可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收。 正例：
	objectThreadLocal.set(userInfo);
	try {
		// ...
	} finally {
		objectThreadLocal.remove();
	}《阿里巴巴Java开发手册（泰山版）.pdf》
 * 多线程是Java实现多任务的基础，Thread对象代表一个线程，我们可以在代码中调用Thread.currentThread()获取当前线程。例如，打印日志时，可以同时打印出当前线程的名字：
 * 这种在一个线程中，横跨若干方法调用，需要传递的对象，我们通常称之为上下文（Context），它是一种状态，可以是用户身份、任务信息等。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581251653666
 * ThreadLocal实例通常总是以静态字段初始化如下：
 * static ThreadLocal<User> threadLocalUser = new ThreadLocal<>();
 * 它的典型使用方式如下：
 * void processUser(user) {
	    try {
	        threadLocalUser.set(user);
	        step1();
	        step2();
	    } finally {
	        threadLocalUser.remove();
	    }
	}
	通过设置一个User实例关联到ThreadLocal中，在移除之前，所有方法都可以随时获取到该User实例：
	实际上，可以把ThreadLocal看成一个全局Map<Thread, Object>：每个线程获取ThreadLocal变量时，总是使用Thread自身作为key：
	Object threadLocalValue = threadLocalMap.get(Thread.currentThread());
	因此，ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不干扰。
	最后，特别注意ThreadLocal一定要在finally中清除：
	try {
	    threadLocalUser.set(user);
	    ...
	} finally {
	    threadLocalUser.remove();
	}
 * 这是因为当前线程执行完相关代码后，很可能会被重新放入线程池中，如果ThreadLocal没有被清除，该线程执行其他代码时，会把上一次的状态带进去。
 * 为了保证能释放ThreadLocal关联的实例，我们可以通过AutoCloseable接口配合try (resource) {...}结构，让编译器自动为我们关闭。例如，一个保存了当前用户名的ThreadLocal可以封装为一个UserContext对象：
 * @author dell
 * 小结
 * ThreadLocal表示线程的“局部变量”，它确保每个线程的ThreadLocal变量都是各自独立的；
 * ThreadLocal适合在一个线程的处理流程中保持上下文（避免了同一参数在所有方法中传递）；
 * 使用ThreadLocal要用try ... finally结构，并在finally中清除。
 */
public class ThreadLocalTest {

	/**
	 * 线程内共享变量，每个线程有自己的单独的变量
	 * 一个ThreadLocal代表一个变量,故其中只能放一个数据,你有俩个变量都要线程内共享,你要定义多个ThreadLocal
	 * Runtime Runtime是JVM虚拟机的对象
	 */
	private static ThreadLocal<Integer> x = new ThreadLocal<Integer>();
	//Runtime run = Runtime.getRuntime();//Runtime是JVM虚拟机的对象
	//ThreadDeathEvent,ThreadDeathRequest线程死亡的时候可以得到通知
	
	public static void main(String[] args) {
		for(int i=0; i <2; i++) {
			new Thread(new Runnable () {
				@Override
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + " has put data:" + data);
					x.set(data);
					MyThreadScopeData.getThreadInstance().setName("name" + data);
					MyThreadScopeData.getThreadInstance().setAge( data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	static class A {//外部类可以用static修改吗？
		public void get() {
			int data = x.get();
			System.out.println("A from " + Thread.currentThread().getName() + " get data:" + data);
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();

			System.out.println("A from " + Thread.currentThread().getName() + " getMyData:" + myData.getName() + "," + myData.getAge());

		}
	}
	
	static class B {
		public void get() {
			int data = x.get();
			System.out.println("B from " + Thread.currentThread().getName() + " get data:" + data);
			
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("B from " + Thread.currentThread().getName() + " getMyData:" + myData.getName() + "," + myData.getAge());
		}
	}
}

class MyThreadScopeData {
	private MyThreadScopeData(){
	}
	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();
	public static MyThreadScopeData getThreadInstance() {
		MyThreadScopeData instance = map.get();
		if (instance == null) {
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		return instance;
	}
	
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}

/*
 * 为了保证能释放ThreadLocal关联的实例，我们可以通过AutoCloseable接口配合try (resource) {...}结构，让编译器自动为我们关闭。
 * 例如，一个保存了当前用户名的ThreadLocal可以封装为一个UserContext对象：
 * 使用的时候，我们借助try (resource) {...}结构，可以这么写：
 * try (var ctx = new UserContext("Bob")) {
	    // 可任意调用UserContext.currentUser():
	    String currentUser = UserContext.currentUser();
	} // 在此自动调用UserContext.close()方法释放ThreadLocal关联对象
	这样就在UserContext中完全封装了ThreadLocal，外部代码在try (resource) {...}内部可以随时调用UserContext.currentUser()获取当前线程绑定的用户名。
 */
class UserContext implements AutoCloseable {
	static final ThreadLocal<String> ctx = new ThreadLocal<>();
	
	public UserContext(String user) {
		/*
		 * 可以看下 ThreadLocal的set源码,实际上set方法获取的是Thread类的属性ThreadLocalMap，
		 * 然后将ThreadLocal的实例作为key放进Thread线程的属性ThreadLocalMap里面了,
		 * 并不是网上说的把Thread.currentThread当前线程的实例当做key放进去了.
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581251653666
		 * 为什么你们要用我的昵称 问:每个线程获取ThreadLocal变量时，总是使用Thread自身作为key：感觉这个说法跟代码实现不太一样啊，set（） 作为ThreadLocal类的方法，从源代码里可以看到传进去的key 是this， 也就是ThreadLocal实例本身，而不是Thread自身。
		 *  public void set(T value) {
            	map.set(this, value);
    		}
		 * 这里的map，即ThreadLocalMap，是Thread类的一个变量，保存每一个ThreadLocal想要存进去的数据。 如有错误，请廖老师纠正。
		 * 廖雪峰答: 这里是给出一个最简单的实现ThreadLocal的原理。
		 * 但是在JDK的实际代码中，为了提高速度，每个Thread自带一个Map，用ThreadLocal作为key存取Thread自己的Map，目的是避免单个全局Map带来的加锁问题。
		 */
		ctx.set(user);
	}
	
	public static String currentUser() {
		return ctx.get();
	}
	
	@Override
	public void close() throws Exception {
		ctx.remove();
	}
}