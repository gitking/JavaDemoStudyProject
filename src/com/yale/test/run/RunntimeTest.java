package com.yale.test.run;

public class RunntimeTest {

	public static void main(String[] args) {
		Runtime run = Runtime.getRuntime();
		System.out.println("最大内存:" +  run.maxMemory());
		System.out.println("可用内存:" +  run.totalMemory());
		System.out.println("空余内存:" +  run.freeMemory());
		
		run.gc();//手工调用垃圾回收
		System.gc();
		
		RunntimeTest sdf = new RunntimeTest();
		try {
			sdf.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("finalize方法会在JAVA进行垃圾回收前,被自动调用" );
	}
}
