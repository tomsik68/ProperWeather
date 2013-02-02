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