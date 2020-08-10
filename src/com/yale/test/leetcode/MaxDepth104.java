package com.yale.test.leetcode;

import java.util.LinkedList;
import java.util.Queue;

import com.yale.test.compareable.TreeNode;

public class MaxDepth104 {

	public static void main(String[] args) {

	}
	
	/*
	 * 给定一个二叉树，找出其最大深度DFS。
	 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
	 *	说明: 叶子节点是指没有子节点的节点。
	 * 示例：
	 *	给定二叉树 [3,9,20,null,null,15,7]，
	 * 返回它的最大深度 3 。
	 * 复杂度分析
     * 时间复杂度：O(n)，其中 nnn 为二叉树节点的个数。每个节点在递归中只被遍历一次。
     * 空间复杂度：O(height)，其中 heigh 表示二叉树的高度。递归函数需要栈空间，而栈空间取决于递归的深度，因此空间复杂度等价于二叉树的高度。
	 * 来源：力扣（LeetCode）
	 * 链接：https://leetcode-cn.com/problems/maximum-depth-of-binary-tree
	 * https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/solution/er-cha-shu-de-zui-da-shen-du-by-leetcode-solution/
	 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
	 */
	public static int maxDepth(TreeNode root) {
		if (root == null) {
            return 0;
        } else {
            int leftHeight = maxDepth(root.left);
            int rightHeight = maxDepth(root.right);
            return Math.max(leftHeight, rightHeight) + 1;
        }
	}
	
   public int maxDepth2(TreeNode root) {
	   return root == null ? 0 : Math.max(this.maxDepth(root.left), this.maxDepth(root.right)) + 1;
   }
   
   /*
    * 方法二：广度优先搜索
	* 思路与算法
	* 我们也可以用「广度优先搜索」的方法来解决这道题目，但我们需要对其进行一些修改，此时我们广度优先搜索的队列里存放的是「当前层的所有节点」。
	* 每次拓展下一层的时候，不同于广度优先搜索的每次只从队列里拿出一个节点，我们需要将队列里的所有节点都拿出来进行拓展，这样能保证每次拓展完的时候队列里存放的是当前层的所有节点，
	* 即我们是一层一层地进行拓展，最后我们用一个变量 ans 来维护拓展的次数，该二叉树的最大深度即为 ans。
	* 作者：LeetCode-Solution
	* 链接：https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/solution/er-cha-shu-de-zui-da-shen-du-by-leetcode-solution/
	* 来源：力扣（LeetCode）
	* 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    */
   public int maxDepthSec(TreeNode root) {
	   if (root == null) {
           return 0;
       }
       Queue<TreeNode> queue = new LinkedList<TreeNode>();
       queue.offer(root);
       int ans = 0;
       while (!queue.isEmpty()) {
           int size = queue.size();
           while (size > 0) {
               TreeNode node = queue.poll();
               if (node.left != null) {
                   queue.offer(node.left);
               }
               if (node.right != null) {
                   queue.offer(node.right);
               }
               size--;
           }
           ans++;
       }
       return ans;
   }
}
