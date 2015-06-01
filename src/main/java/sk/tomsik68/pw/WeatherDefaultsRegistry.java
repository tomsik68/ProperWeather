package sk.tomsik68.pw;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;

public final class WeatherDefaultsRegistry extends BaseRegistry<WeatherDefaults> {
	private FileConfiguration weatherSettings;
	private File weatherSettingsFile;

	public WeatherDescription getWeatherDescription(String weatherName) {
		weatherName = weatherName.toLowerCase();
		if (!weatherSettings.isConfigurationSection(weatherName)) {
			WeatherDefaults defaults = get(weatherName);
			Validate.notNull(defaults);
			Map<String, Object> sectionValues = defaults.serialize();
			weatherSettings.createSection(weatherName, sectionValues);

			try {
				weatherSettings.save(weatherSettingsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		WeatherDescription wd = new WeatherDescription(weatherSettings.getConfigurationSection(weatherName));

		return wd;
	}

	public void generateDefaultWeathersConfig(File file, Collection<String> weathers) {
		try {
			file.createNewFile();
			YamlConfiguration config = new YamlConfiguration();
			StringBuilder sb = new StringBuilder();
			for (String w : weathers) {
				sb = sb.append(w).append(',');
			}
			sb = sb.deleteCharAt(sb.length() - 1);
			config.options().header("Weathers: " + sb.toString());
			WeatherDefaults instance = null;
			List<String> allBiomes = new ArrayList<String>();
			for (Biome biome : Biome.values()) {
				allBiomes.add(biome.name().toLowerCase());
			}
			for (String weather : weathers) {
				instance = ProperWeather.instance().getWeathers().get(weather).getDefaults();
				if (instance == null)
					throw new NullPointerException("WeatherDefaults for `" + weather + "` not found.");
				config.set(weather, instance.serialize());
			}
			config.save(file);
			ProperWeather.log.fine("Weather description file created at: " + file.getAbsolutePath());
		} catch (Exception e) {
			ProperWeather.log.severe("Weather description file creation error: ");
			e.printStackTrace();
		}
	}

	public void createDefaultsInFile(File dataFolder) {
		if (weatherSettingsFile == null)
			weatherSettingsFile = new File(dataFolder, "weather_settings.yml");
		if (!weatherSettingsFile.exists() || (weatherSettingsFile.exists() && weatherSettingsFile.length() == 0))
			generateDefaultWeathersConfig(weatherSettingsFile, elements.keySet());
		weatherSettings = YamlConfiguration.loadConfiguration(weatherSettingsFile);
		elements.clear();
		for (Entry<String, WeatherDefaults> entry : elements.entrySet()) {
			String weatherName = entry.getKey();
			WeatherDefaults wd = entry.getValue();
			if (!weatherSettings.contains(weatherName) || !weatherSettings.isConfigurationSection(weatherName)) {
				get(weatherName);
				Validate.notNull(wd);

				weatherSettings.set(weatherName, wd.serialize());
				try {
					weatherSettings.save(weatherSettingsFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}