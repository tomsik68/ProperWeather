package sk.tomsik68.pw.api;

import java.util.Collection;
import java.util.List;
import org.bukkit.World;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.struct.WeatherData;

public abstract interface WeatherSystem {
	public abstract void runWeather(String paramString);

	public abstract Collection<String> getWeatherList();

	public abstract void stopAtWeather(String paramString1, String paramString2);

	public abstract void deInit();

	public abstract void init();

	public abstract boolean isHooked(World paramWorld);

	public abstract void update(World paramWorld);

	public abstract void unHook(String paramString);

	public abstract void hook(World paramWorld);

	public abstract void hook(String paramString);

	public abstract WeatherController getWeatherController(Region paramRegion);

	public abstract boolean canNowBeLightning(Region paramRegion);

	public abstract void changeControllers(boolean paramBoolean);

	public abstract WeatherData getCurrentSituation(int paramInt);

	public abstract void setWeatherController(Region paramRegion,
			WeatherController paramWeatherController);

	public abstract void setWeatherController(int paramInt,
			WeatherController paramWeatherController);

	public abstract WeatherController getWeatherController(int paramInt);

	public abstract RegionManager getRegionManager();

	public abstract void setRegionManager(RegionManager paramRegionManager);

	public abstract List<String> getWorldList();
}