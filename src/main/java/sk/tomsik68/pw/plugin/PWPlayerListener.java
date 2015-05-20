package sk.tomsik68.pw.plugin;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.region.Region;

public class PWPlayerListener implements Listener {
	private final WeatherSystem weatherSystem;
	private final int PLAYER_MOVE_CHECK_DELAY = 3000;// ms
	private final PlayerRegionManager playerRegions;

	public PWPlayerListener(WeatherSystem weatherSystem1, PlayerRegionManager playerRegions) {
		weatherSystem = weatherSystem1;
		this.playerRegions = playerRegions;
	}

	public void onPlayerChangedRegion(Player player) {
		int region = getOrCreateMetaData(player, "pw.lastRegion", -1).asInt();
		if (weatherSystem.getRegionManager().getRegion(region) != null) {
			weatherSystem.getWeatherController(region).update(player);
			playerRegions.playerChangedRegion(player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setMetadata("pw.moveTimestamp", new FixedMetadataValue(ProperWeather.instance(), 0L));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(final PlayerMoveEvent event) {
		Player player = event.getPlayer();
		long lastCheck = getMetadata(player, "pw.moveTimestamp").asLong();
		// check if sufficient time has passed after last check
		if (System.currentTimeMillis() - lastCheck >= PLAYER_MOVE_CHECK_DELAY) {
			// check if player has metadata flag for lastRegion(PW can be
			// started/stopped in various regions during game, which is what
			// often leads to meta variable not being present...

			if (player.hasMetadata("pw.lastRegion")) {
				Region region = weatherSystem.getRegionManager().getRegionAt(event.getTo());
				// check if the region changed
				if (region != null && getMetadata(player, "pw.lastRegion").asInt() != region.getUID()) {
					// save the new region in metadata
					player.setMetadata("pw.lastRegion", new FixedMetadataValue(ProperWeather.instance(), region.getUID()));
					// call onPlayerChangedRegion
					onPlayerChangedRegion(player);
				} else if (region == null && getMetadata(player, "pw.lastRegion").asInt() != -1) {
					playerRegions.onQuit(player);
				}
			} else {
				Region region = weatherSystem.getRegionManager().getRegionAt(player.getLocation());
				if (region != null) {
					// save the new region in metadata
					player.setMetadata("pw.lastRegion", new FixedMetadataValue(ProperWeather.instance(), region.getUID()));
					// call onPlayerChangedRegion
					onPlayerChangedRegion(player);
				}
			}
			// player region check has happened, so save the time in millis
			player.setMetadata("pw.moveTimestamp", new FixedMetadataValue(ProperWeather.instance(), System.currentTimeMillis()));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		playerRegions.onQuit(player);

		if (player.hasMetadata("pw.lastRegion")) {
			int region = getMetadata(player, "pw.lastRegion").asInt();
			if (weatherSystem.getRegionManager().getRegion(region) != null) {
				weatherSystem.getWeatherController(region).playerLeft(player);
			}
			// remove all metadata
			player.removeMetadata("pw.lastRegion", ProperWeather.instance());
			player.removeMetadata("pw.moveTimestamp", ProperWeather.instance());
			player.removeMetadata("pw.raining", ProperWeather.instance());
		}
	}

	private MetadataValue getOrCreateMetaData(Player player, String key, Object defaultValue) {
		MetadataValue result = null;
		result = getMetadata(player, key);
		if (result == null) {
			result = new FixedMetadataValue(ProperWeather.instance(), defaultValue);
			player.setMetadata(key, result);
		}
		return result;
	}

	private MetadataValue getMetadata(Player player, String key) {
		MetadataValue result = null;
		List<MetadataValue> values = player.getMetadata(key);
		for (MetadataValue value : values) {
			if (value.getOwningPlugin().equals(ProperWeather.instance())) {
				result = value;
			}
		}
		return result;
	}
}