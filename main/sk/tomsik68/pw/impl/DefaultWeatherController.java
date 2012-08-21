package sk.tomsik68.pw.impl;

import java.awt.Color;
import net.minecraft.server.Packet70Bed;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

public class DefaultWeatherController implements WeatherController {
    protected final Region region;
    protected boolean thundersAllowed;
    protected boolean thundering;
    protected boolean rain;

    public DefaultWeatherController(Region region1) {
        this.region = region1;
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
        for (Player p : this.region.getPlayers()) {
            CraftPlayer player = (CraftPlayer) p;
            player.getHandle().netServerHandler.sendPacket(new Packet70Bed(this.rain ? 1 : 2, 0));
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
        this.region.getWorld().strikeLightning(new Location(region.getWorld(),x,y,z));
    }

    public void strikeEntity(Entity entity) {
        this.region.getWorld().strikeLightning(entity.getLocation());
    }

    public void clear() {
        setRaining(false);
        denyThundering();
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
        CraftPlayer player = (CraftPlayer) p;
        player.getHandle().netServerHandler.sendPacket(new Packet70Bed(isRaining() ? 1 : 2, 0));
    }

    @Override
    public void strike(Location location) {
        this.region.getWorld().strikeLightning(location);
    }
}