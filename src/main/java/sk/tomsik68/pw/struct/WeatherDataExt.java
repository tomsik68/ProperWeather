package sk.tomsik68.pw.struct;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.plugin.ProperWeather;

/**
 * This is the new data structure, saves strings as weather IDs
 * 
 */
@Deprecated
public class WeatherDataExt implements Externalizable, IWeatherData {
	private static final long serialVersionUID = -7099393484035l;

	private LinkedList<String> list;
	private Weather currentWeather;
	private int duration;
	private int region;
	private boolean canEverChange;

	public String sCurrentWeather;

	public WeatherDataExt() {
		list = new LinkedList<String>();
	}

	public boolean wasWeather(Weather weather) {
		return list.contains(weather.getName());
	}

	public void setCurrentWeather(Weather currentWeather1) {
		if (list == null)
			list = new LinkedList<String>();
		list.add(currentWeather1.getName());
		if (list.size() > 5)
			list.removeFirst();
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

	public void setCanEverChange(boolean canEverChange1) {
		this.canEverChange = canEverChange1;
	}

	public boolean canEverChange() {
		return canEverChange;
	}

	public int decrementDuration() {
		return duration -= ProperWeather.TASK_PERIOD;
	}

	public String getPreviousWeather() {
		if (list.size() > 0)
			return list.get(list.size() - 1);
		return "clear";
	}

	public LinkedList<String> getPreviousWeathers() {
		return list;
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.canEverChange = in.readBoolean();
		this.duration = in.readInt();
		this.region = in.readInt();
		// String weather = in.readUTF();
		this.sCurrentWeather = in.readUTF();
		/*
		 * this.currentWeather =
		 * ProperWeather.instance().getWeathers().createWeather(weather,
		 * region); if (currentWeather == null)
		 * System.out.println("Invalid data for region #" + region +
		 * ". Weather = null !");
		 */
		this.list = ((LinkedList<String>) in.readObject());
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeBoolean(canEverChange);
		out.writeInt(duration);
		out.writeInt(region);
		out.writeUTF(currentWeather.getName());
		out.writeObject(list);
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region1) {
		this.region = region1;
	}

	@Override
	public String toString() {
		return "WeatherData[region=" + region + " weather=" + currentWeather.getName() + " canEverChange=" + canEverChange + " duration=" + duration + "]";
	}

	@Override
	public void setCycle(WeatherCycle randomWeatherCycle) {
		throw new IllegalStateException("This can't happen!!!");
	}

	@Override
	public WeatherCycle getCycle() {
		return null;
	}
}