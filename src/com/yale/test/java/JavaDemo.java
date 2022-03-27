package com.yale.test.java;

import java.lang.reflect.Field;

import org.openjdk.jol.vm.VM;

import sun.misc.Unsafe;

public class JavaDemo {
	public static void main(String[] args) {
		try {
			JavaDemo javaDemoAddr = new JavaDemo();

			//GC前
			System.out.println("-----------GC前------------");
			print(javaDemoAddr);
			
			System.out.println("GC垃圾回收前JavaDemo这个类的Class对象地址:" + VM.current().addressOf(JavaDemo.class));
			System.out.println("GC垃圾回收前JavaDemo这个类的Class对象地址:" + Integer.toHexString(new JavaDemo().hashCode()));
			System.out.println("GC垃圾回收前JavaDemo这个类的Class对象地址:" + Integer.toHexString(JavaDemo.class.hashCode()));

			System.out.println("GC垃圾回收前javaDemoAddr对象的地址:" + VM.current().addressOf(javaDemoAddr));
			int[] intArrAdrress = new int[10485760];
			System.out.println("GC垃圾回收前数组intArrAdrress对象的地址:" + VM.current().addressOf(intArrAdrress));
			System.out.println("注意观察这个大数组intArrAdrress是直接分配在了老年代了？还是直接分配在了新生代了?");
//			for (int i=0;i<intArrAdrress.length;i++) {
//				intArrAdrress[i] = Integer.MAX_VALUE;
//			}
			System.out.println("GC垃圾回收开始了");
			//Thread.sleep(180000);
			System.gc();
			System.out.println("GC垃圾回收结束了");
			System.out.println("GC垃圾回收之后javaDemoAddr对象的地址:GC垃圾回收之后注意看对象的引用地址发生变化了:" + VM.current().addressOf(javaDemoAddr));
			System.out.println("GC垃圾回收之后数组intArrAdrress对象的地址:GC垃圾回收之后注意看对象的引用地址发生变化了:" + VM.current().addressOf(intArrAdrress));
			System.out.println("GC垃圾回收之后JavaDemo这个类的Class对象地址:" + VM.current().addressOf(JavaDemo.class));
			System.out.println("GC垃圾回收前JavaDemo这个类的Class对象地址:" + Integer.toHexString(new JavaDemo().hashCode()));
			System.out.println("GC垃圾回收前JavaDemo这个类的Class对象地址:" + Integer.toHexString(JavaDemo.class.hashCode()));
			//GC后
			System.out.println("-----------GC后------------");
			print(javaDemoAddr);
			intArrAdrress[1] = 1024;
			javaDemoAddr.equals(new JavaDemo());
			Thread.sleep(10000000000000000L);
			intArrAdrress[2] = 1024;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static final Unsafe unsafe = getUnsafe();
	static final boolean is64bit = true; // auto detect if possible.

	private static void print(JavaDemo a) {
		// hashcode
		System.out.println("Hashcode :       " + a.hashCode());
		System.out.println("Hashcode :       " + System.identityHashCode(a));
		System.out.println("Hashcode (HEX) : " + Integer.toHexString(a.hashCode()));// Integer.toHexString(int)是将一个整型转成一个十六进制数

		// toString
		System.out.println("toString :       " + String.valueOf(a));

		//通过sun.misc.Unsafe;
		printAddresses("Address", a);
	}

	//https://blog.csdn.net/zhoufanyang_china/article/details/86750351
	//从输出信息来看，printAddresses()方法和jol工具类打印的对象的内存地址是一致的，当然如果去深入研究jol工具类，它底层也是通过sun.misc.Unsafe实现的！
	@SuppressWarnings("deprecation")
	public static void printAddresses(String label, Object... objects) {
		System.out.print(label + ":         0x");
		long last = 0;
		int offset = unsafe.arrayBaseOffset(objects.getClass());
		int scale = unsafe.arrayIndexScale(objects.getClass());
		switch (scale) {
		case 4:
			long factor = is64bit ? 8 : 1;
			final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
			System.out.print(Long.toHexString(i1));
			last = i1;
			for (int i = 1; i < objects.length; i++) {
				final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
				if (i2 > last)
					System.out.print(", +" + Long.toHexString(i2 - last));
				else
					System.out.print(", -" + Long.toHexString(last - i2));
				last = i2;
			}
			break;
		case 8:
			throw new AssertionError("Not supported");
		}
		System.out.println();
	}

	private static Unsafe getUnsafe() {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
