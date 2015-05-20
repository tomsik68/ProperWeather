package sk.tomsik68.pw.struct;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.plugin.ProperWeather;

@Deprecated
public class WeatherDatav4 implements Externalizable, IWeatherData {
	private static final long serialVersionUID = -7099393484035l;

	private Weather currentWeather;
	private int duration;
	private int region;
	private WeatherCycle cycle;

	public String weather;

	public String cycleName;

	public WeatherDatav4() {
	}

	public void setCurrentWeather(Weather currentWeather1) {
		this.currentWeather = currentWeather1;
	}

	public Weather getCurrentWeather() {
		return currentWeather;
	}

	public void setDuration(int duration1) {
		this.duration = duration1;
	}

	public int getDuration() {
		return duration;
	}

	public int decrementDuration() {
		return duration -= ProperWeather.TASK_PERIOD;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.duration = in.readInt();
		this.region = in.readInt();
		this.weather = in.readUTF();
		// this.currentWeather =
		// ProperWeather.instance().getWeathers().createWeather(weather,
		// region);
		/*
		 * if (currentWeather == null)
		 * ProperWeather.log.severe("Invalid data for region #" + region +
		 * ". Weather = null !");
		 */
		this.cycleName = in.readUTF();

		/*
		 * cycle =
		 * ProperWeather.instance().getCycles().get(cycleName).create(ProperWeather
		 * .instance().getWeatherSystem()); if (cycle == null)
		 * ProperWeather.log.severe("Invalid data for region #" + region +
		 * ". cycle = null !"); cycle.loadState(in);
		 */
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(duration);
		out.writeInt(region);
		out.writeUTF(currentWeather.getName());
		out.writeUTF(cycle.getName());
		cycle.saveState(out);
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region1) {
		this.region = region1;
	}

	public WeatherCycle getCycle() {
		return cycle;
	}

	public void setCycle(WeatherCycle newCycle) {
		cycle = newCycle;
	}

	@Override
	public String toString() {
		return "WeatherData[region=" + region + " weather=" + currentWeather.getName() + " duration=" + duration + "]";
	}

}
