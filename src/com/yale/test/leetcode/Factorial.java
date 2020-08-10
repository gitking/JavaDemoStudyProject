package com.yale.test.leetcode;

import javafx.scene.chart.PieChart.Data;

/**
 * 阶乘
 * 一个正整数的阶乘（英语：factorial）是所有小于及等于该数的正整数的积，并且有0的阶乘为1。自然数n的阶乘写作n!。
 * 亦即n!=1×2×3×...×n。阶乘亦可以递归方式定义：0!=1，n!=(n-1)!×n。
 * https://www.runoob.com/java/java-examples.html
 * @author dell
 */
public class Factorial {
	public static void main(String[] args) {
		for (int counter = 0; counter <= 10; counter++){
	        System.out.printf("%d! = %d\n", counter, factorial(counter));
	    }
	}
	
	public static long factorial(long number) {
		if (number < 0) {//为了junit测试才加的这行代码
            throw new IllegalArgumentException();
        }
		if (number<=1) {
			return 1;
		} else {
			return number * factorial(number-1);
		}
	}
	
	/*
	 * 覃超递归代码模板
	 * 递归思维要点:
	 * 1,不要人肉进行递归(最大误区),刚开始练习递归时可以把递归的状态树在纸上画出来,后面就不要这样做了
	 * 2,找到最近最简单的方法,将其拆解成可重复解决的问题(重复子问题)
	 * 3,数学归纳法的思维
	 */
	public void recursion(int level, int param1,int param2){
		//recursion terminator 递归终结条件,写递归开始一定要记得先把递归终止条件给写上,否则会造成无限循环或者死递归
		if (level > 5) {
			return;
		}
		
		//process logic in current level 处理当前层逻辑
		//process(level, data);//业务代码
		
		//drill down 下到下一层去,level+1这几个参数用来标记当前是哪一层
		recursion(level+1, param1, param2);
		
		//reverse the current level status if needed 清理当前层,打扫战场,一般来说不需要清理
	}
}
