package com.yale.test.java.fanshe.perfma;

/*
 * https://mp.weixin.qq.com/s/gJhSzeDxZ9853KZMSEFJKw
 */
public class VolatileBarrierExample {
	int a = 0;
	volatile int v1 = 1;
	volatile int v2 = 1;
	
	void readAndWrite() {
		int i = v1;//volatile 读
		int j = v2;//volatile 读
		a = i + j;//普通读
		v1 = i + 1;//volatile 写
		v2 = j * 2;//volatile 写
	}
}
