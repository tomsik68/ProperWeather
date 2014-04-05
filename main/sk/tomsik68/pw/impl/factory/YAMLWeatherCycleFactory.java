package sk.tomsik68.pw.impl.factory;

import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.config.EOrder;
import sk.tomsik68.pw.impl.YAMLWeatherCycle;

public class YAMLWeatherCycleFactory extends WeatherCycleFactory {
    private final List<String> weathers;
    private final boolean stop;
    private final String name;
    private EOrder order;

    public YAMLWeatherCycleFactory(ConfigurationSection cs) {
        weathers = cs.getStringList("weathers");
        stop = cs.getBoolean("stop");
        name = cs.getName();
        if(!stop)
            order = EOrder.valueOf(cs.getString("order").toUpperCase());

    }

    @Override
    public WeatherCycle create(WeatherSystem ws) {
        WeatherCycle result = new YAMLWeatherCycle(ws, stop, order, name, weathers);
        return result;
    }

}
