package sk.tomsik68.pw.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Random;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;

public class RandomWeatherCycle extends WeatherCycle {
	private static final Random rand = new Random();

	public RandomWeatherCycle(WeatherSystem ws, String name) {
		super(ws, name);
	}

	@Override
	public IWeatherData nextWeatherData(IWeatherData wd) {
		// just choose random weather and start it with no rules
		if (wd.decrementDuration() <= 0) {
			ArrayList<String> weathers = new ArrayList<String>(ProperWeather.instance().getWeathers().getRegistered());

			Weather weather = ProperWeather.instance().getWeathers().createWeather(weathers.get(rand.nextInt(weathers.size())), wd.getRegion());

			// weatherSystem.setRegionalWeather(weather, wd.getRegion());

			wd.setCurrentWeather(weather);
			weather.initWeather();

			wd.setDuration(rand.nextInt(36000));

		}
		return wd;
	}

	@Override
	public void loadState(ObjectInput in) throws IOException, ClassNotFoundException {
	}

	@Override
	public void saveState(ObjectOutput out) throws IOException {
	}

}
