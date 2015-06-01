package sk.tomsik68.pw.impl.factory;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.utils.Util;

public final class ClassWeatherFactory<W extends Weather> implements WeatherFactory<W> {
	private final Class<W> clazz;

	public ClassWeatherFactory(Class<W> clazz1) {
		this.clazz = clazz1;
	}

	public W create(int region) {
		try {
			return clazz.getConstructor(new Class[] { WeatherDescription.class, Integer.class }).newInstance(new Object[] { ProperWeather.instance().getWeatherDescription(clazz.getSimpleName().replace("Weather", "")), region });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public WeatherDefaults getDefaults() {
		try {
			return Util.getWeatherDefaults(clazz);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't get WeatherDefaults from " + clazz.getName(), e);
		}
	}
}