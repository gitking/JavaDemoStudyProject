package com.yale.test.java.fanshe.perfma;

/*
 * https://mp.weixin.qq.com/s/gJhSzeDxZ9853KZMSEFJKw
 * 下面我们就那个经典的例子来分析volatile变量的读写建立的happens-before关系。
 */
public class VolatileTest {
	int i = 0;
	volatile boolean flag = false;
	
	public void write() {//Thread A
		i = 2;//1
		flag = true;//2
	}
	
	public void read() {//Thread B
		if (flag) {//3
			System.out.println("---i= " + i);//4
		}
	}
}
