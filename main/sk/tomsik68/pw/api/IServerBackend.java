package sk.tomsik68.pw.api;

import java.awt.Color;

import org.bukkit.entity.Player;

/**
 * Here comes the `ugly` code that works differently on each server implementation.
 * 
 */
public interface IServerBackend {
    public void setRaining(Player player, boolean raining);
    
    

    public String getName();



    public void setClouds(Player p, boolean clouds);



    public void setCloudsColor(Player p, Color cloudsColor);



    public void setCloudsHeight(Player p, int cloudsHeight);



    public void setFogColor(Player p, Color fogColor);



    public void setMoonSize(Player p, int moonSize);



    public void setSkyColor(Player p, Color skyColor);



    public void setStarFrequency(Player p, int starFrequency);



    public void setSunSize(Player p, int sunSize);
}
