package sk.tomsik68.pw;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;

public class WeatherInfoManager {
    private FileConfiguration weatherSettings;
    private final HashMap<String, WeatherDefaults> defaults = new HashMap<String, WeatherDefaults>();
    private File weatherSettingsFile;

    public WeatherDescription getWeatherDescription(String weatherName) {
        weatherName = weatherName.toLowerCase();
        if (!weatherSettings.contains(weatherName)) {
            WeatherDefaults wd = (WeatherDefaults) defaults.get(weatherName);
            if (wd == null) {
                wd = WeatherManager.getWeatherDefaults(weatherName);
                if (wd == null)
                    throw new NullPointerException("No data about " + weatherName);
                else
                    defaults.put(weatherName, wd);
            }

            weatherSettings.createSection(weatherName);
            weatherSettings.set(weatherName + ".probability", Integer.valueOf(wd.getDefProbability()));
            weatherSettings.set(weatherName + ".max-duration", Integer.valueOf(wd.getDefMaxDuration()));
            weatherSettings.set(weatherName + ".rand-time-probability", Integer.valueOf(wd.getDefRandomTimeProbability()));
            weatherSettings.set(weatherName + ".cant-be-after", Arrays.asList(wd.getDefCantBeAfter()));
            weatherSettings.set(weatherName + ".customs", null);

            try {
                weatherSettings.save(weatherSettingsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WeatherDescription wd = new WeatherDescription(weatherSettings.getConfigurationSection(weatherName.toLowerCase()));
        return wd;
    }

    public WeatherDefinition getWeatherDefinition(String weatherName) {
        return new WeatherDefinition(weatherSettings.getConfigurationSection(weatherName));
    }

    public void init(File dataFolder) {
        WeatherManager.init(this);
        if (weatherSettingsFile == null)
            weatherSettingsFile = new File(dataFolder, "weathers.yml");
        if (!weatherSettingsFile.exists() || (weatherSettingsFile.exists() && weatherSettingsFile.length() == 0))
            WeatherDescription.generateDefaultWeathersConfig(weatherSettingsFile);
        weatherSettings = YamlConfiguration.loadConfiguration(weatherSettingsFile);
        defaults.clear();
    }

    public void register(String w, WeatherDefaults wd) {
        defaults.put(w.toLowerCase(), wd);
    }

    public WeatherDefaults getWeatherDefaults(String weather) {
        return (WeatherDefaults) defaults.get(weather);
    }
}