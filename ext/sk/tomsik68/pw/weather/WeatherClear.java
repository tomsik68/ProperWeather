package sk.tomsik68.pw.weather;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;

public class WeatherClear extends Weather {

	@Defaults
	public static final WeatherDefaults def = new BasicWeatherDefaults(36000,
			50, 0, new String[] { "" });

	public WeatherClear(WeatherDescription wd1, Integer uid) {
		super(wd1, uid);
	}

	public void onRandomTime() {
	}

	public void initWeather() {
		getController().clear();
	}
}