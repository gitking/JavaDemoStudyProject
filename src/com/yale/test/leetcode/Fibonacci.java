package com.yale.test.leetcode;

/**
 *  斐波那契数列指的是这样一个数列 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233，377，610，987，1597，2584，4181，6765，10946，17711，28657，46368……
	特别指出：第0项是0，第1项是第一个1。
	这个数列从第三项开始，每一项都等于前两项之和。
	https://www.runoob.com/java/java-examples.html
 * 数列大全的搜索结果：https://oeis.org/search?q=3++4++6++8++12++14++18&language=english&go=Search
 * @游识猷 读到一个数字谜题——
 * 3  4  6  8  12  14  18
 * 这个数字序列可以用一个非常简单的规律来概括，你猜得出吗？
 * 按照这个规律的话，下一个数字是什么？ 
 */
public class Fibonacci {
	public static void main(String[] args) {
		for (int i=0; i<=20; i++) {
			System.out.printf("斐波那契数列 Fibonacci of %d is :%d\n", i, fibonacci(i));
		}
	}
	
	public static long fibonacci(long number){
		if (number == 0 || number == 1) {
			return number;
		} else {
			return fibonacci(number-1) + fibonacci(number - 2);
		}
	}
	
	/*
	 * 这段代码的时间复杂度是O(1),因为不管n的值是多少.
	 * 方法里面的println只会执行一次.
	 */
	public static void pintln(int n) {
		System.out.println("Hey - your input is:" + n);
	}
	
	/*
	 * 这段代码的时间复杂度是O(1)或者你也可以说是O(3)或者叫常数时间复杂度,面试的时候千万不要说O(3),但其实就是O(1).我们不关心前面的常数
	 * 因为不管n的值是多少,方法里面的println只会执行三次.
	 */
	public static void pintlns(int n) {
		System.out.println("Hey - your input is:" + n);
		System.out.println("Hmm.. I'm doing more stuff with:" + n);
		System.out.println("And more:" + n);
	}
	
	public static void ofor(int n) {
		for(int i=0;i<n;i++) {
			System.out.println("代码的执行次数跟n的值有关系,这种时间复杂度为:O(n),叫做O(n)线性时间复杂度");
		}
	}
	
	public static void ofor2(int n) {
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				System.out.println("代码的执行次数跟n的值有关系,这种时间复杂度为:O(n^2),n的平方");
			}
		}
	}
	
	
	public static void ofor3(int n) {
		for(int i=0;i<n;i++) {
			System.out.println("这种有俩个并列的循环,其实就是2n,O(2N),还是同理前面的常数我们不关心,这个时间复杂度还是O(n)的时间复杂度");
		}
		for(int j=0;j<n;j++) {
			System.out.println("这种有俩个并列的循环,其实就是2n,O(2N),还是同理前面的常数我们不关心,这个时间复杂度还是O(n)的时间复杂度");
		}
	}
	
	public static void ofor4(int n) {
		for(int i=0;i<n;i=i*2) {
			System.out.println("如果n的4的话,代码只会循环2次,代码的执行次数永远是log2(n),所以这个代码的时间复杂度为O(logn)");
		}
	}
	
	/*
	 * 菲波那切数列牵扯到了递归调用,递归的时间复杂度怎么计算呢？
	 * 答案是k的n次方,是指数级的,所以递归求菲波那切数列是非常慢的
	 * 要分析递归的时间复杂度,就要分析递归的代码执行了多少次。这个是最简单的实现方式
	 * 这个方法要计算6的值,就要执行fib方法的2的n次方.看图
	 * 可以看到中间有计算了很多重复的数字的结果,可以把这些数字的结果缓存下来,优化这个方法。
	 * 一般在各种递归的情形的话,有这四种情形
	 * 1,二分查找:一般发生在一个数列本身有序的时候,你要在有序的数列里找到你要的目标数,所以他每次都一分为二,只查一边,最后他的时间复杂度是log(n)的,二分查找是log(n)的时间复杂度
	 * 2,如果是二叉树遍历的话,时间复制度为O(n),每次一分为二,但每次一分为二之后每一边它是相等的时间复杂度这么下去最后它的一个递推公式就变成了了图中的。二叉树的遍历我们每一个节点都访问一次
	 * 且仅访问一次,所以它到时间复杂度就是O(n)的
	 * 3,二维矩阵:是排好序的二维矩阵,进行二分查找。最后时间复杂度是O(n),如果是一维的数组进行二分查找就是log(n),
	 * 如果是二维的矩阵进行查找有序的矩阵进行查找同理这时候被降了一维就不是n平方的算法,而是O(n)的算法.
	 * 4,merge sort归并排序:所有排序最优的办法就是nlogn的,时间复杂度为nlogn的时间复杂度
	 * 常见的面试题:
	 * 1,二叉树遍历-前序-中序-后序-:时间复杂度是多少?
	 * 	答:O(n),这里的n代表二叉树里面的节点总数,因为不管是前序中序后序它遍历二叉树的时候,每个节点会访问一次且仅访问一次.
	 * 2,图的遍历:时间复杂度是多少?
	 * 	答:O(n),图里面的每个节点会访问一次且仅访问一次.
	 * 3,搜索算法:DFS,BFS时间复杂度是多少?
	 * 	答:搜索算法DFS是深度优先,BFS是广度优先,不管深度优先或者广度优先它的时间复杂度O(n),这里的N指的是搜索空间里面的节点总数.
	 * 4,二分查找时间复杂度是多少?
	 * 	答:logn
	 */
	public static int fib(int n) {
		if (n<2) {
			return n;
		}
		return fib(n-1) + fib(n-2);
	}
	
	
	/*
	 * 空间复杂度:
	 * 数组的长度:
	 * 1、如果你的代码里面开了数组,那么数组的长度基本上就是你的空间复杂度.
	 * 2、如果你的代码里面了开俩维数组,俩维数组的长度为n的平方,那么你的空间复杂度基本上就是n平方的,
	 * 递归的深度:
	 * 1、递归的最深的深度就是你空间复杂度的最大值
	 * 2、如果你是递归里面又开了数组,那俩者之间的最大值就是你的空间复杂度
	 * 可以看一下leetcode的爬楼梯问题
	 */
	
	
	
	
	
	
	
	/*
	 * 从1到n的和,这个方法的时间复杂度是O(n),效率比较低
	 */
	public static int sum(int n){
		int res = 0;
		for (int i =1;i<n;i++) {
			res = res + i;
		}
		return res;
	}
	
	/*
	 * 从1到n的和,用数学的求和公式sum=n(n+1)/2,时间复杂度是常数复杂度O(1)
	 * 因为这段代码永远都只执行一次且只执行一次
	 */
	public static int sum2(int n){
		int res = n * (n+1)/2;
		return res;
	}
}
