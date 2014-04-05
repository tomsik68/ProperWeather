package sk.tomsik68.pw.impl.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NameAlreadyBoundException;

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.WeatherInfoManager;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.impl.factory.ClassWeatherFactory;
import sk.tomsik68.pw.weather.WeatherClear;
import sk.tomsik68.pw.weather.WeatherRain;
import sk.tomsik68.pw.weather.WeatherStorm;

/**
 * This was previously in WeatherManager, which is now deprecated
 */
public class WeatherFactoryRegistry extends BaseRegistry<WeatherFactory<?>> {
    private final WeatherInfoManager wim;

    public WeatherFactoryRegistry(WeatherInfoManager wim) {
        this.wim = wim;
    }

    @Override
    public void load(File pluginFolder) throws IOException {
        ArrayList<Class<? extends Weather>> weathers = new ArrayList<Class<? extends Weather>>();
        weathers.add(WeatherClear.class);
        weathers.add(WeatherRain.class);
        weathers.add(WeatherStorm.class);
        for (Class<? extends Weather> weatherClass : weathers) {
            try {
                registerClass(weatherClass);
                wim.register(weatherNameFromClass(weatherClass), Util.getWeatherDefaults(weatherClass));
            } catch (Exception e) {
                throw new RuntimeException("Weather registration failed for: " + weatherClass.getName(), e);
            }
        }
        Collection<String> registered = getRegistered();
        wim.init(pluginFolder, registered);
        for (String name : registered) {
            wim.register(name, get(name).getDefaults());
        }
    }

    private String weatherNameFromClass(Class<?> clazz) {
        String name = clazz.getSimpleName().toLowerCase().replace("weather", "");
        if (isRegistered(name)) {
            name = clazz.getName().toLowerCase();
        }
        return name;
    }

    public <W extends Weather> void registerClass(Class<W> weather) throws NameAlreadyBoundException {
        register(weatherNameFromClass(weather), new ClassWeatherFactory<W>(weather));
    }

    public Weather createWeather(String name, int region) {
        return get(name).create(region);
    }
}
