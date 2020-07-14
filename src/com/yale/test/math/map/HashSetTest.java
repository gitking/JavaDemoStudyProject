package com.yale.test.math.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class HashSetTest {

	public static void main(String[] args) {
		HashSet<String> hs = new HashSet<String>();
		System.out.println("HashSet跟HashMap有什么关系呢?HashSet内部就是一个HashMap,可以看下HashSet的构造方法源码");
		Collections.synchronizedMap(new HashMap<String,String>());
		//张孝祥的视频里面有一个网页上面有mpstat撒谎了,我才开始搜mpstat撒谎了
		//https://www.baidu.com/s?wd=mpstat%E6%92%92%E8%B0%8E%E4%BA%86&rsv_spt=1&rsv_iqid=0xe77c0f8b00009bb2&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=0&rsv_dl=tb&oq=%25E4%25BD%25A0%25E7%259A%2584%25E7%25B3%25BB%25E7%25BB%259F%25E8%2590%25A5%25E5%2585%25BB%25E8%25BF%2587%25E5%2589%25A9%25E5%2590%2597&rsv_t=9db6093QijATZsAaF4M%2FyKXnsl%2BLb8Tj30NR2sFqQL6Q4WqKnXuBSt6hoNK0i7H8cCx8&inputT=8222&rsv_pq=bb05c5db0000d78e&rsv_sug3=61&rsv_sug1=21&rsv_sug7=100&rsv_sug2=0&rsv_sug4=9010
		System.out.println("synchronizedMap是Collections里面的一个静态内部类, synchronizedMap内部就是一个HashMap,只不过在每个方法前面加了synchronized");
	}
}
