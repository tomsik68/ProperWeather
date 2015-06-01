package sk.tomsik68.pw.impl.registry;

import java.lang.reflect.InvocationTargetException;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.impl.WeatherElementFactory;

public final class ClassWeatherElementFactory<W extends BaseWeatherElement> extends WeatherElementFactory<W> {
	private Class<W> clazz;

	public ClassWeatherElementFactory(Class<W> clazz) {
		this.clazz = clazz;
	}

	@Override
	public W create(WeatherController wc) {
		try {
			return clazz.getConstructor(WeatherController.class).newInstance(wc);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

}
