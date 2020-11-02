package com.yale.test.design.interpreter.template.method;

/*
 * 整个流程没有问题，但是，lookupCache(key)和putIntoCache(key, value)这两个方法还根本没实现，怎么编译通过？这个不要紧，我们声明抽象方法就可以：
 */
public abstract class AbstractSetting {
	public final String getSeting(String key) {
		String value = lookupCache(key);//先从缓存读取
		if (value == null) {//缓存中找不到,从数据库读取
			value = readFromDatabase(key);
			System.out.println("[DEBUG] load from db: " + key + " = " + value);
			putIntoCache(key, value);//放入缓存
		} else {
			System.out.println("[DEBUG] load from cache: " + key + " = " + value);
		}
		return value;
	}
	
	protected abstract String lookupCache(String key);
	protected abstract void putIntoCache(String key, String value);
	
	private String readFromDatabase(String key) {
		return Integer.toHexString(0x7fffffff  & key.hashCode());
	}
}
