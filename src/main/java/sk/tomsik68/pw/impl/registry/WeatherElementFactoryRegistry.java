package sk.tomsik68.pw.impl.registry;

import java.io.File;
import java.io.IOException;

import javax.naming.NameAlreadyBoundException;

import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.impl.WeatherElementFactory;
import sk.tomsik68.pw.weather.element.Wind;

public class WeatherElementFactoryRegistry extends BaseRegistry<WeatherElementFactory<?>> {
	public WeatherElementFactoryRegistry() {

	}

	@Override
	public void load(File pluginFolder) throws IOException {
		try {
			register(Wind.class.getName(), new ClassWeatherElementFactory<Wind>(Wind.class));
		} catch (NameAlreadyBoundException e) {
			e.printStackTrace();
		}
	}

}
