package sk.tomsik68.pw.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PWServerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().getDescription().getName().equals("Spout"))
			ProperWeather.instance().setupSpout(false, true);
		else
			ProperWeather.instance().setupPermissions();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().getDescription().getName().equals("Spout"))
			ProperWeather.instance().setupSpout(false, false);
		else
			ProperWeather.instance().setupPermissions();
	}
}