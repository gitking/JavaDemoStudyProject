package com.yale.test.thread.mldn;

public class ThreadLambda {
	public static void main(String[] args) {
		new Thread(()-> System.out.println("用Lambda表达式,实现Runnable接口")){}.start();
	}
}
