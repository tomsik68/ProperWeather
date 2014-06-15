/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang.Validate;
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
            WeatherDefaults wd = defaults.get(weatherName);
            Validate.notNull(wd);
            /*
             * if (wd == null) { wd =
             * WeatherManager.getWeatherDefaults(weatherName); if (wd == null)
             * throw new NullPointerException("No data about " + weatherName);
             * defaults.put(weatherName, wd); }
             */

            weatherSettings.createSection(weatherName);
            weatherSettings.set(weatherName + ".probability", Integer.valueOf(wd.getDefProbability()));
            weatherSettings.set(weatherName + ".max-duration", Integer.valueOf(wd.getDefMaxDuration()));
            weatherSettings.set(weatherName + ".rand-time-probability", Integer.valueOf(wd.getDefRandomTimeProbability()));
            weatherSettings.set(weatherName + ".cant-be-after", Arrays.asList(wd.getDefCantBeAfter()));
            weatherSettings.set(weatherName + ".customs", null);
            weatherSettings.set(weatherName + ".active-elements", wd.getDefElements());
            try {
                weatherSettings.save(weatherSettingsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WeatherDescription wd = new WeatherDescription(weatherSettings.getConfigurationSection(weatherName.toLowerCase()));
        return wd;
    }

    public void createDefaultsInFile(File dataFolder, Collection<String> registeredWeathers) {
        if (weatherSettingsFile == null)
            weatherSettingsFile = new File(dataFolder, "weather_settings.yml");
        if (!weatherSettingsFile.exists() || (weatherSettingsFile.exists() && weatherSettingsFile.length() == 0))
            WeatherDescription.generateDefaultWeathersConfig(weatherSettingsFile, registeredWeathers);
        weatherSettings = YamlConfiguration.loadConfiguration(weatherSettingsFile);
        defaults.clear();
        for (String weatherName : registeredWeathers) {
            if (!weatherSettings.isConfigurationSection(weatherName)) {
                if (!weatherSettings.contains(weatherName)) {
                    WeatherDefaults wd = defaults.get(weatherName);
                    Validate.notNull(wd);

                    weatherSettings.createSection(weatherName);
                    weatherSettings.set(weatherName + ".probability", Integer.valueOf(wd.getDefProbability()));
                    weatherSettings.set(weatherName + ".max-duration", Integer.valueOf(wd.getDefMaxDuration()));
                    weatherSettings.set(weatherName + ".rand-time-probability", Integer.valueOf(wd.getDefRandomTimeProbability()));
                    weatherSettings.set(weatherName + ".cant-be-after", Arrays.asList(wd.getDefCantBeAfter()));
                    weatherSettings.set(weatherName + ".customs", null);
                    weatherSettings.set(weatherName + ".active-elements", wd.getDefElements());
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