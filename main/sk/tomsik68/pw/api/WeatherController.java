package sk.tomsik68.pw.api;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import sk.tomsik68.pw.region.Region;

public abstract interface WeatherController
{
  public abstract Region getRegion();

  public abstract void setSkyColor(Color paramColor);

  public abstract Color getSkyColor();

  public abstract void setFogColor(Color paramColor);

  public abstract Color getFogColor();

  public abstract int getSunSize();

  public abstract void setSunSize(int sunSize);

  public abstract void setStarFrequency(int starFreq);

  public abstract void setMoonSize(int moonSize);

  public abstract int getMoonSize();

  public abstract int getStarFrequency();

  public abstract void hideMoon();

  public abstract void showMoon();

  public abstract void hideSun();

  public abstract void showSun();

  public abstract void hideStars();

  public abstract void showStars();

  public abstract boolean isStars();

  public abstract boolean isClouds();

  public abstract void showClouds();

  public abstract void hideClouds();

  public abstract Color getCloudsColor();

  public abstract void setCloudsColor(Color cloudColor);

  public abstract void setCloudsHeight(int h);

  public abstract int getCloudsHeight();

  public abstract void setRaining(boolean rain);

  public abstract boolean isRaining();

  public abstract boolean isThundering();

  public abstract void setThundering(boolean thunder);

  public abstract void strike(int x, int y, int z);
  
  public abstract void strike(Location location);
  
  public abstract void strikeEntity(Entity paramEntity);

  public abstract void clear();

  public abstract boolean isThunderingAllowed();

  public abstract void allowThundering();

  public abstract void denyThundering();

  public abstract boolean isMoonVisible();

  public abstract boolean isSun();

  public abstract void setFogDistance(int fogDist);

  public abstract int getFogDistance();

  public abstract void update();

  public abstract void update(Player player);
}