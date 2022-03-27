package com.yale.test.java.fanshe.imooc.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * javap -l -c -v ListReflectDemo2 用这个命令可以看到有一个checkcast指令
 * https://www.heapdump.cn/question/3209796
 * https://heapdump.cn/question/3209796
 * https://www.zhihu.com/question/510544205
 * 泛型强转类型之后取数据的一个疑问,问题不好描述请看代码和截图吧。
 * com.yale.test.json.JacksonDemo.java一起看
 * 
 * 受知乎答主廖雪峰大神的启发，这个疑问我已经完全搞明白了，非常感谢廖雪峰大神，知乎那里我也感谢了。知乎答主廖雪峰大神的回答在这里：泛型强转类型之后取数据的一个疑问?
 * 我的第一个疑问：这里为什么不会报错？
 * 不报错的原因如下：
 * 首先我们要明白这段程序最后的那个get方法报错，为什么会报错？报错的原因是因为发了强制类型转换，但是Integer类型不能转换为String类型，所以就最后的那个get方法就报错了：java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String。
 * 那我的的第一个疑问：这里为什么不会报错？就是我认为这里也应该发生强制类型转换报错，但是却没有报错。没有报错就说明没有发生强制类型转啊，那这里为什么没有发生强制类型转换呢？看源码是看不出来的，要看编译后的class字节码，使用javap -l -c -v ListReflectDemo2命令来查看字节码文件，截图如下：
 * 从编译后的字节码来看我们的这行代码
 * System.out.println("我想得到一个Boolean类型,但是得到却是String类型,这是为什么,为什么这里没报错?:->    " + listTest.get(0));
 * 这行代码被编译成了
 * System.out.println(new StringBuilder("我想得到一个Boolean类型,但是得到却是String类型,这是为什么,为什么这里没报错?:->    ").append(listTest.get(0)).toString());
 * 这里的关键在于append(listTest.get(0))调用的是StringBuilder的这个方法：
 * 但是要知道我们的代码 listTest.get(0)这个get方法返回的是Boolean类型啊，
 * 那为什么编译后调用的是append(Object obj)这个方法呢？答案是：因为StringBuilder这个类没有提供append(Boolean bool)方法，所以JVM在编译的时候，只能调用append(Object obj)这个方法了。
 * 所以，我的第一个疑问：这里为什么不会报错？已经搞明白了。
 * 我的第二个疑问：这里又为啥会报错了？
 * 答案很简单，因为PrintStream这个类提供了println(String x)方法，所以JVM在编译的时候，只能调用println(String x)这个方法了。
 * 这里很关键的一点是：JVM在编译阶段是知道有泛型存在的,所以JVM知道listStr.get(1)这个get方法的返回值肯定是String类型的,那PrintStream类又恰好有println(String x)这种方法存在,所以JVM编译的时候直接就调用了println(String x)这个方法。
 * 但是在JVM运行的时候由于擦除了泛型，那listStr.get(1)这个get方法返回的肯定是Objejct类型,所以JVM在这里增加了一个checkcast指令，JVM在编译的时候认为这里的强转是肯定没问题的，所以JVM很自信的就加了checkcast这个强转指令。JVM没想到我们会通过反射往一个List<Stirng>里面增加一个整型进去。
 * 现实中没有人会这么写代码的，那我为啥会发现这个问题呢？最近我刚好在看Jackson的文章，有这么一个json字符串
 * 然后将这个json字符串用Jackson转换为Map，然后从Map里面取tags的值发现了一个奇怪的事情：
 * Map<String, Object> mapSec = mapperJackson.readValue(prettyJson, Map.class);
 * System.out.println("tags的值是一个数组(List),注意结果输出的不是Boolean类型而是字符串类型,但是竟然没有报错，我取的是Boolean类型啊,但是输出结果是"java","java"是一个字符串跟Boolean类型完全不兼容啊,为啥没有报错呢？:->" + ((List<Boolean>)mapSec.get("tags")).get(0));
 * 然后就这个问题就被我搞出来了，哈哈, 哎呀真开心啊，终于搞明白了。这个问题截止到现在我已经研究了至少30个小时了，昨天晚上睡觉的时候很不开心，今天很早就起床了，最后经廖雪峰大神点拨一下，瞬间就明白了，太感谢廖雪峰大神了。
 * 关联问题JackSon怎么把一个json字符串转成JSON对象啊？只能转换成JavaBean吗？（https://www.heapdump.cn/question/3207387）
 * https://www.zhihu.com/question/510405450
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1320418650619938
 * @author issuser
 */
public class ListReflectDemo2 {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<String> listStr = new ArrayList<String>();
		listStr.add("只能添加String类型");
		
		Class c2 = listStr.getClass();
		Method m= c2.getMethod("add", Object.class);
		m.invoke(listStr, 20);//注意listStr只能添加String类型,我们现在可以通过反射的手段绕过编译器给他加一个整形进去
		System.out.println("注意整形已经添加进去了,List的元素个数为:->       " + listStr.size());
		
		System.out.println();
		
		Object obj = listStr;
		ArrayList<Boolean> listTest = (ArrayList<Boolean>)obj;
		System.out.println("看看listTest的Class类型到底是什么类型:->    " + listTest.getClass());
		
		/**
		 * https://www.zhihu.com/question/510544205
		 * 知乎答主廖雪峰：
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
		 * 为什么这里的println的方法参数为String x呢？因为，泛型在源码(编译阶段)是存在的,JVM编译这段源代码的时候,一看listStr这个List用的是String类型,
		 * 那么listStr的get方法返回的肯定也是String类型啊,所以JVM在编译的时候直接就把println方法的签名编译为:void java.io.PrintStream.println(String x)
		 * 但是要注意：泛型在编译成class文件之后就会把泛型擦除掉,所以在JVM运行时这里的listStr.get(1),get方法返回的实际上Object类型,这里一定要理解。
		 * 由于println的方法签名接收的是String类型,所以JVM帮我们做了一个强制类型转换,把Object转换成了String,于是就发生了错误
		 * java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
		 */
		Object in = listStr.get(1);//这里不会报错
		System.out.println("这样可以取出来唉,不会报错:" + in.toString());
		System.out.println("11" + listStr.get(1));//这里会什么又会报错了,因为lis2认为20是String类型的,结果就报错了,java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
	}
}
