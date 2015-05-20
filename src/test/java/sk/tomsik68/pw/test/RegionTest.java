package sk.tomsik68.pw.test;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.bukkit.block.Biome;
import org.junit.Test;

import sk.tomsik68.pw.impl.SimpleRegionManager;
import sk.tomsik68.pw.region.BiomeRegion;
import sk.tomsik68.pw.region.WorldRegion;

public class RegionTest {

	@Test
	public void test() {
		SimpleRegionManager regionManager = new SimpleRegionManager();
		UUID world1 = UUID.randomUUID();
		UUID world2 = UUID.randomUUID();
		regionManager.addRegion(new WorldRegion(world1));
		regionManager.addRegion(new WorldRegion(world2));
		regionManager.addRegion(new WorldRegion(world2));
		regionManager.addRegion(new BiomeRegion(world1, Biome.BEACH));
		regionManager.addRegion(new BiomeRegion(world1, Biome.DESERT));
		regionManager.addRegion(new BiomeRegion(world2, Biome.DESERT));
		regionManager.addRegion(new WorldRegion(world2));
		regionManager.addRegion(new WorldRegion(world2));
		assertEquals(8, regionManager.getAllRegions().size());
		assertEquals(3, regionManager.getRegions(world1).size());
		assertEquals(5, regionManager.getRegions(world2).size());
	}

}
