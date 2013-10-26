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
package sk.tomsik68.pw.impl;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.plugin.ProjectileManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

//NOTBUKKIT
public class DefaultWeatherController implements WeatherController {
    protected final Region region;
    protected boolean thundersAllowed = true;
    protected boolean thundering = false;
    protected boolean rain = false;
    protected Set<BaseWeatherElement> elements = new HashSet<BaseWeatherElement>();

    public DefaultWeatherController(Region region1) {
        this.region = region1;
        if (region == null)
            throw new NullPointerException("I'll not construct with NULL region!");
    }

    public void setSkyColor(Color color) {
    }

    public Color getSkyColor() {
        return ProperWeather.defaultSkyColor;
    }

    public void setFogColor(Color color) {
    }

    public Color getFogColor() {
        return null;
    }

    public int getSunSize() {
        return 100;
    }

    public void setSunSize(int pcent) {
    }

    public void setStarFrequency(int pcent) {
    }

    public void setMoonSize(int pcent) {
    }

    public int getMoonSize() {
        return 100;
    }

    public int getStarFrequency() {
        return 35;
    }

    public void hideMoon() {
    }

    public void showMoon() {
    }

    public void hideSun() {
    }

    public void showSun() {
    }

    public void hideStars() {
    }

    public void showStars() {
    }

    public boolean isStars() {
        return true;
    }

    public boolean isClouds() {
        return true;
    }

    public void showClouds() {
    }

    public void hideClouds() {
    }

    public Color getCloudsColor() {
        return null;
    }

    public void setCloudsColor(Color color) {
    }

    public void setCloudsHeight(int h) {
    }

    public int getCloudsHeight() {
        return 100;
    }

    public void setRaining(boolean b) {
        this.rain = b;
        for (Player p : region.getPlayers()) {
            update(p);
        }
    }

    public boolean isRaining() {
        return this.rain;
    }

    public boolean isThundering() {
        return this.thundering;
    }

    public void setThundering(boolean b) {
        this.thundering = true;
    }

    public void strike(int x, int y, int z) {
        this.region.getWorld().strikeLightning(new Location(region.getWorld(), x, y, z));
    }

    public void strikeEntity(Entity entity) {
        this.region.getWorld().strikeLightning(entity.getLocation());
    }

    public void clear() {
        // FIXME Kill all fireballs
        ProjectileManager.killAll(Fireball.class);
        // DEBUG System.out.println("Weather Init...");
        setRaining(false);
        denyThundering();
        for (BaseWeatherElement elem : elements) {
            elem.deactivate(this);
        }
    }

    @Override
    public void finish() {
        for (Player p : region.getPlayers()) {
            Util.setRaining(p, isRaining());
            p.removeMetadata("pw.raining", ProperWeather.instance());
            p.removeMetadata("pw.moveTimestamp", ProperWeather.instance());
            p.removeMetadata("pw.lastRegion", ProperWeather.instance());
        }
        getRegion().getWorld();
        denyThundering();
        for (BaseWeatherElement elem : elements) {
            elem.deactivate(this);
        }
    }

    public boolean isThunderingAllowed() {
        return this.thundersAllowed;
    }

    public void allowThundering() {
        this.thundersAllowed = true;
    }

    public void denyThundering() {
        this.thundersAllowed = false;
    }

    public boolean isMoonVisible() {
        return true;
    }

    public boolean isSun() {
        return true;
    }

    public void setFogDistance(int d) {
    }

    public int getFogDistance() {
        return 0;
    }

    public Region getRegion() {
        return this.region;
    }

    public void update() {
        setRaining(this.rain);
    }

    public void update(Player p) {
        if (!p.hasMetadata("pw.raining") || (p.getMetadata("pw.raining").get(0).asBoolean() != isRaining())) {
            Util.setRaining(p, isRaining());
            p.setMetadata("pw.raining", new FixedMetadataValue(ProperWeather.instance(), isRaining()));
        }
    }

    @Override
    public void strike(Location location) {
        this.region.getWorld().strikeLightning(location);
    }

    @Override
    public Set<BaseWeatherElement> getActiveElements() {
        return elements;
    }

    @Override
    public void activateElement(BaseWeatherElement element) {
        element.activate(this);
        elements.add(element);
    }

    @Override
    public void deactivateElement(BaseWeatherElement element) {
        element.deactivate(this);
        elements.remove(element);
    }

    @Override
    public void setSnowing(boolean snow) {
        // sorry but this Controller is really stupid :/ check
        // SpoutWeatherController for something better >:)
        this.rain = snow;
    }

    @Override
    public boolean isSnowing() {
        return rain;
    }
}