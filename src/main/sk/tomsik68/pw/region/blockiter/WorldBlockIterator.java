package sk.tomsik68.pw.region.blockiter;

import java.util.Iterator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import sk.tomsik68.pw.region.WorldRegion;

public class WorldBlockIterator implements Iterator<Block> {
	private World world;
	private int x = 0;
	private int z = 0;
	private int chunkID = 0;
	private Chunk chunk;
	private Chunk[] chunks;

	public WorldBlockIterator(WorldRegion region) {
		this.world = region.getWorld();
		chunks = world.getLoadedChunks();
		chunk = chunks[0];
	}

	public boolean hasNext() {
		return this.chunkID < this.chunks.length;
	}

	public Block next() {
		this.x += 1;
		if (this.x == 16) {
			this.x = 0;
			this.z += 1;
		}
		if (this.z == 16) {
			this.x = 0;
			this.z = 0;
			this.chunkID += 1;
			if (this.chunks.length > this.chunkID)
				this.chunk = this.chunks[this.chunkID];
			else
				this.chunkID = (this.chunks.length + 1);
		}
		return this.chunk.getBlock(this.x, this.world.getHighestBlockYAt(this.x + 16 * this.chunk.getX(), this.z + 16 * this.chunk.getZ()), this.z);
	}

	public void remove() {
	}
}