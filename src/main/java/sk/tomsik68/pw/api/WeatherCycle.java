package sk.tomsik68.pw.api;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class WeatherCycle {
	protected final WeatherSystem weatherSystem;
	private final String name;

	public WeatherCycle(WeatherSystem ws, String name) {
		this.weatherSystem = ws;
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public abstract IWeatherData nextWeatherData(IWeatherData current);

	public abstract void loadState(ObjectInput in) throws IOException, ClassNotFoundException;

	public abstract void saveState(ObjectOutput out) throws IOException;
}