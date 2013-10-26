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

import org.apache.commons.lang.NotImplementedException;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.impl.ClassWeatherFactory;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.weather.WeatherClear;
import sk.tomsik68.pw.weather.WeatherRain;
import sk.tomsik68.pw.weather.WeatherStorm;

public class WeatherManager {
    private static final Object lock = new Object();
    private static final HashMap<String, WeatherFactory<?>> factorys = new HashMap<String, WeatherFactory<?>>();

    /**
     * Registers weather with specified class to the system. Weather will be
     * immediately made available to user.
     * 
     * @param clazz
     */
    public static <T extends Weather> void register(Class<T> clazz) {
        synchronized (lock) {
            if (!factorys.containsKey(clazz.getSimpleName().replace("Weather", "").toLowerCase()))
                registerWeather(clazz.getSimpleName().replace("Weather", "").toLowerCase(), new ClassWeatherFactory<T>(clazz));
        }
    }

    @Deprecated
    public static Weather getWeather(int id, int region) {
        Weather result;
        ProperWeather.log.info(""+id);
        switch (id) {
        case 0:
            result = factorys.get("clear").create(region);
            break;
        case 1:
            result = factorys.get("rain").create(region);
            break;
        case 7:
            result = factorys.get("storm").create(region);
            break;
        default:
            ProperWeather.log.warning("The new version 1.1 removed some weather types which were included in the old 1.0.3. Please kill the server without saving PW weather data and read our ultimate migration guide if you want those weathers back. [id="+id+"]");
            result = factorys.get("clear").create(new Object[] { region });
            break;
        }
        return result;
    }

    public static Set<String> getRegisteredWeathers() {
        return factorys.keySet();
    }

    @Deprecated
    public static Weather randomWeather(Region region) {
        throw new NotImplementedException("This method is moving somewhere else!");
    }

    public static Weather getWeatherByName(String name, Region region) {
        if (region != null)
            getWeatherByName(name, region.getUID());
        return getWeatherByName(name, -1);
    }

    @Deprecated
    /**
     * Should work in theory, but no guarantee... 
     */
    public static String getWeatherName(int uid) {
        return factorys.keySet().toArray()[uid].toString().toLowerCase();
    }

    public static void init(WeatherInfoManager wim) {
        ArrayList<Class<? extends Weather>> classes = new ArrayList<Class<? extends Weather>>();
        classes.add(WeatherClear.class);
        classes.add(WeatherRain.class);
        classes.add(WeatherStorm.class);
        for (Class<? extends Weather> clazz : classes) {
            register(clazz);
            try {
                wim.register(clazz.getSimpleName().replace("Weather", ""), Util.getWeatherDefaults(clazz));
            } catch (Exception e) {
                ProperWeather.log.severe("Error: Weather registration failed.");
                ProperWeather.log.severe("Weather Class: " + clazz.getSimpleName().replace("Weather", ""));
                e.printStackTrace();
            }
        }
    }

    public static boolean isRegistered(String weatherName) {
        return factorys.containsKey(weatherName.toLowerCase());
    }
    @Deprecated
    public static int getUID(String weatherName) {
        List<WeatherFactory<?>> list = new ArrayList<WeatherFactory<?>>(factorys.values());
        return list.indexOf(factorys.get(weatherName));
    }

    public static void registerWeather(String name, WeatherFactory<?> wf) {
        synchronized (lock) {
            factorys.put(name.toLowerCase(), wf);
            ProperWeather.log.finest(String.format("Registering '%s' with factory '%s'", name, wf.getClass().getName()));
        }
    }

    public static WeatherDefaults getWeatherDefaults(String weatherName) {
        return ((WeatherFactory<?>) factorys.get(weatherName.toLowerCase())).getDefaults();
    }

    public static Weather getWeatherByName(String weather, int region) {
        WeatherFactory<?> wf = factorys.get(weather.toLowerCase());
        Weather result = wf.create(new Object[] { region });
        return result;
    }
}