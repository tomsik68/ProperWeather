package sk.tomsik68.pw.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * This class tracks players' position. It's updated from player listener and is
 * used in region.getPlayers
 * 
 * @author Tomsik68
 *
 */
public class PlayerRegionManager {
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

	void onQuit(Player player) {
		if (player.hasMetadata("pw.lastRegion")) {
			int region = player.getMetadata("pw.lastRegion").get(0).asInt();
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
