package sk.tomsik68.pw.plugin;

import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.region.Region;

public class RegionUpdateTask implements Runnable {
	private RegionManager regions;

	public RegionUpdateTask(RegionManager regionManager) {
		regions = regionManager;
	}

	public void run() {
		for (Region region : regions.getAllRegions()) {
			region.update();
		}
	}
}