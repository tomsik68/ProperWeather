package sk.tomsik68.pw.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import sk.tomsik68.pw.api.WeatherSystem;

public class PWWeatherListener implements Listener {
    private final WeatherSystem weatherSystem;

    public PWWeatherListener(WeatherSystem ws) {
        this.weatherSystem = ws;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent event) {
        if(event.getWorld() == null)
            return;
        if (this.weatherSystem.isHooked(event.getWorld())) {
            if (event.getWorld().hasStorm()){
                event.getWorld().setStorm(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLightningStrike(LightningStrikeEvent event) {
        if(weatherSystem.isHooked(event.getWorld()))
            event.setCancelled(!weatherSystem.canNowBeLightning(weatherSystem.getRegionManager().getRegionAt(event.getLightning().getLocation())));
    }
}