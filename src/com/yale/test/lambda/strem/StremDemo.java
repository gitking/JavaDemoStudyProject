package com.yale.test.lambda.strem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 使用Stream
 * Java从8开始，不但引入了Lambda表达式，还引入了一个全新的流式API：Stream API。它位于java.util.stream包中。
 * 划重点：这个Stream不同于java.io的InputStream和OutputStream，它代表的是任意Java对象的序列。两者对比如下：
 * 		java.io				java.util.stream
 *	存储	顺序读写的byte或char	顺序输出的任意Java对象实例
 *	用途	序列化至文件或网络		内存计算／业务逻辑
 * 有同学会问：一个顺序输出的Java对象序列，不就是一个List容器吗？
 * 再次划重点：这个Stream和List也不一样，List存储的每个元素都是已经存储在内存中的某个Java对象，而Stream输出的元素可能并没有预先存储在内存中，而是实时计算出来的。
 * 换句话说，List的用途是操作一组已存在的Java对象，而Stream实现的是惰性计算，两者对比如下：
 * 		java.util.List		java.util.stream
 *	元素	已分配并存储在内存		可能未分配，实时计算
 *	用途	操作一组已存在的Java对象	惰性计算
 * Stream看上去有点不好理解，但我们举个例子就明白了。
 * 如果我们要表示一个全体自然数的集合，显然，用List是不可能写出来的，因为自然数是无限的，内存再大也没法放到List中：
 * List<BigInteger> list = ??? // 全体自然数?
 * 但是，用Stream可以做到。写法如下：
 * Stream<BigInteger> naturals = createNaturalStream(); // 全体自然数
 * 我们先不考虑createNaturalStream()这个方法是如何实现的，我们看看如何使用这个Stream。
 * 首先，我们可以对每个自然数做一个平方，这样我们就把这个Stream转换成了另一个Stream：
 * Stream<BigInteger> naturals = createNaturalStream(); // 全体自然数
 * Stream<BigInteger> streamNxN = naturals.map(n -> n.multiply(n)); // 全体自然数的平方
 * 因为这个streamNxN也有无限多个元素，要打印它，必须首先把无限多个元素变成有限个元素，可以用limit()方法截取前100个元素，最后用forEach()处理每个元素，这样，我们就打印出了前100个自然数的平方：
 * Stream<BigInteger> naturals = createNaturalStream();
	naturals.map(n -> n.multiply(n)) // 1, 4, 9, 16, 25...
        .limit(100)
        .forEach(System.out::println);
 * 我们总结一下Stream的特点：它可以“存储”有限个或无限个元素。这里的存储打了个引号，是因为元素有可能已经全部存储在内存中，也有可能是根据需要实时计算出来的。
 * Stream的另一个特点是，一个Stream可以轻易地转换为另一个Stream，而不是修改原Stream本身。
 * 最后，真正的计算通常发生在最后结果的获取，也就是惰性计算。
 * Stream<BigInteger> naturals = createNaturalStream(); // 不计算
 * Stream<BigInteger> s2 = naturals.map(BigInteger::multiply); // 不计算
 * Stream<BigInteger> s3 = s2.limit(100); // 不计算
 * s3.forEach(System.out::println); // 计算
 * 惰性计算的特点是：一个Stream转换为另一个Stream时，实际上只存储了转换规则，并没有任何计算发生。
 * 例如，创建一个全体自然数的Stream，不会进行计算，把它转换为上述s2这个Stream，也不会进行计算。再把s2这个无限Stream转换为s3这个有限的Stream，
 * 也不会进行计算。只有最后，调用forEach确实需要Stream输出的元素时，才进行计算。我们通常把Stream的操作写成链式操作，代码更简洁：
 * createNaturalStream().map(BigInteger::multiply).limit(100).forEach(System.out::println);
 * 因此，Stream API的基本用法就是：创建一个Stream，然后做若干次转换，最后调用一个求值方法获取真正计算的结果：
 * int result = createNaturalStream() // 创建Stream
             .filter(n -> n % 2 == 0) // 任意个转换
             .map(n -> n * n) // 任意个转换
             .limit(100) // 任意个转换
             .sum(); // 最终计算结果
 * 小结
 *  Stream API的特点是：
    Stream API提供了一套新的流式处理的抽象序列；
    Stream API支持函数式编程和链式操作；
    Stream可以表示无限序列，并且大多数情况下是惰性求值的。
 * 创建Stream
 * 要使用Stream，就必须现创建它。创建Stream有很多种方法，我们来一一介绍。
 * Stream.of()
 * 创建Stream最简单的方式是直接用Stream.of()静态方法，传入可变参数即创建了一个能输出确定元素的Stream：
 * 小结
 *	创建Stream的方法有 ：
 *	    通过指定元素、指定数组、指定Collection创建Stream；
 *	    通过Supplier创建Stream，可以是无限序列；
 *	    通过其他类的相关方法创建。
 *	基本类型的Stream有IntStream、LongStream和DoubleStream。
 * @author dell
 */
public class StremDemo {

	public static void main(String[] args) {
		Stream<String> stream = Stream.of("A", "B", "C", "D|");
		/*forEach()方法想当于内部循环调用
		 * 可传入符号Consumer接口的void accept(T t)的方法引用
		 * 虽然这种方式基本上没啥实质性用途，但测试的时候很方便。
		 */
		stream.forEach(System.out::println);
		
		/*
		 * 基于数组或Collection
		 * 第二种创建Stream的方法是基于一个数组或者Collection，这样该Stream输出的元素就是数组或者Collection持有的元素：
		 * 把数组变成Stream使用Arrays.stream()方法。对于Collection（List、Set、Queue等），直接调用stream()方法就可以获得Stream。
		 * 下面创建Stream的方法都是把一个现有的序列变为Stream，它的元素是固定的。
		 */
		Stream<String> stream1 = Arrays.stream(new String[]{"A", "B", "C"});
		stream1.forEach(System.out::println);
		List<String> strList = new ArrayList<String>();
		Collections.addAll(strList, "X", "Y", "Z");
		Stream<String> stream2 = strList.stream();
		stream2.forEach(System.out::println);
		
		/*
		 * 基于Supplier
		 * 创建Stream还可以通过Stream.generate()方法,它需要传入一个Supplier对象:
		 * Stream<String> s = Stream.generate(Supplier<String> sp);
		 * 基于Supplier创建的Stream会不断调用Supplier.get()方法来不断产生下一个元素，这种Stream保存的不是元素，而是算法，它可以用来表示无限序列。
		 * 例如，我们编写一个能不断生成自然数的Supplier，它的代码非常简单，每次调用get()方法，就生成下一个自然数：
		 * 下面代码我们用一个Supplier<Integer>模拟了一个无限序列（当然受int范围限制不是真的无限大）。如果用List表示，即便在int范围内，也会占用巨大的内存，
		 * 而Stream几乎不占用空间，因为每个元素都是实时计算出来的，用的时候再算。
		 * 对于无限序列，如果直接调用forEach()或者count()这些最终求值操作，会进入死循环，因为永远无法计算完这个序列，所以正确的方法是先把无限序列变成有限序列，
		 * 例如，用limit()方法可以截取前面若干个元素，这样就变成了一个有限序列，对这个有限序列调用forEach()或者count()操作就没有问题。
		 */
		Stream<Integer> natual = Stream.generate(new NatualSupplier());
		//注意:无限序列必须先变成有限序列再打印:
		natual.limit(20).forEach(System.out::println);
		
		/*
		 * 其他方法
		 * 创建Stream的第三种方法是通过一些API提供的接口，直接获得Stream。
		 * 例如，Files类的lines()方法可以把一个文件变成一个Stream，每个元素代表文件的一行内容：
		 * try (Stream<String> lines = Files.lines(Paths.get("/path/to/file.txt"))) {
			    ...
			}
		 * 此方法对于按行遍历文本文件十分有用。
		 * 另外，正则表达式的Pattern对象有一个splitAsStream()方法，可以直接把一个长字符串分割成Stream序列而不是数组：
		 */
		Pattern p = Pattern.compile("\\s+");
		Stream<String> s = p.splitAsStream("The quick brown fox jumps over the lazy dog");
		s.forEach(System.out::println);
		
		/*
		 * 基本类型
		 * 因为Java的范型不支持基本类型，所以我们无法用Stream<int>这样的类型，会发生编译错误。为了保存int，只能使用Stream<Integer>，但这样会产生频繁的装箱、拆箱操作。
		 * 为了提高效率，Java标准库提供了IntStream、LongStream和DoubleStream这三种使用基本类型的Stream，它们的使用方法和范型Stream没有大的区别，
		 * 设计这三个Stream的目的是提高运行效率：
		 */
		//将int[]数组变为IntStream
		IntStream is = Arrays.stream(new int[]{1, 2, 3});
		//将Stream<String>转换为LongStream
		List<String> longList = new ArrayList<String>();
		Collections.addAll(longList, "1","2","3");
		LongStream ls = longList.stream().mapToLong(Long::parseLong);
		
		// 打印Fibonacci数列：1，1，2，3，5，8，13，21...
		LongStream fib = LongStream.generate(new FibSupplier());
		fib.limit(10).forEach(System.out::println);
	}
}

class NatualSupplier implements Supplier<Integer> {
	int n = 0;
	@Override
	public Integer get() {
		n++;
		return n;
	}
}

class FibSupplier implements LongSupplier {
	int n = 0;
	@Override
	public long getAsLong() {
		n++;
		return fibonacci(n);
	}
	
	public static long fibonacci(long number){
		if (number == 0 || number == 1) {
			return number;
		} else {
			return fibonacci(number-1) + fibonacci(number - 2);
		}
	}
}
