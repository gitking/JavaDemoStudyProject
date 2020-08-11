package com.yale.test.leetcode;

/*
 * 各种排序算法一定要会，一定要会，一定要会，重要的问题说三遍,但是让你写个归并排序，写个快排你如果吭哧半天也写不出来，那说不过去了
 * 图解：什么是快速排序？ https://mp.weixin.qq.com/s/xEmtuRnAPoEHGrOBO__b0w
 * https://visualgo.net/zh/bst?slide=1
 * https://visualgo.net/zh/bst?slide=1
 * Redis 设计与实现 »:https://redisbook.readthedocs.io/en/latest/internal-datastruct/skiplist.html
 * LRU缓存算法:https://www.jianshu.com/p/b1ab4a170c3c
 * 极客时间:算法训练营资料
 * 爬楼梯:https://leetcode-cn.com/problems/climbing-stairs/solution/pa-lou-ti-by-leetcode-solution/
 * 589. N叉树的前序遍历:https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/yi-tao-quan-fa-shua-diao-nge-bian-li-shu-de-wen--3/
 * 22. 括号生成: https://leetcode-cn.com/problems/generate-parentheses/
 * 17. 电话号码的字母组合:https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
 * 11. 盛最多水的容器:https://leetcode-cn.com/problems/container-with-most-water/
 * 70. 爬楼梯:https://leetcode-cn.com/problems/container-with-most-water/solution/sheng-zui-duo-shui-de-rong-qi-by-leetcode-solution/
 * 46. 全排列:https://leetcode-cn.com/problems/permutations/
 * 47. 全排列 II https://leetcode-cn.com/problems/permutations-ii/
 * 589. N叉树的前序遍历:https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/submissions/
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/submissions/
 * 26. 删除排序数组中的重复项:https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/
 * 104. 二叉树的最大深度 https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/
 * 98. 验证二叉搜索树 https://leetcode-cn.com/problems/validate-binary-search-tree/
 * 剑指 Offer 33. 二叉搜索树的后序遍历序列 https://leetcode-cn.com/problems/er-cha-sou-suo-shu-de-hou-xu-bian-li-xu-lie-lcof/
 * 剑指 Offer 62. 圆圈中最后剩下的数字 https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/
 * 1001. 网格照明 https://leetcode-cn.com/problems/grid-illumination/
 * 468. 验证IP地址 https://leetcode-cn.com/problems/validate-ip-address/
 * 93. 复原IP地址 https://leetcode-cn.com/problems/restore-ip-addresses/
 * 393. UTF-8 编码验证 https://leetcode-cn.com/problems/utf-8-validation/
 * 65. 有效数字 https://leetcode-cn.com/problems/valid-number/
 * 36. 有效的数独 https://leetcode-cn.com/problems/valid-sudoku/
 * 144. 二叉树的前序遍历 https://leetcode-cn.com/problems/binary-tree-preorder-traversal/
 * 589. N叉树的前序遍历 https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/
 * 146. LRU缓存机制:https://leetcode-cn.com/problems/lru-cache/
 * 51. N皇后 https://leetcode-cn.com/problems/n-queens/
 * 面试题 08.12. 八皇后 https://leetcode-cn.com/problems/eight-queens-lcci/
 * 52. N皇后 II https://leetcode-cn.com/problems/n-queens-ii/
 * 77. 组合 https://leetcode-cn.com/problems/combinations/
 * 105. 从前序与中序遍历序列构造二叉树 https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 * 226. 翻转二叉树 https://leetcode-cn.com/problems/invert-binary-tree/description/
 * 236. 二叉树的最近公共祖先 https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/
 * 297. 二叉树的序列化与反序列化 https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/
 * 1222. 可以攻击国王的皇后 https://leetcode-cn.com/problems/queens-that-can-attack-the-king/
 * 为啥 redis 使用跳表(skiplist)而不是使用 red-black？:https://www.zhihu.com/question/20202931
 * 平衡二叉树、B树、B+树、B*树 理解其中一种你就都明白了:https://zhuanlan.zhihu.com/p/27700617
 * 如何理解算法时间复杂度的表示法，例如 O(n²)、O(n)、O(1)、O(nlogn) 等？：https://www.zhihu.com/question/21387264
 * 主定理:https://zh.wikipedia.org/wiki/%E4%B8%BB%E5%AE%9A%E7%90%86
 * 主定理:https://en.wikipedia.org/wiki/Master_theorem_(analysis_of_algorithms)
 * https://shimo.im/sheets/JxFgd6m6NWgry5Ci/MODOC
 * 	数组	简单	两数之和			https://leetcode-cn.com/problems/two-sum/
		中等	三数之和			https://leetcode-cn.com/problems/3sum/
	链表	简单	环形链表			https://leetcode-cn.com/problems/linked-list-cycle
		中等	两两交换链表中的节点	https://leetcode-cn.com/problems/swap-nodes-in-pairs
	二叉树	简单	二叉树的最大深度	https://leetcode-cn.com/problems/maximum-depth-of-binary-tree
		简单	二叉树的最小深度		https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
		中等	二叉树的层序遍历		https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
	递归	简单	第 N 个泰波那契数		https://leetcode-cn.com/problems/n-th-tribonacci-number/
		中等	括号生成		https://leetcode-cn.com/problems/generate-parentheses/
 * 算法体验营-结课考试:https://shimo.im/docs/hcyHjvpRJVyWj9pP/read
 */
public class ClimStairs {
	public static void main(String[] args) {
		/*
		 * 极客时间覃超爬楼梯分别:
		 * 假如有N级台阶,那么到达N级台阶的办法只有俩种,注意是到达N级台阶终点的方式就只有俩种:
		 * 第一种:最后一步是一次爬一级台阶到达N层
		 * 第二种:最后一步是一次爬二级台阶到达N层
		 * 也就是说你爬的N级台阶的话,它的总的走法,只可能是最后一步是一次爬一级台阶到达N层的总走法或者最后一步是一次爬二级台阶到达N层的总走法
		 * 所以上N级台阶的就等于最后一步是一次爬一级台阶到达N层的总走法+最后一步是一次爬二级台阶到达N层的总走法：f(n)=f(n-1)+f(n-2)其实就是Fibonacci斐波那契数列
		 * 英文叫mutal exclusive, complete exhaustive意思是所有这些分项他们都是互斥的且他们加在一起把所有的可能性都已经包含了
		 */
		System.out.println("爬2个楼梯有几种方法:" + climStairs(2));
	}
	
	/*
	 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
	 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
	 * 注意：给定 n 是一个正整数。
	 * 复杂度分析
	 * 时间复杂度：循环执行 n 次，每次花费常数的时间代价，故渐进时间复杂度为 O(n)。
	 * 空间复杂度：这里只用了常数个变量作为辅助空间，故渐进空间复杂度为 O(1)。
	 * 作者：LeetCode-Solution
 	 * 链接：https://leetcode-cn.com/problems/climbing-stairs/solution/pa-lou-ti-by-leetcode-solution/
	 * 来源：力扣（LeetCode）
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	public static int climStairs(int n) {
		int p=0, q=0, r=1;
		for (int i=1; i<=n; i++) {
			p = q;
			q = r;
			r = p + q;
		}
		return r;
	}
	
	
	/*
	 * 暴力法
	 * 时间复杂度为:O(2的n次方),树形递归的大小为2的n次方
	 * 空间复杂度:O(n),递归树的深度可以达到n.
	 */
	public static int climbStairs2(int n){
		return climb_Stairs(0, n);
	}
	
	/*
	 * 暴力法
	 * 时间复杂度为:O(2的n次方),树形递归的大小为2的n次方
	 */
	public static int climb_Stairs(int i, int n) {
		if(i > n) {
			return 0;
		}
		if (i == n) {
			return 1;
		}
		return climb_Stairs(i+1, n) + climb_Stairs(i+2, n);
	}
	
	/*
	 * 记忆化递归,记忆化搜索
	 * 时间复杂度:O(n),树形递归的大小可以达到n
	 * 空间复杂度:O(n),递归树的深度可以达到n.
	 */
	public static int climbStairsMem(int n){
		int memo[] = new int[n + 1];
		return climb_StairsMem(0, n, memo);
	}
	
	/*
	 * 记忆化递归,记忆化搜索
	 */
	public static int climb_StairsMem(int i, int n, int memo[]) {
		if (i > n) {
			return 0;
		}
		if (i == n) {
			return 1;
		}
		if (memo[i] > 0) {
			return memo[i];
		}
		memo[i] = climb_StairsMem(i+1, n, memo) + climb_StairsMem(i+2, n, memo) ;
		return memo[i];
	}
	
	/*
	 * 动态规划
	 * 时间复杂度:O(n),单循环到n
	 * 空间复杂度:O(n),dp的数组用了n的空间
	 */
	public static int climbStairsDyan(int n){
		if (n==1) {
			return 1;
		}
		int[] dp = new int[n+1];
		dp[1]=1;
		dp[2]=2;
		for(int i=3; i<=n; i++) {
			dp[i] = dp[i-1] + dp[i-2];
		}
		return dp[n];
	}
	
	/*
	 * 斐波那契数列
	 * 时间复杂度:O(n),单循环到n,需要计算n个菲波那切数列
	 * 空间复杂度:O(1),使用常量级空间.
	 */
	public static int climbStairs4(int n){
		if (n==1) {
			return 1;
		}
		int first =1;
		int second =2;
		for (int i=3; i<=n; i++) {
			int third = first + second;
			first = second;
			second = third;
		}
		return second;
	}
	
	/*
	 * Python实现爬楼
	 * Python中的range是生成数字序列
	 * range(2, 5)生成的数字序列为2, 3, 4
	 */
//	def climbStairs(self, n) {
//		if (n <=2): return n
//		f1, f2, f3 = 1, 2, 3
//		for i in range(3, n+1)
//			f3 = f1 + f2
//			f1 = f2
//			f2 = f3
//		return f3
//	}
}
