package sk.tomsik68.pw;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherInfoManager {
    private FileConfiguration weatherSettings;
    private final HashMap<String, WeatherDefaults> defaults = new HashMap<String, WeatherDefaults>();
    private File weatherSettingsFile;

    public WeatherDescription getWeatherDescription(String weatherName) {
        weatherName = weatherName.toLowerCase();
        if (!weatherSettings.contains(weatherName)) {
            WeatherDefaults wd = defaults.get(weatherName);
            Validate.notNull(wd);
            weatherSettings.set(weatherName, wd.serialize());
            try {
                weatherSettings.save(weatherSettingsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WeatherDescription wd = new WeatherDescription(weatherSettings.getConfigurationSection(weatherName.toLowerCase()));
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

    public void createDefaultsInFile(File dataFolder, Collection<String> registeredWeathers) {
        if (weatherSettingsFile == null)
            weatherSettingsFile = new File(dataFolder, "weather_settings.yml");
        if (!weatherSettingsFile.exists() || (weatherSettingsFile.exists() && weatherSettingsFile.length() == 0))
            generateDefaultWeathersConfig(weatherSettingsFile, registeredWeathers);
        weatherSettings = YamlConfiguration.loadConfiguration(weatherSettingsFile);
        defaults.clear();
        for (String weatherName : registeredWeathers) {
            if (!weatherSettings.isConfigurationSection(weatherName)) {
                if (!weatherSettings.contains(weatherName)) {
                    WeatherDefaults wd = defaults.get(weatherName);
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

    public void register(String w, WeatherDefaults wd) {
        defaults.put(w.toLowerCase(), wd);
    }

    public WeatherDefaults getWeatherDefaults(String weather) {
        return defaults.get(weather);
    }
}