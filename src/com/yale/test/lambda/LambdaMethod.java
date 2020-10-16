package com.yale.test.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 方法引用
 * 使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
 * 实际上，除了Lambda表达式，我们还可以直接传入方法引用。例如：
 */
public class LambdaMethod {

	public static void main(String[] args) {
		String[] array = new String[]{"Apple", "Orange", "Banana", "Lemon"};
		/*
		 * 下述代码在Arrays.sort()中直接传入了静态方法cmp的引用，用LambdaMethod::cmp表示。
		 * 因此，所谓方法引用，是指如果某个方法签名和接口恰好一致，就可以直接传入方法引用。
		 * 因为Comparator<String>接口定义的方法是int compare(String, String)，和静态方法int cmp(String, String)相比，
		 * 除了方法名外，方法参数一致，返回类型相同，因此，我们说两者的方法签名一致，可以直接把方法名作为Lambda表达式传入：
		 */
		Arrays.sort(array, LambdaMethod::cmp);
		System.out.println(String.join(",", array));
		
		/*
		 * 注意：在这里，方法签名只看参数类型和返回类型，不看方法名称，也不看类的继承关系。
		 * 我们再看看如何引用实例方法。如果我们把代码改写如下：
		 * 不但可以编译通过，而且运行结果也是一样的，这说明String.compareTo()方法也符合Lambda定义。
		 * 观察String.compareTo()的方法定义：
		 * 这个方法的签名只有一个参数，为什么和int Comparator<String>.compare(String, String)能匹配呢？
		 * 因为实例方法有一个隐含的this参数，String类的compareTo()方法在实际调用的时候，第一个隐含参数总是传入this，相当于静态方法：
		 * public static int compareTo(this, String o);
		 * 所以，String.compareTo()方法也可作为方法引用传入。
		 */
		Arrays.sort(array, String::compareTo);//疑问compareTo是实例方法,需要对象调用,那么此时的对象是谁呢？答案是array里面的String对象
		System.out.println(String.join(",", array));
		
		/*
		 * 构造方法引用
		 * 除了可以引用静态方法和实例方法，我们还可以引用构造方法。
		 * 我们来看一个例子：如果要把一个List<String>转换为List<Human>，应该怎么办？
		 * 传统的做法是先定义一个ArrayList<Person>，然后用for循环填充这个List：
		 * List<String> names = List.of("Bob", "Alice", "Tim");
			List<Person> persons = new ArrayList<>();
			for (String name : names) {
			    persons.add(new Person(name));
			}
		 * 要更简单地实现String到Person的转换，我们可以引用Person的构造方法：
		 * 后面我们会讲到Stream的map()方法。现在我们看到，这里的map()需要传入的FunctionalInterface的定义是：
		 * @FunctionalInterface
			public interface Function<T, R> {
			    R apply(T t);
			}
		 * 把泛型对应上就是方法签名Person apply(String)，即传入参数String，返回类型Person。而Person类的构造方法恰好满足这个条件，
		 * 因为构造方法的参数是String，而构造方法虽然没有return语句，但它会隐式地返回this实例，类型就是Person，因此，此处可以引用构造方法。
		 * 构造方法的引用写法是类名::new，因此，此处传入Human::new。
		 * 小结
		 * FunctionalInterface允许传入：
		 * 接口的实现类（传统写法，代码较繁琐）；
		 * Lambda表达式（只需列出参数名，由编译器推断类型）；
		 * 符合方法签名的静态方法；
		 * 符合方法签名的实例方法（实例类型被看做第一个参数类型）；
		 * 符合方法签名的构造方法（实例类型被看做返回类型）。
		 * FunctionalInterface不强制继承关系，不需要方法名称相同，只要求方法参数（类型和数量）与方法返回类型相同，即认为方法签名相同。
		 */
		List<String> names = new ArrayList<String>();
		Collections.addAll(names, "Bob", "Alice", "Tim");
		List<Human> humans = (List<Human>)names.stream().map(Human::new).collect(Collectors.toList());
		System.out.println(humans);
	}
	
	static int cmp(String s1, String s2) {
		return s1.compareTo(s2);
	}
}
