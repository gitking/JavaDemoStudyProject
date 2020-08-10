package com.yale.test.leetcode;

import java.util.List;

/*
 * 22. 括号生成 https://leetcode-cn.com/problems/generate-parentheses/
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 * 示例：
 * 输入：n = 3
	输出：[
	       "((()))",
	       "(()())",
	       "(())()",
	       "()(())",
	       "()()()"
	     ]
	来源：力扣（LeetCode）
	链接：https://leetcode-cn.com/problems/generate-parentheses
	著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
	国际光头哥,用yield实现leetcode的括号生成
	光头哥是欧洲的一位选手叫Pochmann,他经常会玩一些代码艺术,
	所以要经常看他的代码,对你阅读代码的能力肯定有提高。
	https://leetcode.com/problems/generate-parentheses/discuss/?currentPage=1&orderBy=most_votes&query=
 */
public class GenerateParenthesis22 {
	public static void main(String[] args) {
		/*
		 * 括号生成分析
		 * 1,首先要记录你当前打印的是左括号还是右括号
		 * 2,要记录左括号打印了几个,然后打印对应数量的右括号
		 */
		GenerateParenthesis22 gp = new GenerateParenthesis22();
		System.out.println("测试分析代码" + gp.generateParenthesis(3));//打印3对括号
		System.out.println("****************正式成型代码*****************");
		gp._generate2(0, 0, 3, "");
	}
	
	public List<String> generateParenthesis(int n) {
		_generate(0, 2*n, "");
		return null;
	}
	
	/**
	 * 这个方法是刚开始写的时候分析用的
	 * @param level 递归当前层
	 * @param max 因为是生成括号,一个括号有俩个边界(),所以max是2n
	 * @param s 括号边界
	 */
	public void _generate(int level, int max, String s){
		//terminator  递归终结条件
		if (level >= max) {
			/*
			 * filter the invalid s 验证括号的合法性
			 * left 左括号随时可以加,只要别超标,不能超过max/2个左括号
			 * right 右括号一开始就添加右括号肯定是不行的,添加右括号前必须有对应的左括号
			 */
			System.out.println(s);
			return;
		}
		
		//process current logic:当前层处理逻辑,打印左括号还是右括号
		String s1 = s + "(";
		String s2 = s + ")";
		//drill down,进入到下一层,level要加1,
		_generate(level+1, max, s1);
		_generate(level+1, max, s2);
		
		//reverse states清理当前层,这个方法不需要清理
	}
	
	
	/**
	 * 这个方法是刚开始写的时候分析用的
	 * @param level 递归当前层
	 * @param max 因为是生成括号,一个括号有俩个边界(),所以max是2n
	 * @param s 括号边界
	 */
	public void _generate2(int left, int right, int n, String s){
		//terminator  递归终结条件
		if (left == n && right == n) {
			/*
			 * filter the invalid s 验证括号的合法性
			 * left 左括号随时可以加,只要别超标,不能超过n个左括号
			 * right 右括号一开始就添加右括号肯定是不行的,添加右括号前必须有对应的左括号
			 */
			System.out.println(s);
			return;
		}
		
		//process current logic:当前层处理逻辑,打印左括号还是右括号
		String s1 = s + "(";
		String s2 = s + ")";
		//drill down,进入到下一层,level要加1,
		if (left < n) {//左括号加1,因为我们用了一个左括号,所以要加1,right右括号没用不用加1
			_generate2(left+1, right, n, s1);
		}
		/*
		 * 右括号加1,因为我们用了一个右括号,所以要加1,left左括号没用不用加1
		 * 这里其实不用写&& right < n这个代码,
		 * 因为上面有判断left < n left才加1,所以left肯定不会大于n
		 * left不会大于n下面的left > right就能保证 right 不会大于n,如果right大于n,left > right这个条件就不成立了
		 */
		if (left > right && right < n) {
			_generate2(left, right +1, n, s2);
		}
		
		//reverse states清理当前层,这个方法不需要清理
	}
}
