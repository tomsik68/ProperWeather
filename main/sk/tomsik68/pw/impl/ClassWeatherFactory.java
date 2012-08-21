package sk.tomsik68.pw.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;

public class ClassWeatherFactory<W extends Weather> implements WeatherFactory<W> {
    private final Class<W> clazz;

    public ClassWeatherFactory(Class<W> clazz1) {
        this.clazz = clazz1;
    }

    public W create(Object[] args) {
        if ((args != null) && (args.length >= 1) && (args[0] != null) && ((args[0] instanceof Integer))) {
            try {
                return (W) clazz.getConstructor(new Class[] { WeatherDescription.class, Integer.class }).newInstance(new Object[] { ProperWeather.instance().getWeatherDescription(clazz.getSimpleName().replace("Weather", "")), args[0] });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public WeatherDefaults getDefaults() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Defaults.class)) {
                field.setAccessible(true);
                try {
                    return (WeatherDefaults) field.get(clazz.getConstructor(new Class[] { WeatherDescription.class, Integer.class }).newInstance(new Object[] { null, Integer.valueOf(-1) }));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}