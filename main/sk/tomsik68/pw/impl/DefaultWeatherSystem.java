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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;

import sk.tomsik68.pw.DataManager;
import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.spout.SpoutWeatherController;
import sk.tomsik68.pw.struct.WeatherData;
import sk.tomsik68.pw.struct.WeatherDataExt;
import sk.tomsik68.pw.struct.WeatherDatav4;

public class DefaultWeatherSystem implements WeatherSystem {
    private Map<Integer, IWeatherData> weatherData;
    private Map<Integer, WeatherController> controllers;
    private RegionManager regionManager = new SimpleRegionManager();
    private Random rand = new Random();
    private final WeatherFactoryRegistry weathers;
    private final IServerBackend backend;

    public DefaultWeatherSystem(WeatherFactoryRegistry weathers, IServerBackend backend) {
        weatherData = new HashMap<Integer, IWeatherData>();
        controllers = new HashMap<Integer, WeatherController>();
        this.weathers = weathers;
        this.backend = backend;

    }

    public void runWeather(String worldName) {
        startCycle("random", worldName, "");
    }

    public void stopAtWeather(String worldName, String weatherName) {
        startCycle("stop", worldName, weatherName);
    }

    @Override
    public void startCycle(String cycleName, String worldName, String startWeather) {
        Validate.notNull(cycleName);
        Validate.notNull(worldName);
        Validate.notNull(startWeather);
        Validate.notEmpty(cycleName);
        Validate.notEmpty(worldName);

        World world = Bukkit.getWorld(worldName);
        if (regionManager.isHooked(world)) {
            List<Integer> regions = regionManager.getRegions(world);
            for (int r : regions) {
                if (controllers.containsKey(r)) {
                    controllers.get(r).finish();
                    controllers.remove(r);
                }
            }
        } else
            regionManager.hook(Bukkit.getWorld(worldName), ProperWeather.instance().getConfigFile().getRegionType(worldName));

        List<Integer> regionsInWorld = regionManager.getRegions(Bukkit.getWorld(worldName));
        for (int r : regionsInWorld) {
            startCycleInRegion(cycleName, r, startWeather);
        }

    }

    @Override
    public void startCycleInRegion(String cycleName, int r, String startWeather) {
        IWeatherData wd = weatherData.get(r);
        if (wd == null) {
            wd = createDefaultWeatherData();
        }

        WeatherCycle cycle = ProperWeather.instance().getCycles().get(cycleName).create(this);
        wd.setCycle(cycle);
        if (startWeather != null && !startWeather.isEmpty()) {
            wd.setCurrentWeather(ProperWeather.instance().getWeathers().get(startWeather).create(r));
            wd.getCurrentWeather().initWeather();
        } else {
            wd = cycle.nextWeatherData(wd);
        }
        weatherData.put(r, wd);
    }

    public synchronized void deInit() {
        List<IWeatherData> toSave = new ArrayList<IWeatherData>();
        for (Entry<Integer, IWeatherData> entry : weatherData.entrySet()) {
            entry.getValue().setRegion(entry.getKey().intValue());
            toSave.add(entry.getValue());
        }
        regionManager.saveRegions();
        DataManager.save(toSave);
    }

    @SuppressWarnings(value = {
        "deprecation"
    })
    public void init() {
        boolean mv = false;
        regionManager.loadRegions();
        try {
            List<?> toLoad = DataManager.load();
            if ((toLoad != null) && (toLoad.size() > 0)) {
                if ((toLoad.get(0) instanceof WeatherData)) {
                    ProperWeather.log.fine("Detected v2 data file. Converting...");
                    HashMap<Integer, String> oldWeatherMap = Util.generateOLDIntWeatherLookupMap();
                    for (Object obj : toLoad) {
                        WeatherData wd = (WeatherData) obj;
                        if (wd != null) {
                            try {
                                regionManager.getRegion(Integer.valueOf(wd.getRegion())).getWorld();
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                            WeatherDataExt dataV3 = new WeatherDataExt();
                            dataV3.setCanEverChange(wd.canEverChange());
                            dataV3.setDuration(wd.getDuration());
                            dataV3.setRegion(wd.getRegion());
                            Weather weather = weathers.createWeather(oldWeatherMap.get(wd.getNumberOfWeather()), dataV3.getRegion());
                            weather.initWeather();
                            dataV3.setCurrentWeather(weather);

                            WeatherDatav4 dataV4 = new WeatherDatav4();
                            dataV4.setCycle(dataV3.getCycle());
                            weatherData.put(Integer.valueOf(wd.getRegion()), dataV4);
                        } else {
                            ProperWeather.log.finest("Got a null data!!!");
                        }
                    }
                    ProperWeather.log.fine("Conversion finished.");
                } else if (toLoad.get(0) instanceof WeatherDataExt) {
                    ProperWeather.log.fine("Detected v3 data file. Converting...");
                    for (Object obj : toLoad) {
                        WeatherDataExt oldWD = (WeatherDataExt) obj;
                        if (oldWD != null) {
                            try {
                                regionManager.getRegion(oldWD.getRegion()).getWorld();
                            } catch (Exception e) {
                                continue;
                            }
                            Weather w = oldWD.getCurrentWeather();
                            w.initWeather();
                            WeatherDatav4 wd = new WeatherDatav4();
                            wd.setCycle(oldWD.getCycle());

                            weatherData.put(oldWD.getRegion(), wd);
                        }
                    }
                    ProperWeather.log.fine("Conversion finished.");
                } else if (toLoad.get(0) instanceof WeatherDatav4) {
                    ProperWeather.log.fine("Detected v4 data file. Loading...");
                    for (Object obj : toLoad) {
                        WeatherDatav4 wd = (WeatherDatav4) obj;
                        if (wd != null) {
                            try {
                                regionManager.getRegion(wd.getRegion()).getWorld();
                            } catch (Exception e) {
                                continue;
                            }
                            Weather w = wd.getCurrentWeather();
                            w.initWeather();

                            weatherData.put(wd.getRegion(), wd);
                        }
                    }
                    ProperWeather.log.fine("Loading finished.");
                } else
                    ProperWeather.log.severe("Detected corrupted/incompatible save file. Class=" + toLoad.get(0).getClass());
            }
            if ((toLoad == null) && (mv)) {
                ProperWeather.log.info("Data file not found, but it looks like you've got multiverse installed.");
                ProperWeather.log.info("You can use /pw im to import weather settings from multiverse.");
                return;
            }
            DataManager.save(new ArrayList<WeatherDatav4>());
            // cancel raining, so minecraft server doesn't change it(raining is
            // handled by WeatherControllers, which send packets directly to
            // players)
            for (String w : getWorldList()) {
                Bukkit.getWorld(w).setStorm(false);
            }
        } catch (Exception e) {
            ProperWeather.log.severe("Can't find any data source, creating file...");
            e.printStackTrace();
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
            wc = new DefaultWeatherController(regionManager.getRegion(regionId), backend);
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
        WeatherDatav4 wd = new WeatherDatav4();
        wd.setDuration(-1);
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