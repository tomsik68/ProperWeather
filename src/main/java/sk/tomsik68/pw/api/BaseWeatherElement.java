package sk.tomsik68.pw.api;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

/**
 * Represents a weather element like wind. It'll soon be extended to plugins.
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

	/**
	 * [Called by plugin]
	 * 
	 * @param controller
	 */
	public abstract void activate(WeatherController controller);

	/**
	 * [Called by plugin]
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
		// thanks bukkit for this :) saved me a lot of work
		HandlerList.unregisterAll(this);
	}

	public final IWeatherData getSituation() {
		return region.getWeatherData();
	}

	public final Weather getCurrentWeather() {
		return getSituation().getCurrentWeather();
	}

}
