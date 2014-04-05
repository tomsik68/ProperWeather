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
package sk.tomsik68.pw.api;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import sk.tomsik68.pw.RegionalEventExecutor;
import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
/** Represents a weather element like wind. It'll soon be extended to plugins.
 * 
 * @author Tomsik68
 *
 */
public abstract class BaseWeatherElement implements Listener {
    protected Region region;
    /**
     * Don't activate your WeatherElement. It'll be activated later.
     * 
     * @param wc
     */
    public BaseWeatherElement(WeatherController wc) {
        region = wc.getRegion();
    }
    /** [Called by plugin]
     * 
     * @param controller
     */
    public abstract void activate(WeatherController controller);
    /** [Called by plugin]
     * 
     * @param controller
     */
    public abstract void deactivate(WeatherController controller);

    public Region getRegion() {
        return region;
    }

    protected final void registerEvent(Class<? extends Event> eventClass, EventPriority priority) {
        try {
            HandlerList handlerList = (HandlerList) eventClass.getMethod("getHandlerList").invoke(null);
            handlerList.register(new RegisteredListener(this, new RegionalEventExecutor(this, eventClass), priority, ProperWeather.instance(), false));
        } catch (Exception e) {
            System.out.println("[ProperWeather-" + getClass().getName() + "] Invalid event '" + eventClass.getName() + "'");
        }
    }
    protected final void unregisterAll() {
        //thanks bukkit for this :) saved me a lot of work
        HandlerList.unregisterAll(this);
    }
    public final IWeatherData getSituation(){
        return region.getWeatherData();
    }
    public final Weather getCurrentWeather(){
        return getSituation().getCurrentWeather();
    }

}
