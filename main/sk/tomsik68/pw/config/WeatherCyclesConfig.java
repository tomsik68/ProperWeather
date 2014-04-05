package sk.tomsik68.pw.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import sk.tomsik68.pw.impl.factory.YAMLWeatherCycleFactory;

public class WeatherCyclesConfig {
    private FileConfiguration config;

    public WeatherCyclesConfig(FileConfiguration cfg) {
        config = cfg;
    }

    public Map<String, YAMLWeatherCycleFactory> getWeatherCycles() {
        ConfigurationSection cyclesSection = config.getConfigurationSection("cycles");
        HashMap<String, YAMLWeatherCycleFactory> result = new HashMap<String, YAMLWeatherCycleFactory>();
        Set<String> cyclesKeys = cyclesSection.getKeys(false);
        for (String cycleName : cyclesKeys) {
            result.put(cycleName, new YAMLWeatherCycleFactory(cyclesSection.getConfigurationSection(cycleName)));
        }
        return result;
    }
}
