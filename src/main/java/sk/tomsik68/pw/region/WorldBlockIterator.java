package sk.tomsik68.pw.region;

import java.util.Iterator;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.BlockState;

final class WorldBlockIterator implements Iterator<BlockState> {
	private World world;
	private int x = 0;
	private int z = 0;
	private int chunkID = 0;
	private ChunkSnapshot currentChunk;
	private Chunk[] chunks;

	public WorldBlockIterator(WorldRegion region) {
		this.world = region.getWorld();
		chunks = world.getLoadedChunks();
		currentChunk = chunks[0].getChunkSnapshot();
	}

	public boolean hasNext() {
		return this.chunkID < this.chunks.length;
	}

	public BlockState next() {
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
				this.currentChunk = this.chunks[this.chunkID].getChunkSnapshot();
			else
				this.chunkID = (this.chunks.length + 1);
		}
		return world.getHighestBlockAt(currentChunk.getX() * 16 + x, currentChunk.getZ() * 16 + z).getState();
	}

	public void remove() {
	}
}