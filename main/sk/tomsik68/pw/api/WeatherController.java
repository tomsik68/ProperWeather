/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw.api;

import java.awt.Color;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.region.Region;

/**
 * Represents a controller of weather, which can do various stuff with weather.
 * 
 * @author Tomsik68
 * 
 */
public interface WeatherController {
    public Set<BaseWeatherElement> getActiveElements();

    public void activateElement(BaseWeatherElement element);

    public void deactivateElement(BaseWeatherElement element);

    /**
     * 
     * @return Region this controller is controlling
     */
    public Region getRegion();

    public void setSkyColor(Color paramColor);

    public Color getSkyColor();

    public void setFogColor(Color paramColor);

    public Color getFogColor();

    public int getSunSize();

    public void setSunSize(int sunSize);

    public void setStarFrequency(int starFreq);

    public void setMoonSize(int moonSize);

    public int getMoonSize();

    public int getStarFrequency();

    public void setMoon(boolean moon);

    public void setSun(boolean sun);

    public void setStars(boolean stars);

    public boolean isStars();

    public boolean isClouds();

    public void setClouds(boolean c);

    public Color getCloudsColor();

    public void setCloudsColor(Color cloudColor);

    public void setCloudsHeight(int h);

    public int getCloudsHeight();

    public void setRaining(boolean rain);

    public boolean isRaining();

    public boolean isThundering();

    public void setThundering(boolean thunder);

    public void strike(int x, int y, int z);

    public void strike(Location location);

    public void strikeEntity(Entity paramEntity);

    public void clear();

    public boolean isThunderingAllowed();

    public boolean isMoonVisible();

    public boolean isSun();

    public void setFogDistance(int fogDist);

    public int getFogDistance();

    public void update();

    public void update(Player player);

    public void setSnowing(boolean snow);

    public boolean isSnowing();

    void finish();
}