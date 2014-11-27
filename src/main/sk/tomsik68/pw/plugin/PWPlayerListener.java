package sk.tomsik68.pw.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.region.Region;

public class PWPlayerListener implements Listener {
	private final WeatherSystem weatherSystem;
	private final int PLAYER_MOVE_CHECK_DELAY = 3000;// ms
	private final PlayerRegionManager playerRegions;

	public PWPlayerListener(WeatherSystem weatherSystem1,
			PlayerRegionManager playerRegions) {
		weatherSystem = weatherSystem1;
		this.playerRegions = playerRegions;
	}

	public void onPlayerChangedRegion(Player player) {
		int region = player.getMetadata("pw.lastRegion").get(0).asInt();
		weatherSystem.getWeatherController(region).update(player);
		playerRegions.playerChangedRegion(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (weatherSystem.isHooked(event.getPlayer().getWorld())) {
			onPlayerChangedRegion(event.getPlayer());
			event.getPlayer().setMetadata("pw.moveTimestamp",
					new FixedMetadataValue(ProperWeather.instance(), 0L));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(final PlayerMoveEvent event) {
		if (weatherSystem.isHooked(event.getTo().getWorld())) {
			// getOrCreate movement timestamp
			if (!event.getPlayer().hasMetadata("pw.moveTimestamp")) {
				event.getPlayer().setMetadata("pw.moveTimestamp",
						new FixedMetadataValue(ProperWeather.instance(), 0L));
			}
			long lastCheck = event.getPlayer().getMetadata("pw.moveTimestamp")
					.get(0).asLong();
			// check if sufficient time has passed after last check
			if (System.currentTimeMillis() - lastCheck >= PLAYER_MOVE_CHECK_DELAY) {
				Region region = weatherSystem.getRegionManager().getRegion(
						event.getPlayer().getMetadata("pw.lastRegion").get(0)
								.asInt());
				// check if player changed their region
				if (!region.contains(event.getFrom())
						|| !event.getPlayer().hasMetadata("pw.lastRegion")
						|| event.getPlayer().getMetadata("pw.lastRegion")
								.get(0).asInt() != region.getUID()) {
					// save the new region in metadata
					event.getPlayer().setMetadata(
							"pw.lastRegion",
							new FixedMetadataValue(ProperWeather.instance(),
									region.getUID()));
					// call onPlayerChangedRegion
					onPlayerChangedRegion(event.getPlayer());
				}
				// player region check has happened, so save the time in millis
				event.getPlayer().setMetadata(
						"pw.moveTimestamp",
						new FixedMetadataValue(ProperWeather.instance(), System
								.currentTimeMillis()));
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		playerRegions.onQuit(event.getPlayer());
		// remove all metadata
		event.getPlayer().removeMetadata("pw.lastRegion",
				ProperWeather.instance());
		event.getPlayer().removeMetadata("pw.moveTimestamp",
				ProperWeather.instance());
		event.getPlayer()
				.removeMetadata("pw.raining", ProperWeather.instance());
	}
}