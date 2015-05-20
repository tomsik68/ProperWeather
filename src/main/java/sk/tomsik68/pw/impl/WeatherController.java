package sk.tomsik68.pw.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.plugin.ProjectileManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.struct.WeatherStatusStructure;

public class WeatherController {
	private WeatherStatusStructure struct = new WeatherStatusStructure();

	protected final Region region;
	protected Set<BaseWeatherElement> elements = new HashSet<BaseWeatherElement>();
	protected final IServerBackend backend;

	public WeatherController(Region region1, IServerBackend backend) {
		Validate.notNull(region1, "I'll not construct with NULL region!");
		Validate.notNull(backend, "I'll not construct with NULL backend!");
		this.region = region1;
		this.backend = backend;
	}

	public Region getRegion() {
		return this.region;
	}

	public void update(Player p) {
		if (p.hasMetadata("pw.weather")) {
			List<MetadataValue> values = p.getMetadata("pw.weather");
			for (MetadataValue value : values) {
				if (value.getOwningPlugin().equals(ProperWeather.instance())) {
					String playerWeatherCode = value.asString();
					if (!playerWeatherCode.equalsIgnoreCase(struct.getHash())) {
						setValues(p);
					}
				}
			}

		} else {
			setValues(p);
		}
	}

	private void setValues(Player p) {
		backend.setRaining(p, isRaining());
		backend.setClouds(p, isClouds());
		backend.setCloudsColor(p, getCloudsColorRGB());
		backend.setCloudsHeight(p, getCloudsHeight());
		backend.setFogColor(p, getFogColorRGB());
		backend.setMoonSize(p, getMoonSize());
		backend.setSkyColor(p, getSkyColorRGB());
		backend.setStarFrequency(p, getStarFrequency());
		backend.setSunSize(p, getSunSize());
		p.setMetadata("pw.weather", new FixedMetadataValue(ProperWeather.instance(), struct.getHash()));
	}

	public void strike(Location location) {
		this.region.getWorld().strikeLightning(location);
	}

	public Set<BaseWeatherElement> getActiveElements() {
		return elements;
	}

	public void activateElement(BaseWeatherElement element) {
		element.activate(this);
		elements.add(element);
	}

	public void deactivateElement(BaseWeatherElement element) {
		element.deactivate(this);
		elements.remove(element);
	}

	public void strike(int x, int y, int z) {
		this.region.getWorld().strikeLightning(new Location(region.getWorld(), x, y, z));
	}

	public void strikeEntity(Entity entity) {
		this.region.getWorld().strikeLightning(entity.getLocation());
	}

	public void clear() {
		ProjectileManager.killAll();
		setRaining(false);
		setThundering(false);
		struct = new WeatherStatusStructure();
		for (BaseWeatherElement elem : elements) {
			elem.deactivate(this);
		}
		updateAll();
	}

	public void finish() {
		for (Player p : region.getPlayers()) {
			backend.reset(p);

			p.removeMetadata("pw.weather", ProperWeather.instance());
			p.removeMetadata("pw.moveTimestamp", ProperWeather.instance());
			p.removeMetadata("pw.lastRegion", ProperWeather.instance());
		}
		getRegion().getWorld();
		getRegion().getWorld().setThundering(true);
		for (BaseWeatherElement elem : elements) {
			elem.deactivate(this);
		}
		updateAll();
	}

	public void updateAll() {
		for (Player player : region.getPlayers()) {
			update(player);
		}
	}

	// rest of the methods under here are getters/setters for each individual
	// variable
	// the setters are special, because they're also responsible for calling
	// update on each player in region.

	public void setSkyColor(int rgb) {
		struct.skyColor = rgb;
		updateAll();
	}

	public int getSkyColorRGB() {
		return struct.skyColor;
	}

	public void setFogColor(int rgb) {
		struct.fogColor = rgb;
		updateAll();
	}

	public int getFogColorRGB() {
		return struct.fogColor;
	}

	public int getSunSize() {
		return struct.sunSize;
	}

	public void setSunSize(int pcent) {
		struct.sunSize = pcent;
		updateAll();
	}

	public void setStarFrequency(int pcent) {
		struct.starFrequency = pcent;
		updateAll();
	}

	public void setMoonSize(int pcent) {
		struct.moonSize = pcent;
		updateAll();
	}

	public int getMoonSize() {
		return struct.moonSize;
	}

	public int getStarFrequency() {
		return struct.starFrequency;
	}

	public boolean isStars() {
		return struct.starsVisible;
	}

	public boolean isClouds() {
		return struct.cloudsVisible;
	}

	public int getCloudsColorRGB() {
		return struct.cloudsColor;
	}

	public void setCloudsColor(int rgb) {
		struct.cloudsColor = rgb;
		updateAll();
	}

	public void setCloudsHeight(int h) {
		struct.cloudsHeight = h;
		updateAll();
	}

	public int getCloudsHeight() {
		return struct.cloudsHeight;
	}

	public void setRaining(boolean b) {
		struct.rain = b;
		updateAll();
	}

	public boolean isRaining() {
		return struct.rain;
	}

	public void setThundering(boolean b) {
		struct.thundersAllowed = b;
		updateAll();
	}

	public boolean isThunderingAllowed() {
		return struct.thundersAllowed;
	}

	public boolean isMoonVisible() {
		return struct.moonVisible;
	}

	public boolean isSun() {
		return struct.sunVisible;
	}

	public void setSnowing(boolean snow) {
		struct.snowing = snow;
		updateAll();
	}

	public boolean isSnowing() {
		return struct.snowing;
	}

	public void setMoon(boolean moon) {
		struct.moonVisible = moon;
		updateAll();
	}

	public void setSun(boolean sun) {
		struct.sunVisible = sun;
		updateAll();
	}

	public void setStars(boolean stars) {
		struct.starsVisible = stars;
		updateAll();
	}

	public void setClouds(boolean c) {
		struct.cloudsVisible = c;
		updateAll();
	}

	public void playerLeft(Player player) {
		player.removeMetadata("pw.weather", ProperWeather.instance());
	}
}