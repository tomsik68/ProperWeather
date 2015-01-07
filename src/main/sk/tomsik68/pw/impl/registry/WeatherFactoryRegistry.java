package sk.tomsik68.pw.impl.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.naming.NameAlreadyBoundException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.WeatherDefaultsRegistry;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.impl.factory.ClassWeatherFactory;
import sk.tomsik68.pw.impl.factory.DefinedWeatherFactory;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.weather.WeatherClear;
import sk.tomsik68.pw.weather.WeatherRain;
import sk.tomsik68.pw.weather.WeatherStorm;

public class WeatherFactoryRegistry extends BaseRegistry<WeatherFactory<?>> {
	private final WeatherDefaultsRegistry wim;

	public WeatherFactoryRegistry(WeatherDefaultsRegistry wim) {
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
			} catch (Exception e) {
				throw new RuntimeException("Weather registration failed for: " + weatherClass.getName(), e);
			}
		}
		/*
		 * This is now done on registration Collection<String> registered =
		 * getRegistered(); for (String name : registered) { try {
		 * wim.register(name, get(name).getDefaults()); } catch
		 * (NameAlreadyBoundException e) { e.printStackTrace(); } }
		 */
		wim.createDefaultsInFile(pluginFolder);

		// defined weathers won't be added to weatherSettings file
		loadDefinedWeathers(pluginFolder);
	}

	private void loadDefinedWeathers(File pluginFolder) throws IOException {
		File weatherDefs = new File(pluginFolder, "weather_defs.yml");
		if (weatherDefs.exists()) {
			FileConfiguration weatherDefinitions = YamlConfiguration.loadConfiguration(weatherDefs);
			Set<String> keys = weatherDefinitions.getKeys(false);
			for (String weather : keys) {
				if (isRegistered(weather)) {
					ProperWeather.log.severe("Weather registration problem: '" + weather + "' already exists. Please rename it in your definition file.");
				} else {
					ProperWeather.log.finest("Registering new weather: " + weather);
					try {
						register(weather, new DefinedWeatherFactory(weatherDefinitions.getConfigurationSection(weather)));
					} catch (NameAlreadyBoundException e) {
						e.printStackTrace();
					}

				}
			}
		} else
			weatherDefs.createNewFile();
	}

	private String weatherNameFromClass(Class<?> clazz) {
		String name = clazz.getSimpleName().toLowerCase().replace("weather", "");
		if (isRegistered(name)) {
			name = clazz.getName().toLowerCase();
		}
		return name;
	}

	public <W extends Weather> void registerClass(Class<W> weather) throws Exception {
		register(weatherNameFromClass(weather), new ClassWeatherFactory<W>(weather));
	}

	@Override
	public void register(String name, WeatherFactory<?> element) throws NameAlreadyBoundException {
		// in case super.register failed, don't register WeatherDefaults
		wim.register(name, element.getDefaults());
		super.register(name, element);
	}

	public Weather createWeather(String name, int region) {
		return get(name).create(region);
	}
}
