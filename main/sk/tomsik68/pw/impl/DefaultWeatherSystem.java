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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;

import sk.tomsik68.pw.DataManager;
import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.impl.registry.weather.WeatherFactoryRegistry;
import sk.tomsik68.pw.mv.MVInteraction;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.spout.SpoutWeatherController;
import sk.tomsik68.pw.struct.WeatherData;
import sk.tomsik68.pw.struct.WeatherDataExt;

public class DefaultWeatherSystem implements WeatherSystem {
    private Map<Integer, WeatherDataExt> weatherData;
    private Map<Integer, WeatherController> controllers;
    private Map<Integer, WeatherCycle> cycles;
    private RegionManager regionManager = new SimpleRegionManager();
    private Random rand = new Random();
    private final WeatherFactoryRegistry weathers;

    public DefaultWeatherSystem(WeatherFactoryRegistry weathers) {
        weatherData = new HashMap<Integer, WeatherDataExt>();
        controllers = new HashMap<Integer, WeatherController>();
        cycles = new HashMap<Integer, WeatherCycle>();
        this.weathers = weathers;

    }

    public void runWeather(String worldName) {
        if (isHooked(Bukkit.getWorld(worldName))) {
            unHook(worldName);
        }
        if (!regionManager.isHooked(Bukkit.getWorld(worldName))) {
            regionManager.hook(Bukkit.getWorld(worldName), ProperWeather.instance().getConfigFile().getRegionType(worldName));
        }
        for (Iterator<?> localIterator = regionManager.getRegions(Bukkit.getWorld(worldName)).iterator(); localIterator.hasNext();) {
            int r = ((Integer) localIterator.next()).intValue();
            Region region = regionManager.getRegion(Integer.valueOf(r));
            if (!cycles.containsKey(r))
                cycles.put(r, new RandomWeatherCycle(this));
            weatherData.put(Integer.valueOf(r), new WeatherDataExt());

            getRegionData(region).setCanEverChange(true);
            Weather weather = cycles.get(r).nextWeather(region);

            getRegionData(region).setCurrentWeather(weather);
            getRegionData(region).setDuration(rand.nextInt(getRegionData(region).getCurrentWeather().getMaxDuration()));
            weather.initWeather();
        }
    }

    private void notifyMV(String region, boolean b) {
        if (ProperWeather.instance().getConfigFile().shouldNotifyMV())
            MVInteraction.getInstance().notifyChange(region, b);
    }

    public void stopAtWeather(String worldName, String weatherName) {
        List<Integer> reg = regionManager.getRegions(Bukkit.getWorld(worldName));
        if (reg == null) {
            hook(Bukkit.getWorld(worldName));
        }
        WeatherDataExt wd = new WeatherDataExt();
        wd.setCanEverChange(false);
        wd.setDuration(-1);
        reg = regionManager.getRegions(Bukkit.getWorld(worldName));
        for (int r : reg) {
            stopAtWeather(r, weatherName);
        }
        notifyMV(worldName, false);
    }

    public void stopAtWeather(int region, String weatherName) {
        Region reg = regionManager.getRegion(region);
        if (reg == null) {
            throw new NullPointerException("Region doesn't exist.");
        }
        WeatherDataExt wd = new WeatherDataExt();
        wd.setCanEverChange(false);
        wd.setDuration(-1);
        if (!controllers.containsKey(reg.getUID()))
            controllers.put(reg.getUID(), ProperWeather.isSpout ? new SpoutWeatherController(reg) : new DefaultWeatherController(reg));
        setRegionData(reg, wd);
        Weather w = weathers.createWeather(weatherName, region);
        w.initWeather();
        getRegionData(reg).setCurrentWeather(w);
        getRegionData(reg).setCanEverChange(false);
    }

    public synchronized void deInit() {
        List<WeatherDataExt> toSave = new ArrayList<WeatherDataExt>();
        for (Entry<Integer, WeatherDataExt> entry : weatherData.entrySet()) {
            entry.getValue().setRegion(entry.getKey().intValue());
            toSave.add(entry.getValue());
        }
        regionManager.saveRegions();
        DataManager.save(toSave);
    }

    @SuppressWarnings(value = { "deprecation" })
    public void init() {
        boolean mv = false;
        mv = MVInteraction.getInstance().setup(Bukkit.getServer());
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
                            cycles.put(wd.getRegion(), new RandomWeatherCycle(this));
                            WeatherDataExt newData = new WeatherDataExt();
                            newData.setCanEverChange(wd.canEverChange());
                            newData.setDuration(wd.getDuration());
                            newData.setRegion(wd.getRegion());
                            Weather weather = weathers.createWeather(oldWeatherMap.get(wd.getNumberOfWeather()), newData.getRegion());
                            weather.initWeather();
                            newData.setCurrentWeather(weather);
                            ProperWeather.log.finest(String.format("Loaded: %s, converted to %s", wd.toString(), newData.toString()));
                            weatherData.put(Integer.valueOf(wd.getRegion()), newData);
                        } else {
                            ProperWeather.log.finest("Got a null data!!!");
                        }
                    }
                    ProperWeather.log.fine("Conversion finished.");
                } else if (toLoad.get(0) instanceof WeatherDataExt) {
                    ProperWeather.log.fine("Hell yeah! Regular save file! Loading...");
                    for (Object obj : toLoad) {
                        WeatherDataExt wd = (WeatherDataExt) obj;
                        if (wd != null) {
                            try {
                                regionManager.getRegion(wd.getRegion()).getWorld();
                            } catch (Exception e) {
                                continue;
                            }
                            Weather w = wd.getCurrentWeather();
                            w.initWeather();

                            cycles.put(wd.getRegion(), new RandomWeatherCycle(this));
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
            DataManager.save(new ArrayList<WeatherDataExt>());
            // cancel raining, so minecraft server doesn't change it(raining is
            // handled via packets in Util class)
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
            WeatherDataExt wd = getRegionData(region);
            if ((wd == null) || wd.getCurrentWeather() == null) {
                wd = new WeatherDataExt();
                wd.setCanEverChange(true);
                Weather weather;
                if (cycles.containsKey(r))
                    weather = cycles.get(r).nextWeather(region);
                else
                    weather = weathers.createWeather("clear", r);
                wd.setCurrentWeather(weather);
                wd.setDuration(weather.getMaxDuration());
                weather.initWeather();
                weatherData.put(r, wd);
                continue;
            }
            if (rand.nextInt(100) <= wd.getCurrentWeather().getRandomTimeProbability()) {
                wd.getCurrentWeather().onRandomTime();
            }
            if (!wd.canEverChange()) {
                continue;
            }
            if (wd.decrementDuration() > 0) {
                setRegionData(region, wd);
                continue;
            }

            wd.setCurrentWeather(cycles.get(r).nextWeather(region));
            wd.setDuration(rand.nextInt(wd.getCurrentWeather().getMaxDuration()));
            wd.getCurrentWeather().initWeather();
            setRegionData(region, wd);
        }
    }

    public void unHook(String worldName) {
        if (!isHooked(Bukkit.getWorld(worldName)))
            return;
        World world = Bukkit.getWorld(worldName);
        List<Integer> regions = regionManager.getRegions(world);
        for (int r : regions) {
            controllers.get(r).finish();
            weatherData.remove(r);
            controllers.remove(r);
            cycles.remove(r);
        }
        regionManager.unHook(world);

    }

    public WeatherController getWeatherController(Region region) {
        return getWeatherController(region.getUID());
    }

    public WeatherController getWeatherController(int regionId) {
        WeatherController wc;
        if (!controllers.containsKey(Integer.valueOf(regionId))) {
            wc = ProperWeather.isSpout ? new SpoutWeatherController(regionManager.getRegion(Integer.valueOf(regionId))) : new DefaultWeatherController(regionManager.getRegion(regionId));
            controllers.put(Integer.valueOf(regionId), wc);
        } else
            wc = controllers.get(Integer.valueOf(regionId));
        return wc;
    }

    public boolean canNowBeLightning(Region region) {
        return controllers.get(Integer.valueOf(region.getUID())).isThunderingAllowed();
    }

    public void changeControllers(boolean spout) {
        synchronized (controllers) {
            for (Map.Entry<Integer, WeatherController> entry : controllers.entrySet())
                if (((entry.getValue() instanceof DefaultWeatherController)) || ((entry.getValue() instanceof SpoutWeatherController))) {
                    WeatherController temp = spout ? new SpoutWeatherController(entry.getValue().getRegion()) : new DefaultWeatherController(entry.getValue().getRegion());
                    temp.setRaining(entry.getValue().isRaining());
                    if (entry.getValue().isThunderingAllowed())
                        temp.allowThundering();
                    else
                        temp.denyThundering();
                    setWeatherController(entry.getKey().intValue(), temp);
                }
        }
    }

    public void setWeatherController(int regionID, WeatherController wc) {
        controllers.put(Integer.valueOf(regionID), wc);
    }

    public void setWeatherController(Region region, WeatherController wc) {
        setWeatherController(region.getUID(), wc);
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public boolean isHooked(World world) {
        return regionManager.isHooked(world);
    }

    public void hook(World world) {
        regionManager.hook(world, ProperWeather.instance().getConfigFile().getRegionType(world.getName()));
        WeatherDataExt wd = new WeatherDataExt();
        wd.setCanEverChange(false);
        wd.setDuration(-1);
        for (Iterator<?> localIterator = regionManager.getRegions(world).iterator(); localIterator.hasNext();) {
            int regionId = ((Integer) localIterator.next()).intValue();
            weatherData.put(Integer.valueOf(regionId), wd);
            cycles.put(regionId, new RandomWeatherCycle(this));
        }
    }

    public void hook(String worldName) {
        hook(Bukkit.getWorld(worldName));
    }

    public List<String> getWorldList() {
        return regionManager.getWorlds();
    }

    public void setRegionManager(RegionManager newThing) {
        regionManager = newThing;
    }

    @Override
    public WeatherCycle getWeatherCycle(int region) {
        return cycles.get(region);
    }

    @Override
    public void setWeatherCycle(int region, WeatherCycle wc) {
        cycles.put(region, wc);
    }

    @Override
    public void setRegionData(Region region, WeatherDataExt wd) {
        weatherData.put(region.getUID(), wd);
    }

    @Override
    public WeatherDataExt getRegionData(Region region) {
        return weatherData.get(region.getUID());
    }
}