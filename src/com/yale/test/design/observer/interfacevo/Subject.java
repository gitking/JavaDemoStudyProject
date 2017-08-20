package com.yale.test.design.observer.interfacevo;

/**
 * 主题类
 * 主题类被所有观察者观察,当主题发生变化时,需要通知所有的观察者
 * 观察者模式可以理解为一触即发
 * @author lenovo
 */
public interface Subject {
	/**
	 * 观察者注册
	 * @param observer
	 */
	public void registerObserver(Observer observer);
	/**
	 * 删除观察者
	 * @param observer
	 */
	public void removeObserver(Observer observer);
	/**
	 * 当主题状态发生变化时,通知所有的观察者
	 */
	public void notifyObservers();
}
