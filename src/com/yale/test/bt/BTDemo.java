package com.yale.test.bt;

import java.util.Arrays;
/**
 * 二叉树原理
 *树的机构:取第一个数据作为树的根节点,然后将比这个根节点大的数据放在节点的右子树,如果比根节点小的放在左子树
 *在进行输出的时候按照中序遍历的原则取出:左中右的数据,输出 
 *		       	99 根节点
 *       	 20     90
 *         6  11  88 89
 * @author lenovo
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
