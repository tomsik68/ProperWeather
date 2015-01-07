package sk.tomsik68.pw.struct;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherDatav5 implements IWeatherData {
	private Weather currentWeather;
	private int duration;
	private int region;
	private WeatherCycle cycle;

	@Override
	public Weather getCurrentWeather() {
		return currentWeather;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public int decrementDuration() {
		return duration -= ProperWeather.TASK_PERIOD;
	}

	@Override
	public int getRegion() {
		return region;
	}

	@Override
	public WeatherCycle getCycle() {
		return cycle;
	}

	@Override
	public void setRegion(int regionID) {
		this.region = regionID;
	}

	@Override
	public void setCycle(WeatherCycle c) {
		this.cycle = c;
	}

	@Override
	public void setCurrentWeather(Weather w) {
		this.currentWeather = w;
	}

	@Override
	public void setDuration(int d) {
		duration = d;
	}
}
