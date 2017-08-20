package com.yale.design.singleton;

public class Singleton {
	
	public volatile static Singleton singleton = null;
	
	private Singleton(){
	}
	
	public static Singleton getInstence(){
		if(singleton == null){
			singleton = new Singleton();
		}
		return singleton;
	}
	
	/**
	 * 这种方式效率比较低
	 * 通过增加synchronized关键字到getInstence()方法中,我们迫使每个线程在进入这个方法之前,要先等到别的线程离开该方法。
	 * 也就是说,不会有来个线程可以同时进入这个方法。
	 * @return
	 */
	public static synchronized Singleton getInstenceSec(){
		if(singleton == null){
			singleton = new Singleton();
		}
		return singleton;
	}
	
	/**
	 * 双重检查锁
	 * @return
	 */
	public static Singleton getInstenceThrid(){
		if(singleton == null){
			synchronized (Singleton.class) {
				if(singleton == null){
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
}
