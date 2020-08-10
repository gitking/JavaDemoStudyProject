package com.yale.test.compareable;

import java.util.Arrays;
/**
 * 二叉树原理
 *树的机构:取第一个数据作为树的根节点,然后将比这个根节点大的数据放在节点的右子树,如果比根节点小的放在左子树
 *在进行输出的时候按照中序遍历的原则取出:左中右的数据,输出 
 *		       	99 根节点
 *       	 80     120
 *         60  90  100 250
 * 阿里云 mldn 课时54 比较器（二叉树）
 * @author lenovo
 * 
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
 * 链表程序的本质是在于节点的互相引用,但是之前的链表有一个缺点:所有的保存数据顺序就是你的添加顺序,而且无法排序
 * 如果想进行排序的保存,就可以通过树的结构来完成。树的机构:取第一个数据作为树的跟节点,而后比这个根节点大的数据放在节点的右子树
 * 如果比跟节点小的放在左子树,在进行输出的时候按照中序遍历的原则取出:左-中-右
 * 
 * 红黑树(均衡二叉树)
 */
class BinaryTree {//现在实现一个二叉树
	private class Node {
		private Comparable data;
		private Node left;//保存左边节点
		private Node right;//保存右边节点
		public Node (Comparable data) {
			this.data = data;
		}
		
		public void addNode(Node newNode) {
			if (this.data.compareTo(newNode.data) > 0) {
				if (this.left == null) {
					this.left = newNode;
				} else {
					this.left.addNode(newNode);
				}
			} else {
				if (this.right == null) {
					this.right = newNode;
				} else {
					this.right.addNode(newNode);
				}
			}
		}
		public void toArrayNode() {
			if (this.left != null) { //有左边节点
				this.left.toArrayNode();
			}
			BinaryTree.this.returnData[BinaryTree.this.foot++] = this.data;
			if (this.right != null) {
				this.right.toArrayNode();
			}
		}
	}
	
	private Node root;//根节点
	private int count = 0;
	private int foot = 0;//脚标
	private Object[] returnData;//返回数据
	public Object[] toArray() {
		this.foot = 0;//先清空脚标,保证此方法可以被调用多次
		this.returnData = new Object[this.count];
		this.root.toArrayNode();
		return this.returnData;
	}
	public void add(Object data) {
		if (data == null) {
			return;
		}
		Node newNode = new Node((Comparable)data);
		if (this.root == null) {
			this.root = newNode;
		} else {
			this.root.addNode(newNode);
		}
		this.count ++;
	}
}

public class BTDemo {
	public static void main(String[] args) {
		BinaryTree bt = new BinaryTree();
		bt.add("A");
		bt.add("X");
		bt.add("B");
		System.out.println(Arrays.toString(bt.toArray()));
	}
}
