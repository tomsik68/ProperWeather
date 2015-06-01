package sk.tomsik68.pw.impl;

import java.util.Map;

public final class WeatherSpec {
	private final Map<String, Object> map;

	public WeatherSpec(Map<String, Object> map) {
		this.map = map;
	}

	public int getProbability() {
		return (Integer) getProperty("probability");
	}

	public int getMinDuration() {
		return (Integer) getProperty("min-duration");
	}

	public String getWeatherName() {
		return getProperty("weather").toString();
	}

	public int getMaxDuration() {
		return (Integer) getProperty("max-duration");
	}

	public Object getProperty(String key) {
		return map.get(key);
	}
}
