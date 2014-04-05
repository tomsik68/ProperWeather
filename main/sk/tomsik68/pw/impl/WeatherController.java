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
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
                    WeatherStatusStructure playerWeather = (WeatherStatusStructure) value.value();
                    if (!playerWeather.equals(struct)) {
                        setValues(p);
                    }
                }
            }

        } else {
            setValues(p);
        }
    }

    private void setValues(Player p) {
        WeatherStatusStructure clone = struct.clone();
        backend.setRaining(p, isRaining());
        backend.setClouds(p, isClouds());
        backend.setCloudsColor(p, getCloudsColor());
        backend.setCloudsHeight(p, getCloudsHeight());
        backend.setFogColor(p, getFogColor());
        backend.setMoonSize(p, getMoonSize());
        backend.setSkyColor(p, getSkyColor());
        backend.setStarFrequency(p, getStarFrequency());
        backend.setSunSize(p, getSunSize());
        p.setMetadata("pw.weather", new FixedMetadataValue(ProperWeather.instance(), clone));
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
        ProjectileManager.killAll(Projectile.class);
        setRaining(false);
        setThundering(false);
        for (BaseWeatherElement elem : elements) {
            elem.deactivate(this);
        }
    }

    public void finish() {
        for (Player p : region.getPlayers()) {
            backend.setRaining(p, p.getWorld().hasStorm());

            p.removeMetadata("pw.weather", ProperWeather.instance());
            p.removeMetadata("pw.moveTimestamp", ProperWeather.instance());
            p.removeMetadata("pw.lastRegion", ProperWeather.instance());
        }
        getRegion().getWorld();
        setThundering(true);
        for (BaseWeatherElement elem : elements) {
            elem.deactivate(this);
        }
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

    public void setSkyColor(Color color) {
        struct.skyColor = color;
    }

    public Color getSkyColor() {
        return struct.skyColor;
    }

    public void setFogColor(Color color) {
        struct.fogColor = color;
    }

    public Color getFogColor() {
        return struct.fogColor;
    }

    public int getSunSize() {
        return struct.sunSize;
    }

    public void setSunSize(int pcent) {
        struct.sunSize = pcent;
    }

    public void setStarFrequency(int pcent) {
        struct.starFrequency = pcent;
    }

    public void setMoonSize(int pcent) {
        struct.moonSize = pcent;
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

    public Color getCloudsColor() {
        return struct.cloudsColor;
    }

    public void setCloudsColor(Color color) {
        struct.cloudsColor = color;
    }

    public void setCloudsHeight(int h) {
        struct.cloudsHeight = h;
    }

    public int getCloudsHeight() {
        return struct.cloudsHeight;
    }

    public void setRaining(boolean b) {
        struct.rain = b;
    }

    public boolean isRaining() {
        return struct.rain;
    }

    public void setThundering(boolean b) {
        struct.thundersAllowed = b;
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
    }

    public boolean isSnowing() {
        return struct.snowing;
    }

    public void setMoon(boolean moon) {
        struct.moonVisible = moon;
    }

    public void setSun(boolean sun) {
        struct.sunVisible = sun;
    }

    public void setStars(boolean stars) {
        struct.starsVisible = stars;
    }

    public void setClouds(boolean c) {
        struct.cloudsVisible = c;
    }
}