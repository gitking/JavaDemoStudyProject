package com.yale.test.design.observer.javautils.test;

import com.yale.test.design.observer.javautils.CurrentConditionsDisplay;
import com.yale.test.design.observer.javautils.WeatherData;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		CurrentConditionsDisplay  currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
		weatherData.setMeasurements(10.1f, 20.1f, 30.1f);
	}

}
