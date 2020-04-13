package com.yale.test.thread.heima.zhangxiaoxiang.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 现在有程序同时启动了4个线程去调用TestDoDo.doSome(key, value)方法,由于TestDoDo.doSome(key, value)方法内部的代码是先暂停1秒,
 * 然后再输出以秒为单位的当前时间值,所以会打印出4个相同的值,如下所示:
 * 4:4:1258199615
 * 1:1:1258199615
 * 3:3:1258199615
 * 1:2:1258199615
 * 请修改代码,如果有几个线程调用TetDoDo.doSome(key, value)方法时,传递进去的key相等(equals比较为true),则这几个线程应互斥排队输出结果,即当有俩个线程的key都是1时,他们中的一个要比另外
 * 其他线程晚1秒输出,如下所示
 * 4:4:1258199615
 * 1:1:1258199615
 * 3:3:1258199615
 * 1:2:1258199616
 * 总之,当每个线程中指定的key相等时,这些相等的key的线程应每隔1秒依次输出时间值(要用互斥),如果key不同,则并行执行(相互之间不互斥).
 * @author dell
 *
 */
public class Test03 extends Thread{

	private TestDoDo testDo;
	private String key;
	private String value;
	
	public Test03(String key, String key2, String value) {
		this.testDo = TestDoDo.getInstance();
		/**
		 * 常量"1" 和 "1"是同一个对象,下面这行代码就是要用"1" + ""的方式产生新的对象
		 * 以实现内容没有改变,仍然相等(都还是"1"),当对象却不再是同一个的效果
		 */
		this.key = key + key2;
		this.value = value;
	}
	public static void main(String[] args) {
		Test03 a = new Test03("1", "", "1");
		Test03 b = new Test03("1", "", "2");
		Test03 c = new Test03("3", "", "3");
		Test03 d = new Test03("4", "", "4");
		System.out.println("begin:" + (System.currentTimeMillis()/1000));
		a.start();
		b.start();
		c.start();
		d.start();
	}
	
	@Override
	public void run() {
		testDo.doSome(key, value);
	}
}


class TestDoDo {
	private TestDoDo(){
	}
	
	private static TestDoDo instance = new TestDoDo();
	public static TestDoDo getInstance() {
		return instance;
	}
	//private ArrayList keys = new ArrayList();//Iterator迭代的时候不能,往集合里面加东西会报错,这个ArrayList不是线程安全的
	private CopyOnWriteArrayList keys = new CopyOnWriteArrayList();
	public void doSome(Object key, String value) {
		Object o = key;
		if (!keys.contains(o)) {
			keys.add(o);//Iterator迭代的时候不能,往集合里面加东西会报错
		} else {
			for (Iterator iter = keys.iterator();iter.hasNext();) {
				//Iterator迭代的时候不能,往集合里面加东西会报错
				Object oo = iter.next();
				if (oo.equals(o)) {
					o = oo;
				}
			}
		}
		//以大括号内的是需要局部同步的代码,不能改动
		synchronized(o){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			/**
			 * System.currentTimeMillis() / 1000 这个是为了取秒
			 * 10000348
			 * 10000349
			 * 10000350
			 * 上面这几个除以1000取出的都是同一秒,
			 * 10000998
			 * 10000999
			 * 10001000 注意这个除以1000取余
			 * 注意上面这个,10000999和10001000 虽然只差了1毫秒,但是却是俩个世界,取出的却不是同一秒, 上面这几个除以1000取出的都是同一秒,
			 */
			System.out.println(key + ":" + value +":" + (System.currentTimeMillis() / 1000));
		}
	}
}