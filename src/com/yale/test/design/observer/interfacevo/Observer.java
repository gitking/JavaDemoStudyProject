package com.yale.test.design.observer.interfacevo;

/**
 * 观察者
 * @author lenovo
 */
public interface Observer {
	/**
	 * 所有的具体观察者都需要实现此方法
	 * 当主题发生变化时,主题会调用此方法将变化值通知各个观察者
	 * @param temp
	 * @param humidity
	 * @param pressure
	 */
	public void update(float temp,float humidity,float pressure);
}
