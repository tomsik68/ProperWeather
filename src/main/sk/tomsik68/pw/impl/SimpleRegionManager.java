package sk.tomsik68.pw.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.files.impl.regions.RegionSaveStructure;
import sk.tomsik68.pw.files.impl.regions.RegionsDataFile;
import sk.tomsik68.pw.plugin.PlayerRegionManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.BiomeRegionDivider;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.region.RegionDivider;
import sk.tomsik68.pw.region.RegionType;
import sk.tomsik68.pw.region.WorldRegionDivider;

public class SimpleRegionManager implements RegionManager {
	private final Map<Integer, Region> regions = new HashMap<Integer, Region>();
	private final Map<UUID, Integer[]> worldRegions = new HashMap<UUID, Integer[]>();
	private static final Map<RegionType, RegionDivider> dividers = new HashMap<RegionType, RegionDivider>();
	private static final Integer[] EMPTY_INT_ARRAY = new Integer[0];
	private RegionsDataFile dataFile;
	private final PlayerRegionManager playerRegionManager;
	private static int lastRegionID = -1;

	public SimpleRegionManager() {
		if (dividers.isEmpty()) {
			addDividers();
		}
		playerRegionManager = new PlayerRegionManager();
	}

	private void addDividers() {
		dividers.put(RegionType.WORLD, new WorldRegionDivider(this));
		dividers.put(RegionType.BIOME, new BiomeRegionDivider(this));
		// dividers.put(RegionType.CUBOID, new CuboidRegionDivider());
	}

	public Region getRegion(Integer id) {
		return regions.get(id);
	}

	public int addRegion(Region region) {
		int r = region.getUID();
		if (r == -1) {
			r = lastRegionID++;

			while (regions.containsKey(Integer.valueOf(r))) {
				r++;
			}
			lastRegionID = r;
			region.setUID(r);
		} else
			lastRegionID = Math.max(r, lastRegionID);
		region.setRegionManager(this);
		regions.put(Integer.valueOf(r), region);
		if (!worldRegions.containsKey(region.getWorldId())) {
			worldRegions.put(region.getWorldId(), new Integer[] { Integer.valueOf(r) });
		} else {
			Integer[] array = worldRegions.get(region.getWorldId());
			Integer[] array2 = new Integer[array.length + 1];
			System.arraycopy(array, 0, array2, 0, array.length);
			array2[array.length] = Integer.valueOf(r);
			worldRegions.put(region.getWorldId(), array2);
		}
		return r;
	}

	public Region getRegionAt(Location location) {
		if (!worldRegions.containsKey(location.getWorld().getUID()))
			return null;
		List<Region> check = new ArrayList<Region>();

		Integer[] regionIds = worldRegions.get(location.getWorld().getUID());
		for (Integer i : regionIds) {
			check.add(regions.get(i));
		}
		for (Region region : check) {
			if (region.contains(location)) {
				return region;
			}
		}
		return null;
	}

	public void unHook(World world) {
		Integer[] toRemove = worldRegions.get(world.getUID());
		Integer[] arrayOfInteger1;
		int j = (arrayOfInteger1 = toRemove).length;
		for (int i = 0; i < j; i++) {
			int r = arrayOfInteger1[i].intValue();
			Region region = getRegion(r);
			if (region.getWorld().getUID().equals(world.getUID())) {
				regions.remove(region.getUID());
			}
		}
		worldRegions.remove(world.getUID());
	}

	public void hook(World world, RegionType regionType) {
		List<Integer> ids = new ArrayList<Integer>();

		RegionDivider div = dividers.get(regionType);
		List<Region> regions = div.divideToRegions(world);
		for (Region region : regions) {
			ids.add(addRegion(region));
		}
	}

	public List<Integer> getRegions(World world) {
		if (worldRegions.containsKey(world.getUID())) {
			return Arrays.asList(worldRegions.get(world.getUID()));
		}
		return null;
	}

	public boolean isHooked(World world) {
		return worldRegions.containsKey(world.getUID());
	}

	public List<String> getWorlds() {
		List<String> result = new ArrayList<String>();
		for (UUID uid : worldRegions.keySet()) {
			result.add(Bukkit.getWorld(uid).getName());
		}
		return result;
	}

	public void saveRegions() {
		ProperWeather.log.fine("Saving region data...");
		try {
			RegionSaveStructure save = new RegionSaveStructure(new ArrayList<Region>(regions.values()));
			dataFile.saveData(save);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadRegions() {
		ProperWeather.log.fine("Loading region data(automatically dropping broken regions)...");
		dataFile = new RegionsDataFile(new File(ProperWeather.instance().getDataFolder(), "regions.dat"));
		regions.clear();
		try {
			ArrayList<Region> regs = dataFile.loadData().getRegions();
			HashMap<UUID, ArrayList<Integer>> worldregs = new HashMap<UUID, ArrayList<Integer>>();
			for (Region region : regs) {
				if (region.getWorldId() == null)
					continue;

				regions.put(region.getUID(), region);
				if (!worldregs.containsKey(region.getWorldId())) {
					worldregs.put(region.getWorldId(), new ArrayList<Integer>());
				}
				ArrayList<Integer> worldRegionss = worldregs.get(region.getWorldId());
				worldRegionss.add(region.getUID());
				worldregs.put(region.getWorldId(), worldRegionss);
				lastRegionID = region.getUID();
				region.setRegionManager(this);

				// addRegion(region);
			}
			for (Entry<UUID, ArrayList<Integer>> entry : worldregs.entrySet()) {
				worldRegions.put(entry.getKey(), entry.getValue().toArray(EMPTY_INT_ARRAY));
			}
			if (regions.size() == 0)
				lastRegionID = 0;
			ProperWeather.log.finer("Dropped regions: " + (regs.size() - regions.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<Region> getAllRegions() {
		return regions.values();
	}

	@Override
	public List<Integer> getRegions(UUID worldId) {
		if (worldRegions.containsKey(worldId)) {
			return Arrays.asList(worldRegions.get(worldId));
		}
		return null;
	}

	public PlayerRegionManager getPlayerRegionManager() {
		return playerRegionManager;
	}

	@Override
	public Iterable<Player> getPlayers(int r) {
		Iterable<UUID> playerUUIDs = playerRegionManager.getPlayers(r);
		HashSet<Player> players = new HashSet<Player>();
		if (playerUUIDs != null) {
			for (UUID playerID : playerUUIDs) {
				players.add(Bukkit.getPlayer(playerID));
			}
		}
		return players;
	}
}