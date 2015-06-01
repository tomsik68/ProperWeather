package sk.tomsik68.pw.config;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

public final class WeatherDescription {
	private ConfigurationSection cs;

	public WeatherDescription(ConfigurationSection s) {
		Validate.notNull(s);
		cs = s;
	}

	public int getRandomTimeProbability() {
		return cs.getInt("rand-time-probability", 0);
	}

	public String getName() {
		return cs.getName();
	}

	public Map<String, Object> getCustomNodes() {
		return cs.getConfigurationSection("custom").getValues(true);
	}

	public List<String> getActiveElements() {
		return cs.getStringList("active-elements");
	}
}