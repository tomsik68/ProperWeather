package sk.tomsik68.pw.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

/**
 * This class tracks players' position. It's updated from player listener and is
 * used in region.getPlayers
 * 
 * @author Tomsik68
 *
 */
public final class PlayerRegionManager {
	private HashMap<Integer, Set<UUID>> playersInRegion = new HashMap<>();
	private HashMap<UUID, Integer> playerRegions = new HashMap<>();

	public PlayerRegionManager() {
	}

	private void addPlayerToRegion(int region, Player player) {
		Set<UUID> set = playersInRegion.get(region);
		if (set == null)
			set = new HashSet<UUID>();
		set.add(player.getUniqueId());
		playersInRegion.put(region, set);
		playerRegions.put(player.getUniqueId(), region);
	}

	private void removePlayerFromRegion(int prevRegion, Player player) {
		Set<UUID> set = playersInRegion.get(prevRegion);
		if (set == null)
			set = new HashSet<UUID>();
		else {
			set.remove(player.getUniqueId());
		}
		playersInRegion.put(prevRegion, set);
		playerRegions.remove(player.getUniqueId());
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
	
	void onQuit(Player player) {
		if (player.hasMetadata("pw.lastRegion")) {
			int region = getMetadata(player, "pw.lastRegion").asInt();
			removePlayerFromRegion(region, player);
		}
	}

	void playerChangedRegion(Player player) {
		if (!playerRegions.containsKey(player.getUniqueId())) {
			// save the last region information to hashmap
			playerRegions.put(player.getUniqueId(), player.getMetadata("pw.lastRegion").get(0).asInt());
		}
		int nowRegion = player.getMetadata("pw.lastRegion").get(0).asInt();
		int prevRegion = playerRegions.get(player.getUniqueId());
		if (nowRegion != prevRegion) {
			removePlayerFromRegion(prevRegion, player);
			addPlayerToRegion(nowRegion, player);
		}
	}

	public Iterable<UUID> getPlayers(int region) {
		return playersInRegion.get(region);
	}

}
