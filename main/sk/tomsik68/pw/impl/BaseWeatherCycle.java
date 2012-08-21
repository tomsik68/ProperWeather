package sk.tomsik68.pw.impl;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.region.Region;

public abstract class BaseWeatherCycle implements WeatherCycle {
	protected WeatherSystem weatherSystem;

	public BaseWeatherCycle(WeatherSystem ws) {
		this.weatherSystem = ws;
	}

	public WeatherSystem getWeatherSystem() {
		return this.weatherSystem;
	}

	public abstract Weather nextWeather(Region region);
}