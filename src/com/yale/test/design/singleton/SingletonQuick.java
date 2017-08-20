package com.yale.design.singleton;

public class SingletonQuick {
	/**
	 * 这中方式比较浪费资源,JVM启动的时候就会立即实例化这个类
	 * 而不是等到真正用的时候,在实例化这个类
	 */
	public static SingletonQuick singletonQuick = new SingletonQuick();
	
	private SingletonQuick(){
	}
	
	public static SingletonQuick getInstence(){
		return singletonQuick;
	}
}
