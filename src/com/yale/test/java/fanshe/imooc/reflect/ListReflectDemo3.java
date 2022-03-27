package com.yale.test.java.fanshe.imooc.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * javap -l -c -v ListReflectDemo2 用这个命令可以看到有一个checkcast指令
 * https://www.heapdump.cn/question/3209796
 * https://www.zhihu.com/question/510544205
 * 泛型强转类型之后取数据的一个疑问,问题不好描述请看代码和截图吧。
 * com.yale.test.json.JacksonDemo.java一起看
 * @author issuser
 */
public class ListReflectDemo3 {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Integer> listStr = new ArrayList<Integer>();
		listStr.add(20);
		
		Class c2 = listStr.getClass();
		Method m= c2.getMethod("add", Object.class);
		m.invoke(listStr, "只能添加Integer类型");//注意listStr只能添加String类型,我们现在可以通过反射的手段绕过编译器给他加一个整形进去
		System.out.println("注意字符串已经添加进去了,List的元素个数为:->       " + listStr.size());
		
		System.out.println();
		
		Object obj = listStr;
		ArrayList<Boolean> listTest = (ArrayList<Boolean>)obj;
		System.out.println("看看listTest的Class类型到底是什么类型:->    " + listTest.getClass());
		
		/**
		 * https://www.zhihu.com/question/510544205
		 * 答主廖雪峰：
		 * 我建议初学者要把精力放在更重要的问题上，不要钻研这些貌似古怪的问题，因为合格的开发者根本不会写这种代码，自然很少有人去研究为什么。
		 * 你的问题就是放入一个String按List<Boolean>取出应该报错结果不报错，首先不要轻易怀疑JVM，因为Boolean b = bs.get(0)一定报ClassCastException
		 * List<String> strs = new ArrayList<>();
			strs.add("test");
			Object o = strs;
			List<Boolean> bs = (List<Boolean>) o;
			System.out.println(bs.get(0)); // 不报错
			Boolean b = bs.get(0); // ClassCastException
		 * 但是System.out.println(bs.get(0))不报错，你看看println(Object)方法签名就知道了，它是Object，而List.get()取出的本来就是Object，
		 * 编译器只是做了个优化觉得没必要先转型Boolean再转型Object，生成的字节码也完全符合JVM的要求。打印的内容是Object.toString()跟哪个类型并没有关系。
		 */
		System.out.println(listTest.get(0));

		System.out.println("我想得到一个Boolean类型,但是得到却是String类型,这是为什么,为什么这里没报错?:->    " + listTest.get(0));
		System.out.println("我想得到一个Boolean类型,但是得到却是String类型,这是为什么,为什么这里没报错?:->    " + listTest.get(1));
		System.out.println("上面没报错就很奇怪，一定要搞明白");
		System.out.println();

		System.out.println("下面的会报错我能理解,上面的没报错我就不能理解?");
		System.out.println("listStr的toString方法,注意整形也可以输出出来:    " + listStr.toString());
		System.out.println("看看listStr的Class类型到底是什么类型:->    " + listStr.getClass());
		//Integer in = listStr.get(1);编译直接就报错了
		
		
		/*
		 * 这里报错是因为println的方法签名为:void java.io.PrintStream.println(String x)
		 * 注意：泛型在编译成class文件之后就会把泛型擦除掉,所以在JVM运行时这里的listStr.get(1),get方法返回的实际上Object类型,这里一定要理解。
		 * 由于println的方法签名接收的是String类型,所以JVM帮我们做了一个类型转换,把Object转换成了String,于是就发生了错误
		 * java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
		 */
		Object in = listStr.get(1);//这里不会报错
		System.out.println("这样可以取出来唉,不会报错:" + in.toString());
		System.out.println("ArrayList<String> listStr 换成 ArrayList<Integer> listStr 就不会报错了:" +listStr.get(1));//这里会什么又会报错了,因为lis2认为20是String类型的,结果就报错了,java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
	}
}
