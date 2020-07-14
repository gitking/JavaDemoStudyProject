package com.yale.test.java.fanxing.ref;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.yale.test.java.fanxing.Pair;

/*
 * Java的部分反射API也是泛型。例如：Class<T>就是泛型：
 * 调用Class的getSuperclass()方法返回的Class类型是Class<? super T>：
 */
public class RefClass {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		//compile warning:有警告
		Class strCls = String.class;
		String str = (String)strCls.newInstance();
		
		//no warning:没有警告
		Class<String> strClz = String.class;
		String clzObj = strClz.newInstance();
		
		//调用Class的getSuperclass()方法返回的Class类型是Class<? super T>：
		Class<? super String> sup = String.class.getSuperclass();
		
		//构造方法Constructor<T>也是泛型：
		Class<Integer> intCls = Integer.class;
		Constructor<Integer> cons = intCls.getConstructor(int.class);
		Integer i = cons.newInstance(213);
		
		//我们可以声明带泛型的数组，但不能用new操作符创建带泛型的数组：
		Pair<String>[] ps = null;
		//Pair<String>[] psArr = new Pair<String>[2];//编译错误
		
		//必须通过强制转型实现带泛型的数组：
		Pair<String>[] psArr = (Pair<String>[])new Pair[2];//编译错误
		
		System.out.println("带泛型的数组实际上是编译器的类型擦除：" + (psArr.getClass() == Pair[].class)); // true
		System.out.println("所以我们不能直接创建泛型数组T[]，因为擦拭后代码变为Object[]：,必须借助Class<T>来创建泛型数组：,我们还可以利用可变参数创建泛型数组T[]：");
		
		/*
		 * 使用泛型数组要特别小心，因为数组实际上在运行期没有泛型，编译器可以强制检查变量ps，因为它的类型是泛型数组。但是，编译器不会检查变量arr，
		 * 因为它不是泛型数组。因为这两个变量实际上指向同一个数组，所以，操作arr可能导致从ps获取元素时报错，例如，以下代码演示了不安全地使用带泛型的数组：
		 */
		Pair[] parr = new Pair[2];
		Pair<String>[] pasArr = (Pair<String>[])parr;
		pasArr[0] = new Pair<String>("a", "c");
		parr[1] = new Pair<Integer>(1, 2);
		
		Pair<String> p = pasArr[1];
		String s = p.getFirst();//这里会报java.lang.ClassCastException:
		
		//要安全地使用泛型数组，必须扔掉parr的引用：
		Pair<String>[] psafe = (Pair<String>[])new Pair[2];
		//上面的代码中，由于拿不到原始数组的引用，就只能对泛型数组psafe进行操作，这种操作就是安全的。
	}
}

