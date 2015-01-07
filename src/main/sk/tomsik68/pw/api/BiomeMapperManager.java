package sk.tomsik68.pw.api;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.event.Listener;

/**
 * Manages Biome mappers for all worlds.
 * 
 * @author Tomsik68
 *
 */
public interface BiomeMapperManager extends Listener {
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

	/**
	 * Sets biome mapper for specified world.
	 * 
	 * @param world
	 * @param mapper
	 */
	public void setBiomeMapper(UUID world, BiomeMapper mapper);

	/**
	 * This function schedules complete scan of world blocks. Attention: it can
	 * be called from other thread, so it must be thread-safe
	 */
	public void scheduleCompleteScan();
}
