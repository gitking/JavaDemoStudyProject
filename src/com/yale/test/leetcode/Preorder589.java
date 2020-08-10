package com.yale.test.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Preorder589 {
	public static void main(String[] args) {

	}
	
	/*
	 * 给定一个 N 叉树，返回其节点值的前序遍历。
	 * 例如，给定一个 3叉树 :
	 * 			1
	 *  	3 	2 	4
	 *     5 6
	 * 返回其前序遍历: [1,3,5,6,2,4]。
	 * 说明: 递归法很简单，你可以使用迭代法完成此题吗?
	 * 方法一：迭代
	 * 由于递归实现 N 叉树的前序遍历较为简单，因此我们只讲解如何使用迭代的方法得到 N 叉树的前序遍历。
	 * 我们使用一个栈来帮助我们得到前序遍历，需要保证栈顶的节点就是我们当前遍历到的节点。我们首先把根节点入栈，因为根节点是前序遍历中的第一个节点。随后每次我们从栈顶取出一个节点 u，它是我们当前遍历到的节点，并把 u 的所有子节点逆序推入栈中。例如 u 的子节点从左到右为 v1, v2, v3，那么推入栈的顺序应当为 v3, v2, v1，这样就保证了下一个遍历到的节点（即 u 的第一个子节点 v1）出现在栈顶的位置。
	 * 复杂度分析
     * 时间复杂度：时间复杂度：O(M)O(M)O(M)，其中 MMM 是 N 叉树中的节点个数。每个节点只会入栈和出栈各一次。
     * 空间复杂度：O(M)O(M)O(M)。在最坏的情况下，这棵 N 叉树只有 2 层，所有第 2 层的节点都是根节点的孩子。将根节点推出栈后，需要将这些节点都放入栈，共有 M−1M - 1M−1 个节点，因此栈的大小为 O(M)O(M)O(M)。
		作者：LeetCode
		链接：https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/ncha-shu-de-qian-xu-bian-li-by-leetcode/
		来源：力扣（LeetCode）
		著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	public static List<Integer> preorder(Node root) {
		LinkedList<Node> stack = new LinkedList<>();
        LinkedList<Integer> output = new LinkedList<>();
        if (root == null) {
            return output;
        }

        stack.add(root);
        while (!stack.isEmpty()) {
            Node node = stack.pollLast();
            output.add(node.val);
            Collections.reverse(node.children);
            for (Node item : node.children) {
                stack.add(item);
            }
        }
        return output;
    }
	
	/*
	 * N叉树的前序遍历与二叉树本质一样。
	 * 二叉树：根->左->右
	 * N叉树：根->从左到右的各子结点
	 */
	private List<Integer> res;
    public List<Integer> preorder1(Node root) {
        res = new LinkedList<>();
        dfs(root);
        return res;
    }
    private void dfs(Node root) {
        if(root == null)    return;
        res.add(root.val);
        for(Node child : root.children){
            dfs(child);
        }
    }
    
    /*
     * 迭代
		将上述递归的思想用迭代的方式表达出来，关键在于如何每次处理完一个节点后，马上处理它的左边第一个子结点。
		不妨考虑一下层序遍历。
		层序遍历借助队列，初始时将根结点入列，此后只要队列不空，就出列一个结点，并将它的子结点从左到右依次入列。
		这种方式每次从队首出列的结点可能是某个结点的左子节点，也可能是某个结点的右子节点。
		如何由此向前序遍历的方向靠呢？
		再看一下队尾，如果结点每次都从队尾出列，然后再将子结点从左到右入列，很容易发现，这是一种根->右->左的前序遍历。
		那么此时已经茅塞顿开，如果把入列方式改为将子结点从右到左入列，再每次都从队尾出列，这就是根->左->右的前序遍历。
		此外，若每次从队尾出列，属于后进先出，相当于栈，则可用栈代替队列。
		作者：tyanyonecancode
		链接：https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/marveljian-dan-de-xue-xi-bi-ji-589-by-tyanyonecanc/
		来源：力扣（LeetCode）
		著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> preorder3(Node root) {
        List<Integer> res = new LinkedList<>();
        if(root == null)    return res;
        Stack<Node>  stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()) 
        {
            Node cur = stack.pop();
            res.add(cur.val);
            for(int i = cur.children.size()-1; i >= 0; i--)
                stack.push(cur.children.get(i));
        }
        return res;
    }
    
    /*
     * 下面是JavaScript的写法
     * 这个人很厉害写了很多关于二叉树的解法,一定要看完
     * 先序遍历(PreOrder, 按照先访问根节点的顺序)
     * 作者：lrjets
    	链接：https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/yi-tao-quan-fa-shua-diao-nge-bian-li-shu-de-wen--3/
    	来源：力扣（LeetCode）
    	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
//    var preorderTraversal = function(root) {
//    	  const res = []
//    	  function traversal (root) {
//    	    if (root !== null) {
//    	      res.push(root.val) // 访问根节点的值
//    	      traversal(root.left) // 递归遍历左子树
//    	      traversal(root.right) // 递归遍历右子树
//    	    }
//    	  }
//    	  traversal(root)
//    	  return res
//    	}

    	/*
    	 * 94 中序遍历(InOrder, 按照根节点在中间访问的顺序)
    	 * 作者：lrjets
    	链接：https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/yi-tao-quan-fa-shua-diao-nge-bian-li-shu-de-wen--3/
    	来源：力扣（LeetCode）
    	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    	 */
//    var inorderTraversal = function(root) {
//    	  const res = []
//    	  function traversal (root) {
//    	    if (root !== null) {
//    	      traversal(root.left)
//    	      res.push(root.val)
//    	      traversal(root.right)
//    	    }
//    	  }
//    	  traversal(root)
//    	  return res
//    	}
    
    /*
     * 145 后续遍历(PosterOrder, 按照根节点在后面访问的顺序)
     * 作者：lrjets
    	链接：https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/solution/yi-tao-quan-fa-shua-diao-nge-bian-li-shu-de-wen--3/
    	来源：力扣（LeetCode）
    	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
//    var postorderTraversal = function(root) {
//    	  const res = []
//    	  function traversal (root) {
//    	    if (root !== null) {
//    	      traversal(root.left)
//    	      traversal(root.right)
//    	      res.push(root.val)
//    	    }
//    	  }
//    	  traversal(root)
//    	  return res
//    	}

   
    /*
     * 外国网友的迭代解
     * https://leetcode.com/problems/n-ary-tree-preorder-traversal/discuss/147955/Java-Iterative-and-Recursive-Solutions
     */
    public List<Integer> preorderEn(Node root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;
        
        Stack<Node> stack = new Stack<>();
        stack.add(root);
        
        while (!stack.empty()) {
            root = stack.pop();
            list.add(root.val);
            for (int i = root.children.size() - 1; i >= 0; i--)
                stack.add(root.children.get(i));
        }
        
        return list;
    }

    /*
     * 外国网友的Recursive Solution
     * https://leetcode.com/problems/n-ary-tree-preorder-traversal/discuss/147955/Java-Iterative-and-Recursive-Solutions
     */
    public List<Integer> list = new ArrayList<>();
    public List<Integer> preorderEg(Node root) {
        if (root == null)
            return list;
        
        list.add(root.val);
        for(Node node: root.children)
            preorder(node);
                
        return list;
    }
    	
    	
}

class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
}
