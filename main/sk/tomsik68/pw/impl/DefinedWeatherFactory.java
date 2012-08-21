package sk.tomsik68.pw.impl;

import java.lang.reflect.Field;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.weather.WeatherDefined;

public class DefinedWeatherFactory implements WeatherFactory<WeatherDefined> {
	private final String name;

	public DefinedWeatherFactory(String weatherName) {
		this.name = weatherName;
	}

	public WeatherDefined create(Object[] args) {
		WeatherDefined result = new WeatherDefined(ProperWeather.instance()
				.getWeatherDescription(this.name), (Integer) args[0],
				ProperWeather.instance().getWeatherDefinition(this.name));
		return result;
	}

	public WeatherDefaults getDefaults() {
		Field[] fields = WeatherDefined.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Defaults.class)) {
				field.setAccessible(true);
				try {
					return (WeatherDefaults) field.get(WeatherDefined.class
							.newInstance());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}