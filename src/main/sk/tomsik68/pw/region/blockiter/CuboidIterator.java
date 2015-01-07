package sk.tomsik68.pw.region.blockiter;

import java.util.Iterator;

import org.bukkit.World;
import org.bukkit.block.Block;

import sk.tomsik68.pw.region.CuboidRegion;

public class CuboidIterator implements Iterator<Block> {
	private final int minX, minZ, maxX, maxZ;
	private int x, z;
	private final World world;

	public CuboidIterator(CuboidRegion cuboidRegion) {
		world = cuboidRegion.getWorld();
		minX = cuboidRegion.getMinX();
		minZ = cuboidRegion.getMinZ();
		maxX = cuboidRegion.getMaxX();
		maxZ = cuboidRegion.getMaxZ();
		z = minZ;
	}

	@Override
	public boolean hasNext() {
		return x < maxX && z < maxZ;
	}

	@Override
	public Block next() {
		++x;
		if (x == maxX) {
			x = minX;
			z++;
		}
		return world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);
	}

	@Override
	public void remove() {

	}

}
