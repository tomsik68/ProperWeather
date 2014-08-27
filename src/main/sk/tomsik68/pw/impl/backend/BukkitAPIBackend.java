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
        // TODO Auto-generated method stub

    }

    @Override
    public void setClouds(Player p, boolean clouds) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCloudsColor(Player p, int cloudsColorRGB) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCloudsHeight(Player p, int cloudsHeight) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFogColor(Player p, int fogColorRGB) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMoonSize(Player p, int moonSize) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSkyColor(Player p, int skyColorRGB) {
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