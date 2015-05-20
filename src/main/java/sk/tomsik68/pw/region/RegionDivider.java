package sk.tomsik68.pw.region;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.World;

import sk.tomsik68.pw.api.RegionManager;

public abstract class RegionDivider {
	protected final RegionManager regionManager;

	public RegionDivider(RegionManager regionManager) {
		this.regionManager = regionManager;
		Validate.notNull(regionManager);
	}

	public abstract List<Region> divideToRegions(World world);
}
