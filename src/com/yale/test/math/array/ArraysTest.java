package com.yale.test.math.array;

import java.util.Arrays;
import java.util.HashMap;
/*
 * Java的数组有几个特点：
    数组所有元素初始化为默认值，整型都是0，浮点型是0.0，布尔型是false；
    数组一旦创建后，大小就不可改变。
    数组元素可以是值类型（如int）或引用类型（如String），但数组本身是引用类型；
    数组是引用类型，在使用索引访问数组元素时，如果索引超出范围，运行时将报错：
    https://www.liaoxuefeng.com/wiki/1252599548343744/1255941599809248
    
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
	}
}
