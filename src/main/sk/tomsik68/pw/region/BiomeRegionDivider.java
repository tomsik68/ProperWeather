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
	    result.add(new BiomeRegion(world.getUID(), regionManager, biome));
	}
	return result;
    }
}
