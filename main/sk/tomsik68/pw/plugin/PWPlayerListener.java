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
package sk.tomsik68.pw.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.impl.WeatherController;

public class PWPlayerListener implements Listener {
    private final WeatherSystem weatherSystem;
    private final int PLAYER_MOVE_CHECK_DELAY = 3000;

    public PWPlayerListener(WeatherSystem weatherSystem1) {
        weatherSystem = weatherSystem1;
    }

    public void onPlayerChangedRegion(PlayerChangedWorldEvent event) {
        weatherSystem.getWeatherController(weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation())).update(event.getPlayer());
        /*if (false) {
            SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
            if (!player.isSpoutCraftEnabled())
                return;
            if ((weatherSystem.isHooked(event.getFrom())) && (event.getPlayer() != null) && (!weatherSystem.isHooked(event.getPlayer().getWorld())) && (player.isSpoutCraftEnabled())) {
                SkyManager sm = SpoutManager.getSkyManager();
                sm.setCloudColor(player, new org.getspout.spoutapi.gui.Color(255, 255, 255));
                sm.setFogColor(player, new org.getspout.spoutapi.gui.Color(255, 255, 255));
                sm.setSunSizePercent(player, 100);
                sm.setMoonSizePercent(player, 100);
                sm.setSkyColor(player, new org.getspout.spoutapi.gui.Color(ProperWeather.defaultSkyColor.getRed(), ProperWeather.defaultSkyColor.getGreen(), ProperWeather.defaultSkyColor.getBlue()));
                sm.setMoonVisible(player, true);
                sm.setSunVisible(player, true);
                sm.setCloudsVisible(player, true);
            }

            if ((event.getPlayer() != null) && (weatherSystem.isHooked(event.getPlayer().getWorld())) && (player.isSpoutCraftEnabled())) {
                WeatherController wc = weatherSystem.getWeatherController(weatherSystem.getRegionManager().getRegionAt(player.getLocation()));
                wc.update(event.getPlayer());
                if (!ProperWeather.isSpout)
                    return;
                SkyManager sm = SpoutManager.getSkyManager();
                sm.setCloudColor(player, new org.getspout.spoutapi.gui.Color(wc.getCloudsColor().getRed(), wc.getCloudsColor().getGreen(), wc.getCloudsColor().getBlue()));
                sm.setFogColor(player, new org.getspout.spoutapi.gui.Color(wc.getFogColor().getRed(), wc.getFogColor().getGreen(), wc.getFogColor().getBlue()));
                sm.setSunSizePercent(player, wc.getSunSize());
                sm.setMoonSizePercent(player, wc.getMoonSize());
                sm.setSkyColor(player, new org.getspout.spoutapi.gui.Color(wc.getSkyColor().getRed(), wc.getSkyColor().getGreen(), wc.getSkyColor().getBlue()));
                sm.setMoonVisible(player, wc.isMoonVisible());
                sm.setSunVisible(player, wc.isSun());
                sm.setCloudsVisible(player, wc.isClouds());
            }
        }*/
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (weatherSystem.isHooked(event.getPlayer().getWorld())) {
            WeatherController c = weatherSystem.getWeatherController(weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation()));
            c.update(event.getPlayer());
            event.getPlayer().setMetadata("pw.moveTimestamp", new FixedMetadataValue(ProperWeather.instance(), 0L));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!weatherSystem.isHooked(event.getTo().getWorld()))
            return;
        if (!event.getPlayer().hasMetadata("pw.moveTimestamp")) {
            event.getPlayer().setMetadata("pw.moveTimestamp", new FixedMetadataValue(ProperWeather.instance(), 0L));
        }
        long lastCheck = event.getPlayer().getMetadata("pw.moveTimestamp").get(0).asLong();
        if (System.currentTimeMillis() - lastCheck >= PLAYER_MOVE_CHECK_DELAY) {
            Runnable runn = new Runnable() {
                public void run() {
                    if (!weatherSystem.getRegionManager().getRegionAt(event.getFrom()).contains(event.getTo()) || !event.getPlayer().hasMetadata("pw.lastRegion") || event.getPlayer().getMetadata("pw.lastRegion").get(0).asInt() != weatherSystem.getRegionManager().getRegionAt(event.getTo()).getUID()) {
                        onPlayerChangedRegion(new PlayerChangedWorldEvent(event.getPlayer(), event.getFrom().getWorld()));
                    }
                    event.getPlayer().setMetadata("pw.moveTimestamp", new FixedMetadataValue(ProperWeather.instance(), System.currentTimeMillis()));
                    event.getPlayer().setMetadata("pw.lastRegion", new FixedMetadataValue(ProperWeather.instance(),weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation()).getUID()));
                }
            };
            Thread thread = new Thread(runn);
            thread.start();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata("pw.lastRegion", ProperWeather.instance());
        event.getPlayer().removeMetadata("pw.moveTimestamp", ProperWeather.instance());
        event.getPlayer().removeMetadata("pw.raining", ProperWeather.instance());
    }
}