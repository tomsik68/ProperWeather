package sk.tomsik68.pw.region;

import java.util.Iterator;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.BiomeRegion;

final class BiomeBlockIterator implements Iterator<BlockState> {
	private World world;
	private Biome biome;
	private List<Long> blocks;
	private int current = 0;

	public BiomeBlockIterator(BiomeRegion region) {
		world = region.getWorld();
		biome = region.getBiome();
		// fixes error on reload
		try {
			blocks = ProperWeather.instance().getMapperManager().getMapper(region.getWorld().getUID()).getBlocks(biome);
		} catch (NullPointerException npe) {
			ProperWeather.instance().getMapperManager().scheduleCompleteScan();
			// iteration request is flagged as cancelled since the "blocks" list
			// stays null
		}
	}

	public boolean hasNext() {
		// if blocks list is null, the iteration request gets fired, but it
		// doesn't mind, since the requests are random.. :)
		return blocks != null && current < blocks.size();
	}

	public BlockState next() {
		long location = blocks.get(current);
		int x = decompress(location)[0];
		int z = decompress(location)[1];
		BlockState result = world.getHighestBlockAt(x, z).getState();
		current++;
		return result;
	}

	public void remove() {
	}

	private int[] decompress(long l) {
		int a1 = (int) (l & Integer.MAX_VALUE);
		int a2 = (int) (l >> 32);
		return new int[] { a2, a1 };
	}
}
