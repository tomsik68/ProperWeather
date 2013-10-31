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
package sk.tomsik68.pw.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherDescription {
    private ConfigurationSection cs;
    public static final List<String> allBiomes = new ArrayList<String>();
    static{
        for(Biome biome : Biome.values()){
            allBiomes.add(biome.name().toLowerCase());
        }
    }
    public WeatherDescription(ConfigurationSection s) {
        cs = s;
    }

    public int getRandomTimeProbability() {
        return cs.getInt("rand-time-probability", 0);
    }

    public int getMaxDuration() {
        return cs.getInt("max-duration");
    }

    public boolean canBeAfter(String previous) {
        return !cs.getList("cant-be-after").contains(previous);
    }

    public int getProbability() {
        return cs.getInt("probability");
    }

    public static void generateDefaultWeathersConfig(File file, Collection<String> weathers) {
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
            for(Biome biome : Biome.values()){
                allBiomes.add(biome.name().toLowerCase());
            }
            for (String weather : weathers) {
                instance = ProperWeather.instance().getWeatherDefaults(weather);
                if (instance == null)
                    throw new NullPointerException("Weather not found.");
                config.set(weather + ".probability", Integer.valueOf(instance.getDefProbability()));
                config.set(weather + ".max-duration", Integer.valueOf(instance.getDefMaxDuration()));
                config.set(weather + ".rand-time-probability", Integer.valueOf(instance.getDefRandomTimeProbability()));
                config.set(weather + ".cant-be-after", Arrays.asList(instance.getDefCantBeAfter()));
                config.set(weather + ".customs", null);
                config.set(weather + ".active-elements", instance.getDefElements());
                config.set(weather + ".biomes", allBiomes);
            }
            config.save(file);
            ProperWeather.log.fine("Weather description file created at: " + file.getAbsolutePath());
        } catch (Exception e) {
            ProperWeather.log.severe("Weather description file creation error: ");
            e.printStackTrace();
        }
    }

    public String getName() {
        return cs.getName();
    }

    public String getCustomNode(int id) {
        return cs.getStringList("customs").get(id);
    }
    public List<String> getAllowedBiomes(){
        return cs.getStringList("biomes");
    }
    public List<String> getActiveElements(){
        return cs.getStringList("active-elements");
    }
}