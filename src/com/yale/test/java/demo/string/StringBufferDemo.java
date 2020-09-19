package com.yale.test.java.demo.string;

/**
 * String特点:任何的字符串常量都是String对象,而且String的常量一旦声明则不可改变, 如果改变对象的内容改变的是其引用的指向而已
 * 如果一个类被设计为允许多线程正确访问，我们就说这个类就是“线程安全”的（thread-safe），上面的Counter类就是线程安全的。Java标准库的java.lang.StringBuffer也是线程安全的。
 * 还有一些不变类，例如String，Integer，LocalDate，它们的所有成员变量都是final，多线程同时访问时只能读不能写，这些不变类也是线程安全的。
 * 最后，类似Math这些只提供静态方法，没有成员变量的类，也是线程安全的。
 * 除了上述几种少数情况，大部分类，例如ArrayList，都是非线程安全的类，我们不能在多线程中修改它们。但是，如果所有线程都只读取，不写入，那么ArrayList是可以安全地在线程间共享的。
 * 没有特殊说明时，一个类默认是非线程安全的。
 * @author dell
 */
public class StringBufferDemo {
	public static void main(String[] args) {
		String str01 = "abc";
		String str02 = "abc";
		System.out.println("这俩个String对象实际上同一个:" + (str01 == str02));
		
		
		/**
		 * String,StringBuffer,StringBuilder这三个类都实现了CharSequence接口
		 * StringBuffer是从jdk1.0出现的,StringBuffer的每个方法都有synchronized修饰,所以速度慢
		 * StringBuilder是从jdk1..5出现的,StringBuilder的方法没有synchronized,速度快
		 * 推荐使用StringBuilder
		 * 注意：对于普通的字符串+操作，并不需要我们将其改写为StringBuilder，因为Java编译器在编译时就自动把多个连续的+操作编码为StringConcatFactory的操作。在运行期，StringConcatFactory会自动把字符串连接操作优化为数组复制或者StringBuilder操作。
		     你可能还听说过StringBuffer，这是Java早期的一个StringBuilder的线程安全版本，它通过同步来保证多个线程操作StringBuffer也是安全的，但是同步会带来执行速度的下降。
			StringBuilder和StringBuffer接口完全相同，现在完全没有必要使用StringBuffer。
			要高效拼接字符串，应该使用StringBuilder。
		 */
		CharSequence charSeq = "Hello";//字符串,子类对象为父类接口实例化
		
		String str = "hello";
		StringBuffer buf = new StringBuffer(str);
		System.out.println("把str对象转换为StringBuffer对象" + str);
		String strDemo = buf.toString();
		System.out.println("调用StringBuffer的toString方法将StringBuffer对象变为String对象" + buf.toString());
		
		System.out.println("StringBuffer可以将字符串反转:" + buf.reverse());
		System.out.println("StringBuffer删除指定范围的数据:" + buf.delete(2, 4));
		
		System.out.println("StringBuffer在指定位置追加字符串:" + buf.insert(0, "你好"));
		
		changeStr(str);
		System.out.println("String的对象str值没有变:" + str);
		System.out.println("**********************");
		changeStr(buf);
		System.out.println("StringBuffer的对象值变了:" + buf);
	}
	
	public static void changeStr(StringBuffer str) {
		str.append("StringBuffer通过方法参数传进来可以改变自身内容,String就不行");
	}
	
	public static void changeStr(String str) {
		str = str + "StringBuffer通过方法参数传进来可以改变自身内容,String就不行";
	}
}
