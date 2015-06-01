package sk.tomsik68.pw.region;

import java.util.Iterator;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.region.CuboidRegion;

final class CuboidIterator implements Iterator<BlockState> {
	private final int minX, minZ, maxX, maxZ;
	private int x, z;
	private final World world;

	public CuboidIterator(CuboidRegion cuboidRegion) {
		world = cuboidRegion.getWorld();
		minX = cuboidRegion.getMinX();
		minZ = cuboidRegion.getMinZ();
		maxX = cuboidRegion.getMaxX();
		maxZ = cuboidRegion.getMaxZ();
		x = minX;
		z = minZ;
	}

	@Override
	public boolean hasNext() {
		return x < maxX && z < maxZ;
	}

	@Override
	public BlockState next() {
		++x;
		if (x == maxX) {
			x = minX;
			z++;
		}
		return world.getBlockAt(x, world.getHighestBlockYAt(x, z), z).getState();
	}

	@Override
	public void remove() {

	}

}
