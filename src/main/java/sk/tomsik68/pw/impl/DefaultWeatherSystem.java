package sk.tomsik68.pw.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;

import org.bukkit.scheduler.BukkitScheduler;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.files.impl.weatherdata.WeatherDataFile;
import sk.tomsik68.pw.impl.registry.WeatherCycleFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

public class DefaultWeatherSystem implements WeatherSystem {
	private RegionalWeathers weatherSituations;
	private RegionManager regionManager = new SimpleRegionManager();
	private Random rand = new Random();
	private final WeatherFactoryRegistry weathers;
	private final IServerBackend backend;
	private final WeatherCycleFactoryRegistry cycles;
	private final BukkitScheduler scheduler;
	private WeatherDataFile dataFile;
	private final Object changeLock = new Object();
	private Map<Integer, WeatherController> controllers;


	public DefaultWeatherSystem(WeatherFactoryRegistry weathers, WeatherCycleFactoryRegistry cycles, BukkitScheduler scheduler, IServerBackend backend) {
		/*
		 * weatherData = new HashMap<Integer, IWeatherData>();
		 */
		controllers = new HashMap<Integer, WeatherController>();

		weatherSituations = new RegionalWeathers();
		this.weathers = weathers;
		this.backend = backend;
		this.cycles = cycles;
		this.scheduler = scheduler;
	}

	public void runWeather(String worldName) {
		startCycle("random", worldName, "");
	}

	public void stopAtWeather(String worldName, String weatherName) {
		startCycle("stop", worldName, weatherName);
	}

	@Override
	public void startCycle(String cycleName, String worldName, String startWeather) {
		Validate.notNull(cycleName, "cycleName is null!");
		Validate.notEmpty(cycleName, "cycleName is empty!");
		Validate.notNull(worldName, "worldName is null!");
		Validate.notEmpty(worldName, "worldName is empty!");

		World world = Bukkit.getWorld(worldName);
		if (!regionManager.isHooked(world))
			regionManager.hook(Bukkit.getWorld(worldName), ProperWeather.instance().getConfigFile().getRegionType(worldName));

		List<Integer> regionsInWorld = regionManager.getRegions(Bukkit.getWorld(worldName));
		for (int r : regionsInWorld) {
			startCycleInRegion(cycleName, r, startWeather);
		}

	}

	@Override
	public void startCycleInRegion(String cycleName, int r, String startWeather) {
		synchronized (changeLock) {
			// make sure there's no controller
			if (controllers.containsKey(r)) {
				controllers.get(r).finish();
				controllers.remove(r);
			}
			// create new weather data
			IWeatherData wd = weatherSituations.createDefaultWeatherData();
			wd.setRegion(r);
			// set cycle for the data
			WeatherCycle cycle = cycles.get(cycleName).create(this);
			wd.setCycle(cycle);
			// start the initial weather or set weather to clear with -1
			// duration to get replaced after 3 - 4 seconds
			if (startWeather.length() == 0) {
				wd.setDuration(-1);
				Weather weather = weathers.get("clear").create(r);
				wd.setCurrentWeather(weather);
				weather.initWeather();

			} else {
				Weather weather = weathers.get(startWeather).create(r);
				wd.setCurrentWeather(weather);
				weather.initWeather();
			}
			weatherSituations.updateSituation(wd);
		}

	}

	public synchronized void deInit() throws Exception {
		regionManager.saveRegions();
		weatherSituations.save(dataFile);
	}

	public void init() throws Exception {
		regionManager.loadRegions();
		dataFile = new WeatherDataFile(new File(ProperWeather.instance().getDataFolder(), "data.dat"));

		weatherSituations.loadFrom(this, dataFile, weathers, cycles);
		// cancel raining in all worlds that are controlled, so minecraft server
		// doesn't change it(raining is
		// handled by individual backends, which send packets directly to
		// players)
		for (String w : getWorldList()) {
			Bukkit.getWorld(w).setStorm(false);
		}

	}

	public void update(World world) {
		List<Integer> regions = regionManager.getRegions(world);
		for (Integer r : regions) {
			Region region = regionManager.getRegion(r);
			IWeatherData wd = getRegionData(region);
			if (wd != null) {
				try {
					wd = wd.getCycle().nextWeatherData(wd);
					weatherSituations.updateSituation(wd);
				} catch (Exception e) {
					ProperWeather.log.severe("WeatherCycle has malfunctioned. Class: " + wd.getCycle().getClass().getName());
					e.printStackTrace();
				}
				if (wd.getCurrentWeather() != null && (rand.nextInt(100) <= wd.getCurrentWeather().getRandomTimeProbability())) {
					final Weather weather = wd.getCurrentWeather();
					// random time events will be executed synchronously
					scheduler.runTask(ProperWeather.instance(), new Runnable() {
						@Override
						public void run() {
							weather.onRandomTime();
						}
					});
				}
			} else {
				ProperWeather.log.severe("Alert: NULL WeatherData!");
			}
		}
	}

	public WeatherController getWeatherController(Region region) {
		Validate.notNull(region);
		return getWeatherController(region.getUID());
	}

	public WeatherController getWeatherController(int regionId) {
		WeatherController wc;
		if (!controllers.containsKey(Integer.valueOf(regionId))) {
			Region region = regionManager.getRegion(regionId);
			if(region != null){
				wc = new WeatherController(regionManager.getRegion(regionId), backend);
				controllers.put(Integer.valueOf(regionId), wc);
			} else
				return null;
		} else
			wc = controllers.get(Integer.valueOf(regionId));
		return wc;
	}

	public boolean canNowBeLightning(Region region) {
		Validate.notNull(region);
		return controllers.get(Integer.valueOf(region.getUID())).isThunderingAllowed();
	}

	public void setWeatherController(int regionID, WeatherController wc) {
		Validate.notNull(wc);
		controllers.put(Integer.valueOf(regionID), wc);
	}

	public void setWeatherController(Region region, WeatherController wc) {
		Validate.notNull(wc);
		Validate.notNull(region);
		setWeatherController(region.getUID(), wc);
	}

	public RegionManager getRegionManager() {
		return regionManager;
	}

	public List<String> getWorldList() {
		return regionManager.getWorlds();
	}

	public void setRegionManager(RegionManager newThing) {
		Validate.notNull(newThing);
		regionManager = newThing;
	}

	@Override
	public void setRegionData(Region region, IWeatherData wd) {
		Validate.notNull(region);
		Validate.notNull(wd);
		wd.setRegion(region.getUID());
		weatherSituations.updateSituation(wd);
	}

	@Override
	public IWeatherData getRegionData(Region region) {
		Validate.notNull(region);
		return weatherSituations.getSituation(region.getUID());
	}

	@Override
	public void unHook(String worldName) {
		List<Integer> regions = regionManager.getRegions(Bukkit.getWorld(worldName));
		if (regions != null) {
			for (int r : regions) {
				Region region = regionManager.getRegion(r);
				getWeatherController(region).finish();
				controllers.remove(r);
				weatherSituations.remove(r);
			}
			regionManager.unHook(Bukkit.getWorld(worldName));
		}
	}

	@Override
	public boolean isHooked(World world) {
		return regionManager.isHooked(world);
	}

}