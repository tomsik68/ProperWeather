package sk.tomsik68.pw.api;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
/** Manages Biome mappers for all worlds. 
 * 
 * @author Tomsik68
 *
 */
public interface BiomeMapperManager extends Listener{
    /**
     * 
     * @param world
     * @return BiomeMapper for specified world
     */
    public BiomeMapper getMapper(World world);
    /**
     * 
     * @param world
     * @return BiomeMapper for specified world
     */
    public BiomeMapper getMapper(String world);
    /**
     * 
     * @param world
     * @return BiomeMapper for specified world
     */
    public BiomeMapper getMapper(UUID world);
    /** Sets biome mapper for specified world.
     * 
     * @param world
     * @param mapper
     */
    public void setBiomeMapper(UUID world, BiomeMapper mapper);
    /** This is handler for ChunkLoadEvent. It's used to scan the loaded chunk 
     * 
     * @param event
     */
    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event);
    /** This function is called after server reload is detected and everything needs to be scanned.
     *  So it scans every loaded chunk of every loaded world
     */
    public void completeScan();
}
