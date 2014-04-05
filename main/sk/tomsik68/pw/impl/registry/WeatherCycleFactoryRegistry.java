package sk.tomsik68.pw.impl.registry;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NameAlreadyBoundException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.registry.BaseRegistry;
import sk.tomsik68.pw.config.WeatherCyclesConfig;
import sk.tomsik68.pw.impl.RandomWeatherCycle;
import sk.tomsik68.pw.impl.StoppedWeatherCycle;
import sk.tomsik68.pw.impl.factory.ClassWeatherCycleFactory;
import sk.tomsik68.pw.impl.factory.WeatherCycleFactory;
import sk.tomsik68.pw.impl.factory.YAMLWeatherCycleFactory;

public class WeatherCycleFactoryRegistry extends BaseRegistry<WeatherCycleFactory> {

    @Override
    public void load(File pluginFolder) throws IOException {
        try {
            registerClass("random", RandomWeatherCycle.class);
            registerClass("stop", StoppedWeatherCycle.class);
            File cyclesFile = new File(pluginFolder, "cycles.yml");
            if (cyclesFile.exists()) {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(cyclesFile);
                WeatherCyclesConfig config = new WeatherCyclesConfig(cfg);
                Map<String, YAMLWeatherCycleFactory> definedCycles = config.getWeatherCycles();
                for (Entry<String, YAMLWeatherCycleFactory> entry : definedCycles.entrySet()) {
                    register(entry.getKey(), entry.getValue());
                }
            }
        } catch (NameAlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public void registerClass(String name, Class<? extends WeatherCycle> clazz) throws NameAlreadyBoundException {
        register(name, new ClassWeatherCycleFactory(clazz, name));
    }
}
