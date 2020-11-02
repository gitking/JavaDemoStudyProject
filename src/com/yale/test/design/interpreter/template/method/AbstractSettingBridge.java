package com.yale.test.design.interpreter.template.method;

/*
 * 思考如果既可以扩展缓存，又可以扩展底层存储，会不会出现子类数量爆炸的情况？如何解决？
 * 当然是使用桥接模式将数据读取来源从AbstractSetting里分离出来啦
 */
public abstract class AbstractSettingBridge {
	private Source source;
	
	public AbstractSettingBridge(Source source) {
		this.source = source;
	}
	
	public final String getSetting(String key) {
		String value = lookupCache(key);
		if (value == null) {
			value = source.read(key);
			System.out.println("[DEBUG] load from db : " + key + " = " + value);
			putIntoCache(key, value);
		} else {
			System.out.println("[DEBUG] load from cache: " + key + " = " + value);
		}
		return value;
	}
	
	protected abstract String lookupCache(String key);
	
	protected abstract void putIntoCache(String key, String value);
}
