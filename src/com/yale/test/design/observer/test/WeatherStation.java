package com.yale.test.design.observer.test;

import com.yale.test.design.observer.interfacevo.impl.CurrentConditionsDisplay;
import com.yale.test.design.observer.interfacevo.impl.WeatherData;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData();
		CurrentConditionsDisplay  currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
		weatherData.setMeasurements(10.1f, 20.1f, 30.1f);
	}

}
