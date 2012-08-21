package sk.tomsik68.pw.config;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import sk.tomsik68.pw.WeatherManager;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherDescription {
    private ConfigurationSection cs;

    public WeatherDescription(ConfigurationSection s) {
        this.cs = s;
    }

    public int getRandomTimeProbability() {
        return this.cs.getInt("rand-time-probability", 0);
    }

    public int getMaxDuration() {
        return this.cs.getInt("max-duration");
    }

    public boolean canBeAfter(int previous) {
        return !this.cs.getList("cant-be-after").contains(WeatherManager.getWeatherName(previous));
    }

    public int getProbability() {
        return this.cs.getInt("probability");
    }

    public static void generateDefaultWeathersConfig(File file) {
        try {
            Set<String> weathers = WeatherManager.getRegisteredWeathers();
            file.createNewFile();
            YamlConfiguration config = new YamlConfiguration();
            StringBuilder sb = new StringBuilder();
            for (String w : weathers) {
                sb = sb.append(w).append(',');
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            config.options().header("Weathers: " + sb.toString());
            WeatherDefaults instance = null;
            for (String weather : weathers) {
                instance = ProperWeather.instance().getWeatherDefaults(weather);
                if (instance == null)
                    throw new NullPointerException("Weather not found.");
                config.set(weather + ".probability", Integer.valueOf(instance.getDefProbability()));
                config.set(weather + ".max-duration", Integer.valueOf(instance.getDefMaxDuration()));
                config.set(weather + ".rand-time-probability", Integer.valueOf(instance.getDefRandomTimeProbability()));
                config.set(weather + ".cant-be-after", Arrays.asList(instance.getDefCantBeAfter()));
                config.set(weather + ".customs", null);

            }
            config.save(file);
            System.out.println("[ProperWeather]Weather description file created at: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("[ProperWeather] Weather description file creation error: ");
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.cs.getName();
    }

    public String getCustomNode(int id) {
        return (String) this.cs.getStringList("customs").get(id);
    }
}