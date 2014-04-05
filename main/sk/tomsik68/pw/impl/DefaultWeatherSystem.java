/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
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
import sk.tomsik68.pw.files.impl.weatherdata.WeatherFileFormat;
import sk.tomsik68.pw.files.impl.weatherdata.WeatherSaveEntry;
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
        if (controllers.containsKey(r)) {
            controllers.get(r).finish();
            controllers.remove(r);
        }

        IWeatherData wd = weatherData.get(r);
        if (wd == null) {
            wd = createDefaultWeatherData();
        }

        WeatherCycle cycle = cycles.get(cycleName).create(this);
        wd.setCycle(cycle);
        if (startWeather != null && !startWeather.isEmpty()) {
            wd.setCurrentWeather(ProperWeather.instance().getWeathers().get(startWeather).create(r));
        } else {
            wd.setCurrentWeather(ProperWeather.instance().getWeathers().get("clear").create(r));
        }
        wd.getCurrentWeather().initWeather();
        getWeatherController(r).updateAll();
        weatherData.put(r, wd);
    }

    public synchronized void deInit() throws Exception {
        ArrayList<WeatherSaveEntry> toSave = new ArrayList<WeatherSaveEntry>();
        for (Entry<Integer, IWeatherData> entry : weatherData.entrySet()) {
            WeatherSaveEntry save = new WeatherSaveEntry();
            save.duration = entry.getValue().getDuration();
            save.region = entry.getValue().getRegion();
            save.weather = entry.getValue().getCurrentWeather().getName();
            save.cycle = entry.getValue().getCycle().getName();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            entry.getValue().getCycle().saveState(new ObjectOutputStream(baos));
            baos.flush();
            baos.close();
            save.cycleData = baos.toByteArray();
            toSave.add(save);
        }
        dataFile.saveData(new WeatherFileFormat(toSave));
        regionManager.saveRegions();
    }

    public void init() throws Exception {
        regionManager.loadRegions();
        dataFile = new WeatherDataFile(new File(ProperWeather.instance().getDataFolder(), "data.dat"));
        WeatherFileFormat format = dataFile.loadData();
        for (WeatherSaveEntry entry : format.getData()) {
            try {
                regionManager.getRegion(entry.region).getWorld();
            } catch (Exception e) {
                continue;
            }
            IWeatherData wd = createDefaultWeatherData();
            wd.setDuration(entry.duration);
            wd.setRegion(entry.region);
            Weather weather = weathers.createWeather(entry.weather, entry.region);
            weather.initWeather();
            wd.setCurrentWeather(weather);
            WeatherCycle cycle = cycles.get(entry.cycle).create(this);
            if (entry.cycleData != null) {
                cycle.loadState(new ObjectInputStream(new ByteArrayInputStream(entry.cycleData)));
            }
            wd.setCycle(cycle);
            weatherData.put(entry.region, wd);
        }
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
                if (rand.nextInt(100) <= wd.getCurrentWeather().getRandomTimeProbability()) {
                    wd.getCurrentWeather().onRandomTime();
                }
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