package sk.tomsik68.pw.impl.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.config.EOrder;
import sk.tomsik68.pw.impl.WeatherSpec;
import sk.tomsik68.pw.impl.YAMLWeatherCycle;

public class YAMLWeatherCycleFactory extends WeatherCycleFactory {
    private final List<WeatherSpec> weatherSpecs;
    private final String name;
    private EOrder order;

    public YAMLWeatherCycleFactory(ConfigurationSection cs) throws InvalidConfigurationException {
        // weathers = cs.getStringList("weathers");
        weatherSpecs = new ArrayList<WeatherSpec>();
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> weatherSpecsList = (List<Map<String, Object>>) cs.getList("weathers");
            for (Map<String, Object> weatherSpecMap : weatherSpecsList) {
                weatherSpecs.add(new WeatherSpec(weatherSpecMap));
            }
        } catch (Exception e) {
            throw new InvalidConfigurationException(e);
        } catch (Error err) {
            throw new InvalidConfigurationException(err);
        }

        name = cs.getName();
        order = EOrder.valueOf(cs.getString("order").toUpperCase());

    }

    @Override
    public WeatherCycle create(WeatherSystem ws) {
        WeatherCycle result = new YAMLWeatherCycle(ws, order, name, weatherSpecs);
        return result;
    }

}
