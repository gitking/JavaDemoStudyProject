package com.yale.test.java.fanxing.ref;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.yale.test.java.fanxing.Pair;

public class ArrayHelper {
	public static void main(String[] args) {
		//我们可以声明带泛型的数组，但不能用new操作符创建带泛型的数组：
		Pair<String>[] ps = null;
		//Pair<String>[] psArr = new Pair<String>[2];//编译错误
		
		//必须通过强制转型实现带泛型的数组：
		Pair<String>[] psArr = (Pair<String>[])new Pair[2];//编译错误
		
		System.out.println("带泛型的数组实际上是编译器的类型擦除：" + (psArr.getClass() == Pair[].class)); // true
		System.out.println("所以我们不能直接创建泛型数组T[]，因为擦拭后代码变为Object[]：,必须借助Class<T>来创建泛型数组：,我们还可以利用可变参数创建泛型数组T[]：");
		
		String[] ss = ArrayHelper.asArray("a", "b", "c");
		Integer[] ns = ArrayHelper.asArray(1, 2, 3);
		
		String[] arr = asArray("one", "two", "three");
	    System.out.println(Arrays.toString(arr));
	    
        // ClassCastException:
        String[] firstTwo = pickTwo("one", "two", "three");
        System.out.println(Arrays.toString(firstTwo));
	}
	
	/**
	 * 谨慎使用泛型可变参数
	 * asArray方法似乎可以安全地创建一个泛型数组。但实际上，这种方法非常危险。以下代码来自《Effective Java》的示例：
	 * @param objs
	 * @return
	 */
	@SafeVarargs
	static <T> T[] asArray(T...objs){
		return objs;
	}
	
	/*
	 * 直接调用asArray(T...)似乎没有问题，但是在另一个方法中，我们返回一个泛型数组就会产生ClassCastException，
	 * 原因还是因为擦拭法，在pickTwo()方法内部，编译器无法检测K[]的正确类型，因此返回了Object[]。
	 * 如果仔细观察，可以发现编译器对所有可变泛型参数都会发出警告，除非确认完全没有问题，才可以用@SafeVarargs消除警告。
	 */
	static <K> K[] pickTwo(K k1, K k2, K k3) {
        return asArray(k1, k2);// 如果在方法内部创建了泛型数组，最好不要将它返回给外部使用。 更详细的解释请参考《Effective Java》“Item 32: Combine generics and varargs judiciously”。
    }
}
