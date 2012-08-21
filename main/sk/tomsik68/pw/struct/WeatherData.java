package sk.tomsik68.pw.struct;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;
import sk.tomsik68.pw.WeatherManager;
import sk.tomsik68.pw.api.Weather;

public class WeatherData implements Externalizable {
	private LinkedList<Integer> list;
	private Weather currentWeather;
	private int duration;
	private int region;
	private boolean canEverChange;

	public WeatherData() {
		list = new LinkedList<Integer>();
	}

	public boolean wasWeather(Weather weather) {
		return list.contains(Integer.valueOf(WeatherManager.getUID(weather
				.getClass().getSimpleName())));
	}

	public void setCurrentWeather(Weather currentWeather1) {
		if (list == null)
			list = new LinkedList<Integer>();
		list.add(Integer.valueOf(WeatherManager.getUID(currentWeather1.getName())));
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
		return duration--;
	}

	public Integer getPreviousWeather() {
		return (Integer) list.get(list.size() - 1);
	}

	public List<Integer> getPreviousWeathers() {
		return list;
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		canEverChange = in.readBoolean();
		duration = in.read();
		region = in.read();
		currentWeather = WeatherManager.getWeather(in.read(), region);
		if(currentWeather == null)
		    System.out.println("Invalid data for region #"+region+". Weather = null !");
		list = ((LinkedList<Integer>) in.readObject());
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeBoolean(canEverChange);
		out.write(duration);
		out.write(region);
		out.write(WeatherManager.getUID(currentWeather.getName()));
		out.writeObject(list);
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region1) {
		this.region = region1;
	}
}