package com.yale.test.leetcode;

/**
 * 98. 验证二叉搜索树 https://leetcode-cn.com/problems/validate-binary-search-tree/
 * 给定一个二叉树，判断其是否是一个有效的二叉搜索树。
	假设一个二叉搜索树具有如下特征：
	    节点的左子树只包含小于当前节点的数。
	    节点的右子树只包含大于当前节点的数。
	    所有左子树和右子树自身必须也是二叉搜索树。
		示例 1:
		
		输入:
		    2
		   / \
		  1   3
		输出: true
	示例 2:
	输入:
	    5
	   / \
	  1   4
	     / \
	    3   6
	输出: false
	解释: 输入为: [5,1,4,null,null,3,6]。
	     根节点的值为 5 ，但是其右子节点值为 4 。
来源：力扣（LeetCode）
链接：https://leetcode-cn.com/problems/validate-binary-search-tree
著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author dell
 * BST:二叉搜索树Binary Search Tree
 * BST --> 中序遍历是递增的
 * 二叉搜索树不仅仅要看它的左儿子节点和右儿子节点和跟节点大小还要看整个左子树右子树的大小
 */
public class ValidBST98 {
	public static void main(String[] args) {

	}
}
