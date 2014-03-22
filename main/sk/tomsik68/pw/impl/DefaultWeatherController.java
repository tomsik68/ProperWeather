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
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.plugin.ProjectileManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

public class DefaultWeatherController implements WeatherController {
    private java.awt.Color sky;
    private java.awt.Color clouds;
    private java.awt.Color fog;
    private int sunSize;
    private int moonSize;
    private int starFrequency;
    private boolean moon;
    private boolean cloudsVisible;
    private boolean sun;
    private boolean stars;
    private int cloudsHeight;
    private boolean snowing;
    protected final Region region;
    protected boolean thundersAllowed = true;
    protected boolean thundering = false;
    protected boolean rain = false;
    protected Set<BaseWeatherElement> elements = new HashSet<BaseWeatherElement>();
    protected final IServerBackend backend;

    public DefaultWeatherController(Region region1, IServerBackend backend) {
        this.region = region1;
        if (region == null)
            throw new NullPointerException("I'll not construct with NULL region!");
        this.backend = backend;
        sky = ProperWeather.defaultSkyColor;
        clouds = (fog = java.awt.Color.white);
        sunSize = 100;
        moonSize = 100;
        starFrequency = 35;
        moon = true;
        cloudsVisible = true;
        sun = true;
        stars = true;
        cloudsHeight = 110;
    }

    public Region getRegion() {
        return this.region;
    }

    public void update() {
        setRaining(isRaining());
    }

    public void update(Player p) {
        if (!p.hasMetadata("pw.raining") || (p.getMetadata("pw.raining").get(0).asBoolean() ^ rain)) {
            backend.setRaining(p, rain);
            p.setMetadata("pw.raining", new FixedMetadataValue(ProperWeather.instance(), rain));
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

    @Override
    public void finish() {
        for (Player p : region.getPlayers()) {
            backend.setRaining(p, rain);
            p.removeMetadata("pw.raining", ProperWeather.instance());
            p.removeMetadata("pw.moveTimestamp", ProperWeather.instance());
            p.removeMetadata("pw.lastRegion", ProperWeather.instance());
        }
        getRegion().getWorld();
        setThundering(false);
        for (BaseWeatherElement elem : elements) {
            elem.deactivate(this);
        }
    }

    // rest of the methods under here are getters/setters for each individual
    // variable
    // the setters are special, because they're also responsible for calling
    // update on each player in region.

    public void setSkyColor(Color color) {
        sky = color;
        // TODO
    }

    public Color getSkyColor() {
        return sky;
    }

    public void setFogColor(Color color) {
        this.fog = color;
        // TODO
    }

    public Color getFogColor() {
        return fog;
    }

    public int getSunSize() {
        return sunSize;
    }

    public void setSunSize(int pcent) {
        sunSize = pcent;
        // TODO
    }

    public void setStarFrequency(int pcent) {
        starFrequency = pcent;
        // TODO
    }

    public void setMoonSize(int pcent) {
        moonSize = pcent;
        // TODO
    }

    public int getMoonSize() {
        return moonSize;
    }

    public int getStarFrequency() {
        return starFrequency;
    }

    public boolean isStars() {
        return stars;
    }

    public boolean isClouds() {
        return cloudsVisible;
    }

    public Color getCloudsColor() {
        return clouds;
    }

    public void setCloudsColor(Color color) {
        this.clouds = color;
        // TODO
    }

    public void setCloudsHeight(int h) {
        cloudsHeight = h;
        // TODO
    }

    public int getCloudsHeight() {
        return cloudsHeight;
    }

    public void setRaining(boolean b) {
        this.rain = b;
        // TODO
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
        // TODO
    }

    public boolean isThunderingAllowed() {
        return this.thundersAllowed;
    }

    public boolean isMoonVisible() {
        return moon;
    }

    public boolean isSun() {
        return sun;
    }

    public void setFogDistance(int d) {

    }

    public int getFogDistance() {
        return 0;
    }

    @Override
    public void setSnowing(boolean snow) {
        this.snowing = snow;
    }

    @Override
    public boolean isSnowing() {
        return snowing;
    }

    @Override
    public void setMoon(boolean moon) {
        this.moon = moon;
        // TODO
    }

    @Override
    public void setSun(boolean sun) {
        this.sun = sun;
        // TODO

    }

    @Override
    public void setStars(boolean stars) {
        this.stars = stars;
        // TODO
    }

    @Override
    public void setClouds(boolean c) {
        this.cloudsVisible = c;
        // TODO
    }

}