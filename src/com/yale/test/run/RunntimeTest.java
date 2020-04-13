package com.yale.test.run;

/**
 * Runtime描述的是运行时的状态,也就是说在整个的JVM之中,Runtime类是唯一一个与JVM运行状态有关的类,
 * 并且都会默认提供有一个该类的实例化对象。
 * 由于在每一个JVM进程里面只允许提供一个Runtime类的对象,所以这个类的构造方法被默认私有化了,那么就证明该类
 * 使用的是单例设计模式
 * 阿里云 魔乐科技的JAVA教程视频
 * https://edu.aliyun.com/lesson_36_446?spm=5176.8764728.0.0.3ead521ct2pu1e#_446
 * https://edu.aliyun.com/course/explore/gentech?spm=5176.10731491.category.12.78904d8aB9zl2v&subCategory=java&filter%5Btype%5D=all&filter%5Bprice%5D=all&filter%5BcurrentLevelId%5D=all&orderBy=studentNum&title=
 * @author dell
 */
public class RunntimeTest {

	public static void main(String[] args) throws InterruptedException {
		RunntimeTest sdf = new RunntimeTest();
		sdf = null;//等待垃圾回收
		Runtime run = Runtime.getRuntime();
		System.out.println("获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		String str = "垃圾空间";
		for (int i=0;i <30000; i++) {
			str = str + i;
		}
		System.out.println("[2]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[2]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[2]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		Thread.sleep(2000);
		run.gc();//手工调用垃圾回收
		System.gc();//System.gc内部调用的就是run.gc()
		
		System.out.println("[3]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[3]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[3]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		
//		RunntimeTest sdf = new RunntimeTest();
//		try {
//			sdf.finalize();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		/**
		 * 但是从JDK1.9开始,这一操作已经不建议使用了,而对于对象回收释放.从JDK 
		 * 或者使用java.lang.ref.Cleaner类进行回收(Cleaner也支持有AutoCloseable处理).
		 */
		System.out.println("finalize方法会在JAVA进行垃圾回收前,被自动调用,这个方法抛出的异常类型为Throwable,但是这个类即使抛出异常了,也不影响后面代码的执行" );
		throw new Exception("我不影响后面代码的继续运行,因为我这个类马上就死了");
	}
}
