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
package sk.tomsik68.pw.weather.element;

import java.awt.geom.Point2D;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.api.WeatherController;

public class Wind extends BaseWeatherElement {
    private final Point2D windSpeed;
    private final int minLayer;
    private static final Random rand = new Random();

    public Wind(WeatherController wc) {
        super(wc);
        windSpeed = new Point2D.Double(rand.nextDouble() * 0.25 - 0.125, rand.nextDouble() * 0.25 - 0.125);
        minLayer = 50 + rand.nextInt(20);
    }

    @Override
    public void activate(WeatherController controller) {
        registerEvent(PlayerMoveEvent.class, EventPriority.NORMAL);
    }

    @Override
    public void deactivate(WeatherController controller) {
        unregisterAll();
    }

    @Override
    public void setExtendedFeaturesOn(boolean b) {
        // this one does nothing with spout, so ignore this!
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY() < minLayer || Math.abs(event.getPlayer().getVelocity().getX()) > 1.85d || Math.abs(event.getPlayer().getVelocity().getZ()) > 1.85d || event.getFrom().distanceSquared(event.getTo()) < 0.05d)
            return;
        event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(windSpeed.getX(), 0d, windSpeed.getY())));
    }

}
