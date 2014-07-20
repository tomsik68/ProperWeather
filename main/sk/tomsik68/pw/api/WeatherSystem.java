package sk.tomsik68.pw.api;

import java.util.List;

import org.bukkit.World;

import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.region.Region;

public abstract interface WeatherSystem {
    public abstract void startCycle(String cycle, String world, String startWeather);

    public abstract void deInit() throws Exception;

    public abstract void init() throws Exception;

    public abstract void update(World destWorld);

    public abstract WeatherController getWeatherController(Region where);

    public abstract boolean canNowBeLightning(Region where);

    public abstract void setWeatherController(Region where, WeatherController paramWeatherController);

    public abstract void setWeatherController(int regionID, WeatherController paramWeatherController);

    public abstract WeatherController getWeatherController(int regionID);

    public abstract RegionManager getRegionManager();

    public abstract void setRegionManager(RegionManager paramRegionManager);

    public abstract List<String> getWorldList();

    public abstract void setRegionData(Region region, IWeatherData wd);

    public abstract IWeatherData getRegionData(Region region);

    public abstract void startCycleInRegion(String cycleName, int region, String startWeather);

    public abstract void unHook(String worldName);

    public abstract boolean isHooked(World world);

    public void setRegionalWeather(Weather w, int region);
}