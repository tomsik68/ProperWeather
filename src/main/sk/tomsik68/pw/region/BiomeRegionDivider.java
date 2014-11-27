package sk.tomsik68.pw.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Biome;

import sk.tomsik68.pw.api.RegionManager;

public class BiomeRegionDivider extends RegionDivider {

    public BiomeRegionDivider(RegionManager regionManager) {
	super(regionManager);
    }

    @Override
    public List<Region> divideToRegions(World world) {
	List<Region> result = new ArrayList<Region>();
	for (Biome biome : Biome.values()) {
	    BiomeRegion region = new BiomeRegion(world.getUID(), biome);
	    region.setRegionManager(regionManager);
	    result.add(region);
	}
	return result;
    }
}
