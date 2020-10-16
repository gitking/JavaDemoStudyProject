package com.yale.test.lambda.strem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 使用reduce
 * map()和filter()都是Stream的转换方法，而Stream.reduce()则是Stream的一个聚合方法，它可以把一个Stream的所有元素按照聚合函数聚合成一个结果。
 * 我们来看一个简单的聚合方法：
 * @author dell
 */
public class StreamReduce {
	public static void main(String[] args) {
		/*
		 * reduce()方法传入的对象是BinaryOperator接口，它定义了一个apply()方法，负责把上次累加的结果和本次的元素 进行运算，并返回累加的结果：
		 * @FunctionalInterface
			public interface BinaryOperator<T> {
			    // Bi操作：两个输入，一个输出
			    T apply(T t, T u);
			}
			上述代码看上去不好理解，但我们用for循环改写一下，就容易理解了：
			Stream<Integer> stream = ...
			int sum = 0;
			for (n : stream) {
			    sum = (sum, n) -> sum + n;
			}
		 * 可见，reduce()操作首先初始化结果为指定值（这里是0），紧接着，reduce()对每个元素依次调用(acc, n) -> acc + n，其中，acc是上次计算的结果：
		 * // 计算过程:
			acc = 0 // 初始化为指定值
			acc = acc + n = 0 + 1 = 1 // n = 1
			acc = acc + n = 1 + 2 = 3 // n = 2
			acc = acc + n = 3 + 3 = 6 // n = 3
			acc = acc + n = 6 + 4 = 10 // n = 4
			acc = acc + n = 10 + 5 = 15 // n = 5
			acc = acc + n = 15 + 6 = 21 // n = 6
			acc = acc + n = 21 + 7 = 28 // n = 7
			acc = acc + n = 28 + 8 = 36 // n = 8
			acc = acc + n = 36 + 9 = 45 // n = 9
		 * 因此，实际上这个reduce()操作是一个求和。
		 */
		int sum = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).reduce(0, (acc, n) -> acc + n);
		System.out.println("sum的值为:" + sum);
		
		/*
		 * 如果去掉初始值，我们会得到一个Optional<Integer>：
		 */
		Optional<Integer> opt = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).reduce((acc, n) -> acc + n);
		//这是因为Stream的元素有可能是0个，这样就没法调用reduce()的聚合函数了，因此返回Optional对象，需要进一步判断结果是否存在。
		if (opt.isPresent()) {
			System.out.println(opt.get());
		}
		//利用reduce()，我们可以把求和改成求积，代码也十分简单：注意：计算求积时，初始值必须设置为1。
		int s = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).reduce(1, (acc, n) -> acc * n);
		System.out.println("s的乘积值为:" + s);//362880
		/*
		 * 除了可以对数值进行累积计算外，灵活运用reduce()也可以对Java对象进行操作。下面的代码演示了如何将配置文件的每一行配置
		 * 通过map()和reduce()操作聚合成一个Map<String, String>：
		 * 小结
		 * reduce()方法将一个Stream的每个元素依次作用于BinaryOperator，并将结果合并。
		 * reduce()是聚合方法，聚合方法会立刻对Stream进行计算。
		 */
		List<String> props = new ArrayList<String>();
		props.add("profile=native");
		props.add("debug=true");
		props.add("loggin=warn");
		props.add("interval=500");
		Map<String, String> map = props.stream().map(kv ->{//把k=v转换为Map[k]=v:
			String[] ss = kv.split("\\=", 2);
			/*
			 * Java8 及以下版本无 Map.of()
			 * Java8 及以下版本 return Map.of(ss[0], ss[1]);
			 * 可替换成
			 * return Collections.singletonMap(ss[0], ss[1]);
			 */
			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put(ss[0], ss[1]);
			return resMap;
		}).reduce(new HashMap<String, String>(), (m, kv) -> {//把所有Map聚合到一个Map:
			m.putAll(kv);
			return m;
		});
		map.forEach((k, v)->{//打印结果
			System.out.println(k + " = " + v);
		});
		
		
		/*
		 * 这里会报错
		 * 原因是：流的特点是每个元素只遍历一次，聚合操作无非就是一个遍历+累加计算的过程，内部已经自动帮你遍历了。你想再遍历一遍，那只能构造一个新的stream
		 */
		Stream<Integer> st = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		st.reduce(0, (acc, n) -> acc +n);
		st.forEach(System.out::println);

	}
}
