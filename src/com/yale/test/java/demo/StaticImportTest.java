package com.yale.test.java.demo;

//静态导入,MathTest.*的意思是将MathTest类中的静态方法全部导入
//那么在本来中,调用MathTest的静态方法,就不需要加上类名了
//Java编译器最终编译出的.class文件只使用完整类名，因此，在代码中，当编译器遇到一个class名称时：
import static com.yale.test.math.MathTest.*;

public class StaticImportTest {

	public static void main(String[] args) {
		System.out.println("myRound是MathTest类中的方法,我现在静态导入了,调用时不需要加上类名了:" + myRound(10, 2));
	}
}
