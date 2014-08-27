package sk.tomsik68.pw.impl.factory;

import org.bukkit.configuration.ConfigurationSection;

import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.weather.WeatherDefined;

public class DefinedWeatherFactory implements WeatherFactory<WeatherDefined> {
    private final WeatherDefinition defi;
    private final WeatherDescription desc;

    public DefinedWeatherFactory(ConfigurationSection configurationSection) {
        this.defi = new WeatherDefinition(configurationSection);
        this.desc = new WeatherDescription(configurationSection);
    }

    public WeatherDefined create(int region) {
        WeatherDefined result = new WeatherDefined(desc, region, defi);
        return result;
    }

    public WeatherDefaults getDefaults() {
        return WeatherDefined.def;
    }
}