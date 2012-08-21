package sk.tomsik68.pw.struct;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sk.tomsik68.pw.WeatherManager;
@Deprecated
public class SaveStruct implements Serializable {
	private static final long serialVersionUID = -9186915217675713495L;
	private int duration;
	private boolean canNowChange;
	private boolean canEverChange;
	private List<Integer> previousWeathers;
	private int currentWeather;
	private UUID world;

	
	public SaveStruct(UUID world1, WeatherData wd) {
		setPreviousWeathers(wd.getPreviousWeathers());
		setDuration(wd.getDuration());
		setCanNowChange(true);
		setCanEverChange(wd.canEverChange());
		setCurrentWeather(Integer.valueOf(WeatherManager.getUID(wd
				.getCurrentWeather().getName())));
		this.world = world1;
	}

	public void setDuration(int duration1) {
		this.duration = duration1;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setPreviousWeathers(List<Integer> previousWeathers1) {
		this.previousWeathers = previousWeathers1;
	}

	public List<Integer> getPreviousWeathers() {
		return this.previousWeathers;
	}

	public void setCurrentWeather(Integer currentWeather1) {
		this.currentWeather = currentWeather1.intValue();
	}

	public Integer getCurrentWeather() {
		return Integer.valueOf(this.currentWeather);
	}

	public void setCanNowChange(boolean canNowChange1) {
		this.canNowChange = canNowChange1;
	}

	public boolean isCanNowChange() {
		return this.canNowChange;
	}

	public void setCanEverChange(boolean canEverChange1) {
		this.canEverChange = canEverChange1;
	}

	public boolean isCanEverChange() {
		return this.canEverChange;
	}

	public WeatherData toWeatherData() {
		WeatherData wd = new WeatherData();
		wd.setCanEverChange(this.canEverChange);
		wd.setDuration(this.duration);
		wd.setCurrentWeather(WeatherManager.getWeather(this.currentWeather,
				this.world.variant()));
		return wd;
	}

	public UUID getWorldId() {
		return this.world;
	}
}