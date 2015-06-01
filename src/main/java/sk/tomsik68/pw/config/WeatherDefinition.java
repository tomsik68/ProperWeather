package sk.tomsik68.pw.config;

import java.awt.Color;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import sk.tomsik68.pw.plugin.ProperWeather;

public final class WeatherDefinition {
	private final ConfigurationSection cs;

	public WeatherDefinition(ConfigurationSection s) {
		this.cs = s;
	}

	public String getName() {
		return this.cs.getName();
	}

	public Color getSkyColor() {
		return getColor("sky-color");
	}

	public Color getColor(String key) {
		Color result = null;
		if (this.cs.isConfigurationSection(key)) {
			ConfigurationSection c = this.cs.getConfigurationSection(key);
			int r = 0;
			int g = 0;
			int b = 0;
			if (c.contains("r"))
				r = c.getInt("r");
			else if (c.contains("red"))
				r = c.getInt("red");
			if (c.contains("g"))
				g = c.getInt("g");
			else if (c.contains("green"))
				g = c.getInt("green");
			if (c.contains("b"))
				b = c.getInt("b");
			else if (c.contains("blue"))
				b = c.getInt("blue");
			result = new Color(r, g, b);
			return result;
		}
		if (this.cs.isList(key)) {
			List<?> list = this.cs.getList(key);
			result = new Color(((Integer) list.get(0)).intValue(), ((Integer) list.get(1)).intValue(), ((Integer) list.get(2)).intValue());
			return result;
		}
		if (this.cs.isString(key)) {
			try {
				result = (Color) Color.class.getField(this.cs.getString(key)).get(null);
				if (result == null)
					throw new NullPointerException();
			} catch (Exception e) {
				ProperWeather.log.severe("[ProperWeather] Invalid WeatherDefinition color: " + this.cs.getString(key));
			}
		}
		return result;
	}

	public Color getFogColor() {
		return getColor("fog-color");
	}

	public Color getCloudsColor() {
		return getColor("clouds-color");
	}

	public int getSunSize() {
		return this.cs.getInt("sun-size");
	}

	public int getMoonSize() {
		return this.cs.getInt("moon-size");
	}

	public int getStarFrequency() {
		return this.cs.getInt("stars-freq");
	}

	public int getCloudsHeight() {
		return this.cs.getInt("cloud-height");
	}

	public boolean isMoonVisible() {
		return this.cs.getBoolean("moon");
	}

	public boolean isSunVisible() {
		return this.cs.getBoolean("sun");
	}

	public boolean isCloudsVisible() {
		return this.cs.getBoolean("clouds");
	}

	public boolean isStars() {
		return this.cs.getBoolean("stars");
	}

	public boolean isThunderingAllowed() {
		return this.cs.getBoolean("thundering");
	}

	public boolean isRaining() {
		return this.cs.getBoolean("raining");
	}

}