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
package sk.tomsik68.pw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.impl.ClassWeatherFactory;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.weather.WeatherArrowRain;
import sk.tomsik68.pw.weather.WeatherClear;
import sk.tomsik68.pw.weather.WeatherGodAnger;
import sk.tomsik68.pw.weather.WeatherHot;
import sk.tomsik68.pw.weather.WeatherItemRain;
import sk.tomsik68.pw.weather.WeatherMeteorStorm;
import sk.tomsik68.pw.weather.WeatherRain;
import sk.tomsik68.pw.weather.WeatherSandStorm;
import sk.tomsik68.pw.weather.WeatherStorm;
import sk.tomsik68.pw.weather.WeatherWindy;

public class WeatherManager {
    private static final Object lock = new Object();
    private static final HashMap<String, WeatherFactory<?>> factorys = new HashMap<String, WeatherFactory<?>>();
    /** Registers weather with specified class to the system. Weather will be immediately made available to user.
     * 
     * @param clazz
     */
    public static <T extends Weather> void register(Class<T> clazz) {
        synchronized (lock) {
            if (!factorys.containsKey(clazz.getSimpleName().replace("Weather", "").toLowerCase()))
                registerWeather(clazz.getSimpleName().replace("Weather", "").toLowerCase(), new ClassWeatherFactory<T>(clazz));
        }
    }

    public static Weather getWeather(int id, int region) {
        WeatherFactory<?> wf = new ArrayList<WeatherFactory<?>>(factorys.values()).get(id);
        return wf.create(new Object[] { Integer.valueOf(region) });
    }

    public static Set<String> getRegisteredWeathers() {
        return factorys.keySet();
    }

    public static Weather randomWeather(Region region) {
        return getWeather(new Random().nextInt(factorys.size() - 1), region.getUID());
    }

    public static Weather getWeatherByName(String name, Region region) {
        if (region != null)
            return ((WeatherFactory<?>) factorys.get(name.toLowerCase())).create(new Object[] { Integer.valueOf(region.getUID()) });
        return ((WeatherFactory<?>) factorys.get(name.toLowerCase())).create(null);
    }

    public static Weather getWeatherByName(String name, WeatherController wc) {
        return getWeatherByName(name, wc.getRegion());
    }

    public static String getWeatherName(int uid) {
        return factorys.keySet().toArray()[uid].toString().toLowerCase();
    }

    public static void init(WeatherInfoManager wim) {
        ArrayList<Class<? extends Weather>> classes = new ArrayList<Class<? extends Weather>>();
        classes.add(WeatherClear.class);
        classes.add(WeatherRain.class);
        classes.add(WeatherStorm.class);
        classes.add(WeatherHot.class);
        classes.add(WeatherMeteorStorm.class);
        classes.add(WeatherItemRain.class);
        classes.add(WeatherArrowRain.class);
        classes.add(WeatherSandStorm.class);
        classes.add(WeatherGodAnger.class);
        classes.add(WeatherWindy.class);
        for (Class<? extends Weather> clazz : classes) {
            register(clazz);
            try {
                wim.register(clazz.getSimpleName().replace("Weather", ""), Util.getWeatherDefaults(clazz));
            } catch (Exception e) {
                System.out.println("[ProperWeather] Error: Weather registration failed.");
                System.out.println("[ProperWeather] Weather Class: " + clazz.getSimpleName().replace("Weather", ""));
                e.printStackTrace();
            }
        }
    }

    public static boolean isRegistered(String weatherName) {
        return factorys.containsKey(weatherName.toLowerCase());
    }

    public static int getUID(String weatherName) {
        List<WeatherFactory<?>> list = new ArrayList<WeatherFactory<?>>(factorys.values());
        return list.indexOf(factorys.get(weatherName));
    }

    public static void registerWeather(String name, WeatherFactory<?> wf) {
        synchronized (lock) {
            factorys.put(name.toLowerCase(), wf);
        }
    }

    public static WeatherDefaults getWeatherDefaults(String weatherName) {
        return ((WeatherFactory<?>) factorys.get(weatherName.toLowerCase())).getDefaults();
    }
}