package sk.tomsik68.pw.api;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;

public interface BiomeMapper{
    /** Finds out whether specified chunk was scanned
     * 
     * @param x
     * @param z
     * @return
     */
    public boolean isScanned(int x,int z);
    /** Sets this chunk's state to scanned
     * 
     * @param x
     * @param z
     */
    public void setScanned(int x,int z);
    /** (re)scans specified chunk
     * 
     * @param chunk
     */
    public void scan(Chunk chunk);
    /**
     * 
     * @return List of Block x & z coordinates in specified Biome
     */
    public List<Long> getBlocks(Biome biome);
}
