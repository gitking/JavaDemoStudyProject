package com.yale.test.lambda;

/*
 * 断言
 * 以前的Java程序设计师得在程序中加入一大堆System.out.println()代码来显示出错的信息,列出变量值,还有执行到那里的信息以观察流程控制的走向.
 * 等到程序可以正确执行的时候,还要看过所有的程序把println()删掉,这很麻烦且容易出错.
 * 从java1.4之后出错就变得容易多了,为什么?
 * assert断言
 * Java5.0的编译器会假设所编译的源文件与5.0兼容,此时预设断言是打开的.执行时,如果没有特别设定的话,被加入到程序中的assert命令会被java虚拟机忽略,
 * 但若你指定Java虚拟机要打开断言的话,它就能过在不变动任何一行程序的情况下帮助你对程序除错.
 * 有些人会抱怨最终版本的程序上还有assert命令,然而留着assert对于已经部署安装的程序代码是很有价值的,如果用户遇到问题,你就可以指示客户打开断言来执行程序。
 * 并取得输出结果,如果没有留下assert,你就很难知道发生了什么事,这样做没什么坏处,未打开时,java虚拟机会忽略掉他们,所以不会影响性能.
 */
public class AssertDemo {
	public static void main(String[] args) {
		/*
		 * 如何使用断言,在你认为一定是true的地方加上assert命令,例如:assert (height >0);
		 * 如果为true,则继续执行,若为false,抛出AssertionError
		 * 你也可以加一点点信息:
		 * assert(height > 0) : "height= " + height + " weight = " + weight;
		 * 在冒号后面的指令可以是任何解出非null值的合法Java语句,无论如何千万别在assert中改变对象的状态,
		 * 不然的话打开assertion执行时可能会改变程序的行为 。
		 * 带有断言的编译和执行
		 * 编译: javac AssertDemo.java(注意编译并不需要特殊选项)
		 * 执行: java -ea AssertDemo
		 */
		int height = 10;
		assert(height > 0);//注意java默认断言功能是关闭的
	}
}
