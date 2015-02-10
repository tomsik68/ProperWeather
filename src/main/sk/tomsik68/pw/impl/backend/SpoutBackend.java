package sk.tomsik68.pw.impl.backend;

import org.bukkit.entity.Player;
/*import org.getspout.spoutapi.SpoutManager;
 import org.getspout.spoutapi.player.SpoutPlayer;*/

import sk.tomsik68.pw.api.IServerBackend;

// TODO Implement spout backend
public class SpoutBackend implements IServerBackend {

	public SpoutBackend() {
	}

	@Override
	public void setRaining(Player player, boolean raining) {
		// SpoutPlayer p = SpoutManager.getPlayer(player);
	}

	@Override
	public String getName() {
		return "spout";
	}

	@Override
	public void reset(Player player) {

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
