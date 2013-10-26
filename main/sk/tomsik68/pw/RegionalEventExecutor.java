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
package sk.tomsik68.pw;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.EventExecutor;

import sk.tomsik68.pw.api.BaseWeatherElement;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

public class RegionalEventExecutor implements EventExecutor {
    private Method m;
    private final Region region;

    public RegionalEventExecutor(BaseWeatherElement elem, Class<?> eventClass) {
        Method[] methods = elem.getClass().getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 1 && method.isAnnotationPresent(EventHandler.class) && method.getParameterTypes()[0] == eventClass) {
                m = method;
                break;
            }
        }
        if (m == null)
            throw new NullPointerException("[ProperWeather-RegionalEventExecutor] No suitable method found for '" + eventClass.getName() + "' in '" + elem.getClass().getName() + "'. Make sure the method's first parameter is '" + eventClass.getName() + "' and annotation '" + EventHandler.class.getName() + "' is present.");
        region = elem.getRegion();
    }

    @Override
    public void execute(Listener arg0, Event event) throws EventException {
        if (event instanceof WorldEvent) {
            if (!((WorldEvent) event).getWorld().getUID().equals(region.getWorldId()))
                return;
        }
        if (event instanceof BlockEvent) {
            if (!region.contains(((BlockEvent) event).getBlock().getLocation()))
                return;
        }
        if (event instanceof EntityEvent) {
            if (!region.contains(((EntityEvent) event).getEntity().getLocation()))
                return;
        }
        if (event instanceof PlayerEvent) {
            if (!region.contains(((PlayerEvent) event).getPlayer().getLocation()))
                return;
        }
        if (event instanceof VehicleEvent) {
            if (!region.contains(((VehicleEvent) event).getVehicle().getLocation()))
                return;
        }
        if (event instanceof WeatherEvent) {
            return;
        }
        if (event instanceof InventoryEvent) {
            boolean result = false;
            List<HumanEntity> entities = ((InventoryEvent) event).getViewers();
            for (HumanEntity entity : entities) {
                if (region.contains(entity.getLocation())) {
                    result = true;
                    break;
                }
            }
            if(!result)
                return;
        }
        try {
            m.invoke(arg0, event);
        } catch (Exception e) {
            ProperWeather.log.severe("(RegionalEventExecutor) Method invocation error for :'" + arg0.getClass().getName() + "'");
            e.printStackTrace();
        }
    }

}
