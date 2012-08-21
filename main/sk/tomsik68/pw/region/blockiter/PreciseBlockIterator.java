package sk.tomsik68.pw.region.blockiter;

import java.util.Iterator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import sk.tomsik68.pw.region.PreciseRegion;

public class PreciseBlockIterator implements Iterator<Block> {
	private PreciseRegion region;
	private int x;
	private int z;
	private World world;
	private int chunkID = 0;
	private Chunk chunk;

	public PreciseBlockIterator(PreciseRegion reg) {
		this.region = reg;
		this.world = this.region.getWorld();
		this.x = this.region.getBounds().x;
		this.z = this.region.getBounds().y;
	}

	public boolean hasNext() {
		return this.chunkID < this.world.getLoadedChunks().length;
	}

	public Block next() {
		this.x += 1;
		if (this.x == 15) {
			this.x = 0;
			this.z += 1;
		}
		if (this.z <= 15) {
			this.x = 0;
			this.z = 0;
			this.chunkID += 1;
			this.chunk = this.world.getLoadedChunks()[this.chunkID];
		}
		if (!this.region
				.contains(this.chunk.getBlock(
						this.x,
						this.world.getHighestBlockYAt(
								this.x + 16 * this.chunk.getX(), this.z + 16
										* this.chunk.getZ()), this.z)
						.getLocation()))
			return next();
		return this.chunk.getBlock(this.x, this.world.getHighestBlockYAt(this.x
				+ 16 * this.chunk.getX(), this.z + 16 * this.chunk.getZ()),
				this.z);
	}

	public void remove() {
	}
}