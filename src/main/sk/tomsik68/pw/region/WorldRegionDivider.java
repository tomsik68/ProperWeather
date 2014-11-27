package sk.tomsik68.pw.region;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

import sk.tomsik68.pw.api.RegionManager;

public class WorldRegionDivider extends RegionDivider {

    public WorldRegionDivider(RegionManager simpleRegionManager) {
	super(simpleRegionManager);
    }

    @Override
    public List<Region> divideToRegions(World world) {
	return Arrays.asList((Region) new WorldRegion(world.getUID()));
    }

}
