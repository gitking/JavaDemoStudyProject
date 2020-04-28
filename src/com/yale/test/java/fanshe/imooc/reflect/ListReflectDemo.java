package com.yale.test.java.fanshe.imooc.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ListReflectDemo {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ArrayList list = new ArrayList();
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("只能添加String类型");
		Class c1 = list.getClass();
		Class c2 = list2.getClass();
		System.out.println("反射的操作都是编译之后的操作:" + (c1 == c2));
		/**
		 * c1 == c2返回true说明编译之后集合的泛型都是去泛型化的
		 * Java中集合的泛型,是防止错误输入的,只在编译阶段有效,
		 * 绕过编译就无效了
		 * 验证:我们可以通过方法的反射来操作,绕过编译
		 */
		
		Method m= c2.getMethod("add", Object.class);
		m.invoke(list2, 20);//注意list2只能添加String类型,我们现在可以绕过编译给他加一个整形进去
		System.out.println("注意整形已经添加进去了:" + list.size());
		System.out.println("注意整形也可以输出出来:" + list2);
		System.out.println(list2.get(1));//这里会报错,因为lis2认为20是String类型的,结果就报错了
	}
}
