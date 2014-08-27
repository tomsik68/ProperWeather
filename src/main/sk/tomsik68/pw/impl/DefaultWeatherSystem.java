package sk.tomsik68.pw.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;

import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.files.impl.weatherdata.WeatherDataFile;
import sk.tomsik68.pw.files.impl.weatherdata.WeatherSaveEntry;
import sk.tomsik68.pw.files.impl.weatherdata.Weathers110Format;
import sk.tomsik68.pw.files.impl.weatherdata.WeathersFileFormat;
import sk.tomsik68.pw.impl.registry.WeatherCycleFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.struct.WeatherDatav5;

public class DefaultWeatherSystem implements WeatherSystem {
    private Map<Integer, IWeatherData> weatherData;
    private Map<Integer, WeatherController> controllers;
    private RegionManager regionManager = new SimpleRegionManager();
    private Random rand = new Random();
    private final WeatherFactoryRegistry weathers;
    private final IServerBackend backend;
    private final WeatherCycleFactoryRegistry cycles;
    private WeatherDataFile dataFile;
    private final Object changeLock = new Object();

    public DefaultWeatherSystem(WeatherFactoryRegistry weathers, WeatherCycleFactoryRegistry cycles, IServerBackend backend) {
        weatherData = new HashMap<Integer, IWeatherData>();
        controllers = new HashMap<Integer, WeatherController>();
        this.weathers = weathers;
        this.backend = backend;
        this.cycles = cycles;
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
            if (controllers.containsKey(r)) {
                controllers.get(r).finish();
                controllers.remove(r);
            }
            // get rid of old weather data
            weatherData.remove(r);
            // create new weather data
            IWeatherData wd = createDefaultWeatherData();
            weatherData.put(r, wd);
            // set cycle for the data
            WeatherCycle cycle = cycles.get(cycleName).create(this);
            wd.setCycle(cycle);
            // start the initial weather or set weather to clear with -1
            // duration to get replaced
            if (startWeather.length() == 0) {
                wd.setDuration(-1);
                setRegionalWeather(weathers.get("clear").create(r), r);
            } else {
                setRegionalWeather(weathers.get(startWeather).create(r), r);
            }
            weatherData.put(r, wd);
        }

    }

    @Override
    public void setRegionalWeather(Weather w, int r) {
        synchronized (changeLock) {
            if (controllers.containsKey(r)) {
                controllers.get(r).finish();
                controllers.remove(r);
            }
            IWeatherData wd = weatherData.get(r);
            Validate.notNull(wd);
            wd.setCurrentWeather(w);
            w.initWeather();
            getWeatherController(r).updateAll();
            weatherData.put(r, wd);
        }
    }

    public synchronized void deInit() throws Exception {
        regionManager.saveRegions();
///*        // TODO go away!! begin
        ArrayList<WeatherSaveEntry> toSave = new ArrayList<WeatherSaveEntry>();
        for (Entry<Integer, IWeatherData> entry : weatherData.entrySet()) {
            WeatherSaveEntry save = new WeatherSaveEntry();
            save.duration = entry.getValue().getDuration();
            save.region = entry.getValue().getRegion();
            save.weather = entry.getValue().getCurrentWeather().getName();
            save.cycle = entry.getValue().getCycle().getName();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            entry.getValue().getCycle().saveState(oos);
            oos.flush();
            baos.flush();
            oos.close();
            baos.close();
            save.cycleData = baos.toByteArray();
            toSave.add(save);
        }
        // go away!! end*/
        //dataFile.saveData(this);
    }

    public void init() throws Exception {
        regionManager.loadRegions();
        dataFile = new WeatherDataFile(new File(ProperWeather.instance().getDataFolder(), "data.dat"));

        WeathersFileFormat format = dataFile.loadData();
        format.loadDataToWS(this);

        // cancel raining, so minecraft server doesn't change it(raining is
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
                    // avoid null WeatherData
                    Validate.notNull(wd);
                    weatherData.put(r, wd);
                } catch (Exception e) {
                    ProperWeather.log.severe("WeatherCycle has malfunctioned. Class: " + wd.getCycle().getClass().getName());
                    e.printStackTrace();
                }
                if (wd.getCurrentWeather() != null && (rand.nextInt(100) <= wd.getCurrentWeather().getRandomTimeProbability())) {
                    wd.getCurrentWeather().onRandomTime();
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
            wc = new WeatherController(regionManager.getRegion(regionId), backend);
            controllers.put(Integer.valueOf(regionId), wc);
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

    private IWeatherData createDefaultWeatherData() {
        WeatherDatav5 wd = new WeatherDatav5();
        return wd;
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
        weatherData.put(region.getUID(), wd);
    }

    @Override
    public IWeatherData getRegionData(Region region) {
        Validate.notNull(region);
        return weatherData.get(region.getUID());
    }

    @Override
    public void unHook(String worldName) {
        List<Integer> regions = regionManager.getRegions(Bukkit.getWorld(worldName));
        for (int r : regions) {
            Region region = regionManager.getRegion(r);
            getWeatherController(region).finish();
            controllers.remove(r);
            weatherData.remove(r);
        }
        regionManager.unHook(Bukkit.getWorld(worldName));
    }

    @Override
    public boolean isHooked(World world) {
        return regionManager.isHooked(world);
    }

}