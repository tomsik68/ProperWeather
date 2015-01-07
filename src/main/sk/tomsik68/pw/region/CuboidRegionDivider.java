package sk.tomsik68.pw.region;

import java.util.List;

import org.bukkit.World;

import sk.tomsik68.pw.api.RegionManager;

public class CuboidRegionDivider extends RegionDivider {

	public CuboidRegionDivider(RegionManager regionManager) {
		super(regionManager);
	}

	@Override
	public List<Region> divideToRegions(World world) {
		return null;
	}

}
