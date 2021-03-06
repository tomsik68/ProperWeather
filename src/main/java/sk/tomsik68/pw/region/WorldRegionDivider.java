package sk.tomsik68.pw.region;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

import sk.tomsik68.pw.api.RegionManager;

public final class WorldRegionDivider extends RegionDivider {

	public WorldRegionDivider(RegionManager simpleRegionManager) {
		super(simpleRegionManager);
	}

	@Override
	public List<Region> divideToRegions(World world) {
		WorldRegion region = new WorldRegion(world.getUID());
		region.setRegionManager(regionManager);
		return Arrays.asList((Region) region);
	}
}
