package com.yale.test.design.observer.interfacevo.impl;

import com.yale.test.design.observer.interfacevo.DisplayElement;
import com.yale.test.design.observer.interfacevo.Observer;
import com.yale.test.design.observer.interfacevo.Subject;

public class CurrentConditionsDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;
	private Subject weatherDataSubject;
	
	public CurrentConditionsDisplay(Subject weatherData){
		this.weatherDataSubject = weatherData;
		weatherDataSubject.registerObserver(this);
	}

	@Override
	public void update(float temp, float humidity, float pressure) {
		this.humidity = humidity;
		this.temperature = temp;
		diaplay();
	}

	/**
	 * 具体的布告板显示数据
	 */
	public void diaplay() {
		System.out.println("Current conditions:" + temperature + ",F degrees and " + humidity + "% humidity");
	}

}
