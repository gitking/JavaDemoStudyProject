package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class HashSetTest {

	public static void main(String[] args) {
		HashSet<String> hs = new HashSet<String>();
		System.out.println("HashSet跟HashMap有什么关系呢?HashSet内部就是一个HashMap,可以看下HashSet的构造方法源码");
		Collections.synchronizedMap(new HashMap<String,String>());
		System.out.println("synchronizedMap是Collections里面的一个静态内部类, synchronizedMap内部就是一个HashMap,只不过在每个方法前面加了synchronized");
	}
}
