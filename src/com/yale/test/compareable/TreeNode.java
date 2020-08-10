package com.yale.test.compareable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的树怎么定义就是这个类
 * 其实BinaryTree里面的BinaryTree类里面的Node定义的就挺好
 * 二叉树遍历有三种方法:
 * 1,前序(Pre-order):根-左-右
 * 2,中序(In-order):左-根-右
 * 3,后序(Post-order):左-右-根
 * 还有二叉搜索树Binary Search Tree:又叫二叉排序树,有序二叉树(Ordered Binary Tree),排序二叉树(Sorted Binary Tree),
 * 是指一颗空树或者具有以下性质的二叉树:
 * 1,在左子树上所有结点的值均小于它的跟结点的值;
 * 2,在右子树上所有结点的值均大于它的跟结点的值;
 * 3,以此类推:左,右子树也分别为二叉查找树.(这就是重复性)
 * 那么它的中序遍历就是升序排列,二叉搜索树不管是查询还是删除操作都是logn的非常快
 * https://visualgo.net/zh/bst
 * @author dell
 * 树的面试题解法一般都是递归:
 * 1,树的节点的定义,它的数据结构的定义就是用递归的方式来进行的
 * 2,重复性(自相似性):不仅是树本身以及二叉树以及二叉搜索树它在定义它数据结构和它的算法特性的时候也是有所谓的重复性
 * 递归:计算机语言在创造的时候它本质上就是汇编,汇编有个特点就是说汇编没有所谓的循环嵌套这么一说,很多时候它更多的用的就是
 * 你之前有一段函数写在什么地方或者你一段指令写在什么地方,你就直接不断的反复跳到之前的那段指令不断地去执行其实这就是所谓的递归
 * 而循环本身的话你可以把他编译出来你看它的汇编代码的话其实和递归本身有异曲同工之处,所以递归和循环没有明显的边界
 */
public class TreeNode {
	public int val;
	public TreeNode left;
	public TreeNode right;
	public TreeNode(int val){
		this.val = val;
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode(1);
		List<Integer> resultList = new ArrayList<Integer>();
		inOrder(root, resultList);
	}
	
	/*
	 * 二叉树的中序遍历,使用递归,递归调用相当于让系统帮忙我们维护一个栈,
	 * 时间复杂度：O(n)O(n)O(n)。递归函数 T(n)=2⋅T(n/2)+1T(n) = 2 \cdot T(n/2)+1T(n)=2⋅T(n/2)+1。
	 * 空间复杂度：最坏情况下需要空间O(n)O(n)O(n)，平均情况为O(log⁡n)O(\log n)O(logn)。
		作者：LeetCode
		链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/er-cha-shu-de-zhong-xu-bian-li-by-leetcode/
		来源：力扣（LeetCode）
		著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	public static void inOrder(TreeNode root, List<Integer> res) {
		if (root != null) {
			if (root.left != null) {
				inOrder(root.left, res);
			}
			res.add(root.val);
			if (root.right != null) {
				inOrder(root.right, res);
			}
		}
	}
	
	/*
	 * 方法二：基于栈的遍历
	 * 本方法的策略与上面的方法inOrder很相似，区别是使用了栈。
	 * 复杂度分析
	 * 时间复杂度：O(n)O(n)O(n)。
	 * 空间复杂度：O(n)O(n)O(n)。
	 */
	public static List<Integer> inorderTraversal(TreeNode root){
		List<Integer> resultList = new ArrayList<Integer>();
		Stack<TreeNode> stack= new Stack<TreeNode>();
		TreeNode curr = root;
		while (curr != null  || !stack.isEmpty()) {
			while(curr != null) {
				stack.push(curr);
				curr = curr.left;
			}
			curr = stack.pop();//把栈顶的元素弹出(删除)
			resultList.add(curr.val);
			curr = curr.right;
		}
		return resultList;
	}
	
	/*
	 * 官方题解中介绍了三种方法来完成树的中序遍历，包括：
	 *  递归:在树的深度优先遍历中（包括前序、中序、后序遍历），递归方法最为直观易懂
	 *  借助栈的迭代方法：栈迭代方法虽然提高了效率，但其嵌套循环却非常烧脑，不易理解，容易造成“一看就懂，一写就废”的窘况。而且对于不同的遍历顺序（前序、中序、后序），循环结构差异很大，更增加了记忆负担。
	 *  莫里斯遍历:略
	 *  因此，我在这里介绍一种“颜色标记法”（瞎起的名字……），兼具栈迭代方法的高效，又像递归方法一样简洁易懂，更重要的是，这种方法对于前序、中序、后序遍历，能够写出完全一致的代码。
	 *  其核心思想如下：
		    使用颜色标记节点的状态，新节点为白色，已访问的节点为灰色。
		    如果遇到的节点为白色，则将其标记为灰色，然后将其右子节点、自身、左子节点依次入栈。
		    如果遇到的节点为灰色，则将节点的值输
	 * 作者：hzhu212
	 * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/yan-se-biao-ji-fa-yi-chong-tong-yong-qie-jian-ming/
	 * 来源：力扣（LeetCode）
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 * @return
	 */
	public List<Integer> inorderTraversalColor(TreeNode root){
		if (root == null) {
			return new ArrayList<Integer>();
		}
		List<Integer> resultList = new ArrayList<Integer>();
		Stack<ColorNode> stack = new Stack<ColorNode>();
		stack.push(new ColorNode("white", root));
		while (!stack.isEmpty()) {
			ColorNode cn = stack.pop();
			if (cn.color.equals("white")) {
				if (cn.node.right != null) {
					stack.push(new ColorNode("white", cn.node.right));
				}
				stack.push(new ColorNode("gray", cn.node));
				if (cn.node.left != null) {
					stack.push(new ColorNode("white", cn.node.left));
				}
			} else {
				resultList.add(cn.node.val);
			}
		}
		return resultList;
	}
	
	class ColorNode {
		TreeNode node;
		String color;
		public ColorNode(String color, TreeNode treeNode) {
			this.color = color;
			this.node = treeNode;
		}
	}
}	
