/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
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