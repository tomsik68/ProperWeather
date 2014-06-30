package sk.tomsik68.pw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sk.tomsik68.pw.api.Weather;
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
        ProperWeather.log.info("" + id);
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
            ProperWeather.log.warning("The new version 1.1 removed some weather types which were included in the old 1.0.3. Please kill the server without saving PW weather data and read our ultimate migration guide if you want those weathers back. [id=" + id + "]");
            result = factorys.get("clear").create(new Object[] { region });
            break;
        }
        return result;
    }

    public static Set<String> getRegisteredWeathers() {
        return factorys.keySet();
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

    private static final Random random = new Random();

    public static Weather randomWeather(Region region) {
        ArrayList<String> keyList = new ArrayList<String>(getRegisteredWeathers());
        String name = keyList.get(random.nextInt(keyList.size()));
        WeatherFactory<?> wf = factorys.get(name);
        Weather weather = wf.create(region.getUID());
        return weather;
    }
}