package com.yale.test.java.fanshe.perfma;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * https://club.perfma.com/question/669625
 * https://club.perfma.com/article/558214?last=577296&type=sub
 * 重大事故！IO问题引发线上20台机器同时崩溃
 * @author dell
 */
public class PerfmaDemo {
	private final String str = "";
	private final String str2;
	
	public String getStr() {
		return str;
	}

	public String getStr2() {
		return str2;
	}

	PerfmaDemo() {
		str2 = "反射结果有点怪";
	}
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		PerfmaDemo pd = new PerfmaDemo();
		Field field = PerfmaDemo.class.getDeclaredField("str");
		field.setAccessible(true);
		field.set(pd, "xxx");//这里通过反射给PerfmaDemo的属性str设置了一个值,按理来说
		/**
		 * 按理来说这里通过pd.str应该能取到设置的值xxx,但是这里得不到
		 * 其实str修改成功了，只是输出test.str并不是使用getField指令获取值而是使用ldc直接从常量池取值
		 * 这应该是编译对final标识的字符串做出的优化，在取值的时候用反射可以拿到修改后的值.
		 * 这个可以通过javap -verbose 看出来,也可以通过反编译看出来。
		 * 其实最主要的是str这个变量是有final修饰的并且是直接赋值了
		 */
		System.out.println("直接用final修饰并直接赋值的变量,通过反射重新赋值之后,这种方式取不到值:" + pd.str);
		System.out.println("直接用final修饰并直接赋值的变量,通过反射重新赋值之后,用get方法也取不到值:" + pd.getStr());
		Method method = PerfmaDemo.class.getMethod("getStr");
		System.out.println("直接用final修饰并直接赋值的变量,通过反射重新赋值之后,通过反射调用用get方法也取不到值:" + method.invoke(pd, args));
		
		//这里可以取到值
		System.out.println("直接用final修饰并直接赋值的变量,通过反射重新赋值之后,可以用这种方式取到值:" + field.get(pd));
		
		
		Field field2 = PerfmaDemo.class.getDeclaredField("str2");
		field2.setAccessible(true);
		field2.set(pd, "xxx2");
		System.out.println(pd.str2);
	}
}
