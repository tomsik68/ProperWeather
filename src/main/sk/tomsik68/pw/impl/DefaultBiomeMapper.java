package sk.tomsik68.pw.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;

import sk.tomsik68.pw.api.BiomeMapper;

public class DefaultBiomeMapper implements BiomeMapper {
    private HashMap<Biome, ArrayList<Long>> blocks = new HashMap<Biome, ArrayList<Long>>();
    private HashSet<Long> scannedChunks = new HashSet<Long>();

    public DefaultBiomeMapper() {
        // prevents some nulls
        for (Biome biome : Biome.values()) {
            blocks.put(biome, new ArrayList<Long>());
        }
    }

    @Override
    public boolean isScanned(int x, int z) {
        return scannedChunks.contains(compress(x, z));
    }

    @Override
    public void setScanned(int x, int z) {
        synchronized (scannedChunks) {
            if (!scannedChunks.contains(compress(x, z)))
                scannedChunks.add(compress(x, z));
        }
    }

    @Override
    public void scan(Chunk chunk) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                ArrayList<Long> list = blocks.get(chunk.getBlock(x, z, 0).getBiome());
                if (list == null)
                    list = new ArrayList<Long>();
                int xx = 16 * chunk.getX() + x;
                int zz = 16 * chunk.getZ() + z;
                list.add(compress(xx, zz));
                synchronized (blocks) {
                    blocks.put(chunk.getBlock(x, z, 0).getBiome(), list);
                }
            }
        setScanned(chunk.getX(), chunk.getZ());
    }

    private long compress(int i1, int i2) {
        long result = i1;
        result = result << 32;
        result = result | i2;
        return result;
    }

    @Override
    public List<Long> getBlocks(Biome biome) {
        if (blocks.containsKey(biome)) {
            return blocks.get(biome);
        }
        return new ArrayList<Long>();
    }
}
