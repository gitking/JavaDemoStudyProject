package com.yale.test.java.fanxing;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 * 泛型继承
 * 一个类可以继承自一个泛型类。例如：父类的类型是Pair<Integer>，子类的类型是IntPair，可以这么继承：
 * 使用的时候，因为子类IntPair并没有泛型类型，所以，正常使用即可：
 */
public class IntPair extends Pair<Integer>{
	public IntPair() {
		super(11, 22);
	}
	
	public static void main(String[] args) {
		IntPair ip = new IntPair();
		/*
		 * 前面讲了，我们无法获取Pair<T>的T类型，即给定一个变量Pair<Integer> p，无法从p中获取到Integer类型。
		 * 但是，在父类是泛型类型的情况下，编译器就必须把类型T（对IntPair来说，也就是Integer类型）保存到子类的class文件中，
		 * 不然编译器就不知道IntPair只能存取Integer这种类型。
		 * 在继承了泛型类型的情况下，子类可以获取父类的泛型类型。例如：IntPair可以获取到父类的泛型类型Integer。获取父类的泛型类型代码比较复杂：
		 */
		Class<IntPair> clazz = IntPair.class;
		Type t = clazz.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)t;
			Type[] types = pt.getActualTypeArguments();//可能有多个泛型类型
			Type firstType = types[0];//取第一个泛型类型
			Class<?> typeClass = (Class<?>) firstType;
			System.out.println("父类的泛型类型为:" + typeClass);
		}
		/*
		 * 因为Java引入了泛型，所以，只用Class来标识类型已经不够了。实际上，Java的类型系统结构如下：
			                      ┌────┐
			                      │Type│
			                      └────┘
			                         ▲
			                         │
			   ┌────────────┬────────┴─────────┬───────────────┐
			   │            │                  │               │
			┌─────┐┌─────────────────┐┌────────────────┐┌────────────┐
			│Class││ParameterizedType││GenericArrayType││WildcardType│
			└─────┘└─────────────────┘└────────────────┘└────────────┘
		 */
		
		/**
		 * 13. 【强制】在无泛型限制定义的集合赋值给泛型限制的集合时，在使用集合元素时，需要进行instanceof判断，避免抛出ClassCastException异常。 
		 * 说明：毕竟泛型是在JDK5后才出现，考虑到向前兼容，编译器是允许非泛型集合与泛型集合互相赋值。
		 * 反例： 
		 * List<String> generics = null; 
		 * List notGenerics = new ArrayList(10); 
		 * notGenerics.add(new Object()); 
		 * notGenerics.add(new Integer(1)); 
		 * generics = notGenerics; 
		 * // 此处抛出ClassCastException异常 
		 * String string = generics.get(0);
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 */
		List<String> generics = null;
		List notGenerics = new ArrayList(10);
		notGenerics.add(new Object());
		notGenerics.add(new Integer(1));
		generics = notGenerics;
		String str = generics.get(0);//此处抛出ClassCastException异常 
	}
}
