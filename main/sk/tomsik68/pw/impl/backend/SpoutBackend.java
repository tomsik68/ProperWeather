package sk.tomsik68.pw.impl.backend;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import sk.tomsik68.pw.api.IServerBackend;

public class SpoutBackend implements IServerBackend {
    private final SpoutManager spout;

    public SpoutBackend() {
        spout = SpoutManager.getInstance();
    }

    @Override
    public void setRaining(Player player, boolean raining) {
        SpoutPlayer p = spout.getPlayer(player);
    }

    @Override
    public String getName() {
        return "spout";
    }

    @Override
    public void setClouds(Player p, boolean clouds) {
	
    }

    @Override
    public void setCloudsColor(Player p, Color cloudsColor) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setCloudsHeight(Player p, int cloudsHeight) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setFogColor(Player p, Color fogColor) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setMoonSize(Player p, int moonSize) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setSkyColor(Player p, Color skyColor) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setStarFrequency(Player p, int starFrequency) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setSunSize(Player p, int sunSize) {
	// TODO Auto-generated method stub
	
    }

}
