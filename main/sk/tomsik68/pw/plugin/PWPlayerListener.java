package sk.tomsik68.pw.plugin;

import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SkyManager;
import org.getspout.spoutapi.player.SpoutPlayer;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherSystem;

public class PWPlayerListener implements Listener {
    private final WeatherSystem weatherSystem;
    static final HashMap<Integer, Long> playerMoveTimestamps = new HashMap<Integer, Long>();
    static final HashMap<Integer, Integer> lastPlayerRegion = new HashMap<Integer, Integer>();
    private final int PLAYER_MOVE_CHECK_DELAY = 3000;

    public PWPlayerListener(WeatherSystem weatherSystem1) {
        this.weatherSystem = weatherSystem1;
    }

    public void onPlayerChangedRegion(PlayerChangedWorldEvent event) {
        this.weatherSystem.getWeatherController(this.weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation())).update(event.getPlayer());
        if (ProperWeather.isSpout) {
            SpoutPlayer player = SpoutManager.getPlayer(event.getPlayer());
            if (!player.isSpoutCraftEnabled())
                return;
            if ((this.weatherSystem.isHooked(event.getFrom())) && (event.getPlayer() != null) && (!this.weatherSystem.isHooked(event.getPlayer().getWorld())) && (player.isSpoutCraftEnabled())) {
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

            if ((event.getPlayer() != null) && (this.weatherSystem.isHooked(event.getPlayer().getWorld())) && (player.isSpoutCraftEnabled())) {
                WeatherController wc = this.weatherSystem.getWeatherController(this.weatherSystem.getRegionManager().getRegionAt(player.getLocation()));
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
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.weatherSystem.isHooked(event.getPlayer().getWorld())) {
            WeatherController c = this.weatherSystem.getWeatherController(this.weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation()));
            c.update(event.getPlayer());
            PWPlayerListener.playerMoveTimestamps.put(Integer.valueOf(event.getPlayer().getEntityId()), Long.valueOf(0L));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!this.weatherSystem.isHooked(event.getTo().getWorld()))
            return;
        if (!PWPlayerListener.playerMoveTimestamps.containsKey(Integer.valueOf(event.getPlayer().getEntityId()))) {
            PWPlayerListener.playerMoveTimestamps.put(Integer.valueOf(event.getPlayer().getEntityId()), Long.valueOf(0L));
        }
        long lastCheck = ((Long) PWPlayerListener.playerMoveTimestamps.get(Integer.valueOf(event.getPlayer().getEntityId()))).longValue();
        if (System.currentTimeMillis() - lastCheck >= PLAYER_MOVE_CHECK_DELAY) {
            Runnable runn = new Runnable() {
                public void run() {
                    if ((!weatherSystem.getRegionManager().getRegionAt(event.getFrom()).contains(event.getTo())) || (!PWPlayerListener.lastPlayerRegion.containsKey(Integer.valueOf(event.getPlayer().getEntityId()))) || (((Integer) PWPlayerListener.lastPlayerRegion.get(Integer.valueOf(event.getPlayer().getEntityId()))).intValue() != weatherSystem.getRegionManager().getRegionAt(event.getTo()).getUID())) {
                        onPlayerChangedRegion(new PlayerChangedWorldEvent(event.getPlayer(), event.getFrom().getWorld()));
                    }
                    synchronized (PWPlayerListener.playerMoveTimestamps) {
                        PWPlayerListener.playerMoveTimestamps.put(Integer.valueOf(event.getPlayer().getEntityId()), Long.valueOf(System.currentTimeMillis()));
                        PWPlayerListener.lastPlayerRegion.put(Integer.valueOf(event.getPlayer().getEntityId()), Integer.valueOf(weatherSystem.getRegionManager().getRegionAt(event.getPlayer().getLocation()).getUID()));
                    }
                }
            };
            Thread thread = new Thread(runn);
            thread.start();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        synchronized (PWPlayerListener.playerMoveTimestamps) {
            PWPlayerListener.playerMoveTimestamps.remove(Integer.valueOf(event.getPlayer().getEntityId()));
            PWPlayerListener.lastPlayerRegion.remove(Integer.valueOf(event.getPlayer().getEntityId()));
        }
    }
}