package sk.tomsik68.pw.impl.backend;

import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.api.IServerBackend;

public class BukkitAPIBackend implements IServerBackend {

	@Override
	public void setRaining(Player player, boolean raining) {
		player.setPlayerWeather(raining ? WeatherType.DOWNFALL : WeatherType.CLEAR);
	}

	@Override
	public String getName() {
		return "BukkitApi";
	}

	@Override
	public void reset(Player player) {
		setRaining(player, player.getWorld().hasStorm());
	}

	@Override
	public void setClouds(Player p, boolean clouds) {

	}

	@Override
	public void setCloudsColor(Player p, int cloudsColorRGB) {

	}

	@Override
	public void setCloudsHeight(Player p, int cloudsHeight) {

	}

	@Override
	public void setFogColor(Player p, int fogColorRGB) {

	}

	@Override
	public void setMoonSize(Player p, int moonSize) {

	}

	@Override
	public void setSkyColor(Player p, int skyColorRGB) {

	}

	@Override
	public void setStarFrequency(Player p, int starFrequency) {

	}

	@Override
	public void setSunSize(Player p, int sunSize) {

	}
}