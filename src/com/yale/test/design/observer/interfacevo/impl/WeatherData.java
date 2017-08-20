package com.yale.test.design.observer.interfacevo.impl;

import java.util.ArrayList;

import com.yale.test.design.observer.interfacevo.Observer;
import com.yale.test.design.observer.interfacevo.Subject;

public class WeatherData implements Subject {
	private ArrayList observers;
	private float temprature;
	private float humidity;
	private float pressure;
	
	public WeatherData(){
		observers = new ArrayList();
	}
	
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		if(observer != null){
			int i = observers.indexOf(observer);
			if(i >=0 ){
				observers.remove(i);
			}
		}
	}
	public void notifyObservers() {
		for(int i = 0; i < observers.size(); i++){
			Observer observer = (Observer)observers.get(i);
			observer.update(temprature, humidity, pressure);
		}
	}
	
	/**
	 * 一单数据发生变化,此方法会被调用
	 * 数据发生变化时,调用measurementsChanged通知每一个观察者
	 */
	public void measurementsChanged(){
		notifyObservers();
	}

	/**
	 * 自己模拟数据发生变化
	 * @param temp
	 * @param humidity
	 * @param pressure
	 */
	public void setMeasurements(float temp,float humidity,float pressure){
		this.humidity = humidity;
		this.temprature = temp;
		this.pressure = pressure;
		measurementsChanged();
		
	}
}
