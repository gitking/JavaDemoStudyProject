package com.yale.test.design.observer.javautils;

import java.util.Observable;
import java.util.Observer;

import com.yale.test.design.observer.interfacevo.DisplayElement;

/**
 * java提供的观察者模式API,Observer
 * @author lenovo
 *
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
	Observable observable;
	private float tempreature;
	private float humidity;
	
	public CurrentConditionsDisplay(Observable observable){
		this.observable = observable;
		observable.addObserver(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof WeatherData){
			WeatherData weatherData = (WeatherData)observable;
			this.humidity = weatherData.getHumidity();
			this.tempreature = weatherData.getHumidity();
			diaplay();
		}
	}

	@Override
	public void diaplay() {
		System.out.println("Current conditions:" + tempreature + ",F degrees and " + humidity + "% humidity");
	}
}
