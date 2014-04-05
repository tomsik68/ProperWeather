package sk.tomsik68.pw.impl.factory;

import java.lang.reflect.InvocationTargetException;

import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;

public class ClassWeatherCycleFactory extends WeatherCycleFactory {
    private final Class<? extends WeatherCycle> clazz;
    private final String name;

    public ClassWeatherCycleFactory(Class<? extends WeatherCycle> clz, String name) {
        clazz = clz;
        this.name = name;
    }

    @Override
    public WeatherCycle create(WeatherSystem ws) {
        try {
            return clazz.getConstructor(WeatherSystem.class, String.class).newInstance(ws, name);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
