package com.yale.test.thread.chapter.three;

public class TraditioanlThreadSynchrized {

	public static void main(String[] args) {
		new TraditioanlThreadSynchrized().init();
	}
	
	public void init () {
		final Outputer outputer = new Outputer();
		new Thread(){
			public void run () {
				while (true) {
					outputer.outputer("zhangxiaoxiang");
				}
			}
		}.start();
		new Thread(){
			public void run () {
				while (true) {
					outputer.outputer("lihuoming");
				}
			}
		}.start();
	}
	class Outputer {
		public void outputer (String name) {
			int len = name.length();
			synchronized (Outputer.class) {
				for (int i =0; i <len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
	}
}
