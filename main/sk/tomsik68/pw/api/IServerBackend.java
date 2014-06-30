package sk.tomsik68.pw.api;

import org.bukkit.entity.Player;

public interface IServerBackend {
    public void reset(Player player);

    public void setRaining(Player player, boolean raining);

    public String getName();

    public void setClouds(Player p, boolean clouds);

    public void setCloudsColor(Player p, int cloudsColorRGB);

    public void setCloudsHeight(Player p, int cloudsHeight);

    public void setFogColor(Player p, int fogColorRGB);

    public void setMoonSize(Player p, int moonSize);

    public void setSkyColor(Player p, int skyColorRGB);

    public void setStarFrequency(Player p, int starFrequency);

    public void setSunSize(Player p, int sunSize);
}
