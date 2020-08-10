package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/*
 * Java的数组有几个特点：
    数组所有元素初始化为默认值，整型都是0，浮点型是0.0，布尔型是false；
    数组一旦创建后，大小就不可改变。
    数组元素可以是值类型（如int）或引用类型（如String），但数组本身是引用类型；
    数组是引用类型，在使用索引访问数组元素时，如果索引超出范围，运行时将报错：
    https://www.liaoxuefeng.com/wiki/1252599548343744/1255941599809248
    当你申请数组的话,底层内存管理器实际上在内存中给你开辟了一段连续的地址,每一个地址就直接可以通过内存管理器进行访问.直接访问数组的第一个元素和访问中间的任何一个元素时间复杂度都是一样的
    也就是常数时间称为O(1),数组可以随机的访问任何一个元素,速度非常快.问题在添加和删除元素的时候,插入元素的时候,时间复杂度是O(n),因为
    你要插入指定位置的时候,需要挪动数组里面的其他元素
  LinkedList(链表)的插入和删除元素操作时间复杂度是O(1)常数时间复杂度,但是如果你要访问LinkedList的中间元素的话,LinkedList的元素指针next
  要一步一步的往下挪,那么访问元素的复杂度就是线性n,O(n)的复杂度
 * SkipList跳表:跳表是基于链表的:空间换时间
 * 	如果链表元素有序的时候如图
 * 跳表的使用只能用于链表里的元素有序的情况下或者跳表里面的元素必须是有序的,所以跳表对标的是平衡树也就是二叉搜索树中的平衡树和二分查找，
 * 这里指的是跳表是一种插入删除搜索都是O(logn)的数据结构,Redis使用的跳表替代平衡树
 * 平衡树二分查找的话也是用来作用于有一堆元素他们是有序的情况下怎么更加有效的查找你要的元素
 * 一维的数据结构要加速的话,经常采用的方式就是升维也就是说变成二维,为什么要多一个维度呢?因为你多了一个维度之后就会有多一级的信息在里面,这样多一级的信息就可以
 * 帮助你可以很快滴得到一维里面你必须挨个走才能走到的那些元素
 * 跳表查询的时间复杂度分析:
 * 1,n/2,n/4,n/8 第k级索引结点的个数就是n/(2^k)
 * 2,假设索引有n级,最高级的索引有2个结点.n/(2^h)=2,从而求得h=log2(n)+1,结论就是logn的时间复杂度
 * SkipList跳表的维护成本比较高,增加和删除都需要重建索引,在这种过程中它在增加和删除的话,它的时间复杂度就会变成logn了。
 */
public class ArraysTest {
	public static void main(String[] args) {
		int[] ns = new int[] { 68, 79, 91, 85, 62 };
	    System.out.println(ns.length); // 编译器自动推算数组大小为5
	    int[] arrTest;
	    arrTest = new int[]{1,2,3,4,5};
	    System.out.println("数组长度:" + arrTest.length);
	    
	    arrTest = new int[]{1,2,3};
	    System.out.println("数组长度:" + arrTest.length);
	    /*
	     * 数组大小变了吗？看上去好像是变了，但其实根本没变。
		对于数组ns来说，执行ns = new int[] { 68, 79, 91, 85, 62 };时，它指向一个5个元素的数组：
		     ns
		      │
		      ▼
		┌───┬───┬───┬───┬───┬───┬───┐
		│   │68 │79 │91 │85 │62 │   │
		└───┴───┴───┴───┴───┴───┴───┘
		执行ns = new int[] { 1, 2, 3 };时，它指向一个新的3个元素的数组：
		     ns ──────────────────────┐
		                              │
		                              ▼
		┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐
		│   │68 │79 │91 │85 │62 │   │ 1 │ 2 │ 3 │   │
		└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘
		但是，原有的5个元素的数组并没有改变，只是无法通过变量ns引用到它们而已。
		https://www.liaoxuefeng.com/wiki/1252599548343744/1255941599809248
	     */
	    
	    
	    /*
	     * 字符串数组
如果数组元素不是基本类型，而是一个引用类型，那么，修改数组元素会有哪些不同？
字符串是引用类型，因此我们先定义一个字符串数组：
	     */
	    
	    String[] names = {"ABC", "XYZ", "zoo" };
	    String sa = names[1] ;
	    names[1] = "cat";
        System.out.println("s是XYZ还是cat?->:" + sa); // s是"XYZ"还是"cat"?
	    /*
	     * 对于String[]类型的数组变量names，它实际上包含3个元素，但每个元素都指向某个字符串对象：
          ┌─────────────────────────┐
    names │   ┌─────────────────────┼───────────┐
      │   │   │                     │           │
      ▼   │   │                     ▼           ▼
┌───┬───┬─┴─┬─┴─┬───┬───────┬───┬───────┬───┬───────┬───┐
│   │░░░│░░░│░░░│   │ "ABC" │   │ "XYZ" │   │ "zoo" │   │
└───┴─┬─┴───┴───┴───┴───────┴───┴───────┴───┴───────┴───┘
      │                 ▲
      └─────────────────┘
对names[1]进行赋值，例如names[1] = "cat";，效果如下：
          ┌─────────────────────────────────────────────────┐
    names │   ┌─────────────────────────────────┐           │
      │   │   │                                 │           │
      ▼   │   │                                 ▼           ▼
┌───┬───┬─┴─┬─┴─┬───┬───────┬───┬───────┬───┬───────┬───┬───────┬───┐
│   │░░░│░░░│░░░│   │ "ABC" │   │ "XYZ" │   │ "zoo" │   │ "cat" │   │
└───┴─┬─┴───┴───┴───┴───────┴───┴───────┴───┴───────┴───┴───────┴───┘
      │                 ▲
      └─────────────────┘
这里注意到原来names[1]指向的字符串"XYZ"并没有改变，仅仅是将names[1]的引用从指向"XYZ"改成了指向"cat"，其结果是字符串"XYZ"再也无法通过names[1]访问到了。
	     */
	    
		int [] dataA = new int[]{1,2,3,4,5};
		int [] dataB = new int[]{1,2,3,4,5};
		System.out.println("把数组变成字符串输出:" + Arrays.toString(dataA));
		System.out.println("比较俩个数组是否相等(如果数组里面的元素一样,位置不一样也是不相等的):" + Arrays.equals(dataA, dataB));
		System.out.println("二分查找法(元素不在数组里面就是找不到的话,返回负数):" + Arrays.binarySearch(dataA, 5));
		
		
		System.out.println("fill填充:");
		int[] dataFill = new int[5];
		Arrays.fill(dataFill, 5);
		System.out.println("fill就是用一个指定的数字把数组填满:" + Arrays.toString(dataFill));

		int [] dataC = new int[]{6,9,10,22,1,2,3,4,5};
		Arrays.sort(dataC);
		System.out.println("排序后的数组:" + Arrays.toString(dataC));
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("a", "A");
		paramMap.put("b", "B");
		paramMap.put("c", "C");
		String []paramNames = new String[paramMap.size()];
		paramMap.keySet().toArray(paramNames);//软通框架JAR包里面看到的
		System.out.println("数组转成String" + Arrays.toString(paramNames));
		
		/*
		 * 对数组进行排序是程序中非常基本的需求。常用的排序算法有冒泡排序、插入排序和快速排序等。
		 * 我们来看一下如何使用冒泡排序算法对一个整型数组从小到大进行排序：
		 * 冒泡排序的特点是，每一轮循环后，最大的一个数被交换到末尾，因此，下一轮循环就可以“刨除”最后的数，每一轮循环都比上一轮循环的结束位置靠前一位。
		 */
		int[] nsSort = { 28, 12, 89, 73, 65, 18, 96, 50, 8, 36 };
        // 排序前:
        System.out.println("冒泡排序前:" + Arrays.toString(nsSort));
        for (int i = 0; i < nsSort.length - 1; i++) {
            for (int j = 0; j < nsSort.length - i - 1; j++) {
                if (nsSort[j] > nsSort[j+1]) {
                    // 交换ns[j]和ns[j+1]:
                    int tmp = nsSort[j];
                    nsSort[j] = nsSort[j+1];
                    nsSort[j+1] = tmp;
                }
            }
        }
        // 排序后:
        System.out.println("冒泡排序前:" + Arrays.toString(nsSort));
        //二维数组,二维数组的每个数组元素的长度并不要求相同
        int[][] arrayEr = new int[][]{{1, 2, 3}, {4, 5, 6, 7}};
        System.out.println("二维数组:" + arrayEr[0][1]);
        for (int[] arr : arrayEr) {
            for (int n : arr) {
                System.out.print(n);
                System.out.print(',');
            }
            System.out.println();
        }
        
        System.out.println("要打印一个二维数组:" + Arrays.deepToString(arrayEr));
        
        //理论上，我们可以定义任意的N维数组。但在实际应用中，除了二维数组在某些时候还能用得上，更高维度的数组很少使用。
        int[][][] arraySec = new int[][][]{{{1,2,3}, {4, 5, 6}}, {{7,8,9}, {10, 11, 12}}};
        System.out.println("要打印一个三维数组:" + Arrays.deepToString(arraySec));
        //java Main -version 命令行传参数,后面的-version可以传多个参数,以空格隔开
        
        // 把List变为Array有三种方法，第一种是调用toArray()方法直接返回一个Object[]数组：
        
        List<Integer> listStr = new ArrayList<Integer>();
        Collections.addAll(listStr, 1, 2, 3);
        Object[] objArr = listStr.toArray();//第一种
        
        /*
         * 注意到这个toArray(T[])方法的泛型参数<T>并不是List接口定义的泛型参数<E>，所以，我们实际上可以传入其他类型的数组，例如我们传入Number类型的数组，返回的仍然是Number类型：
         * 如果我们传入的数组大小和List实际的元素个数不一致怎么办？根据List接口的文档(https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/List.html#toArray%28T%5B%5D%29)，我们可以知道：
         * 如果传入的数组不够大，那么List内部会创建一个新的刚好够大的数组，填充后返回；如果传入的数组比List元素还要多，那么填充完元素后，剩下的数组元素一律填充null。
         */
        Integer[] intArr = listStr.toArray(new Integer[2]);
        //实际上，最常用的是传入一个“恰好”大小的数组：
        Number[] numArr = listStr.toArray(new Number[listStr.size()]);
        
        //但是，如果我们传入类型不匹配的数组，例如，String[]类型的数组，由于List的元素是Integer，所以无法放入String数组，这个方法会抛出ArrayStoreException。
        String[] strArr = listStr.toArray(new String[3]);
        
        //最后一种更简洁的写法是通过List接口定义的T[] toArray(IntFunction<T[]> generator)方法：
        //Integer[] intArrEasy = listStr.toArray(Integer[]::new);
        
        //把Array变为List就简单多了，通过List.of(T...)方法最简单：
        Integer[] intArrList = new Integer[]{1,2,2,2,2};
        //List<Integer> listIntArr = List.of(intArrList);
        
        //对于JDK 11之前的版本，可以使用Arrays.asList(T...)方法把数组转换成List。
        //要注意的是，返回的List不一定就是ArrayList或者LinkedList，因为List只是一个接口，如果我们调用List.of()，它返回的是一个只读List：
        //对只读List调用add()、remove()方法会抛出UnsupportedOperationException。
        List<Integer> listIntArr = Arrays.asList(intArrList);
	}
}
