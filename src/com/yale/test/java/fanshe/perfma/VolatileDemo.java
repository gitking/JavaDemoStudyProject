package com.yale.test.java.fanshe.perfma;

/**
 * https://club.perfma.com/question/267086
 * 按道理，子线程会通过num++操作破坏while循环的条件，从而终止循环，执行最后的输出操作。
 * 但在我的多次运行中，偶尔会出现while循环一直不结束的场合。像我截图一样，程序一直不终止。
 * JDK7、JDK8均已试验，均能偶然触发。
 * 答案由亮哥提供：https://www.zhihu.com/question/263528143/answer/270308453
 * 简单说来，是因为JIT激进编译导致的问题。
 * volatile只是恰巧阻止了JIT的激进编译，所以这里主要的问题不是可见性。因为哪怕变量不是volatile修饰，只要加上-Xint、-XX:-UseOnStackReplacement参数，问题一样不会出现。
 * 和jit也是有一定关系的，-Xint设定解释执行，也可以只关闭OSR看看，-XX:-UseOnStackReplacement
 * 大空翼 加-Xint、-XX:-UseOnStackReplacement都能解决问题。
 * @author dell
 */
public class VolatileDemo {
	static int num = 0;
	public static void main(String[] args) {
		new Thread(()->{
			System.out.println("Child:" + num);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			num ++;
			System.out.println("Child End:" + num);
		}).start();
		
		while (num == 0) {
			System.out.println("Main:" + num);
		}
	}
}
