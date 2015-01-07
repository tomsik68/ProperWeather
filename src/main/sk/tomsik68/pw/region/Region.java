package sk.tomsik68.pw.region;

import java.io.Externalizable;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.RegionManager;

/**
 * Represents a region
 * 
 * @author Tomsik68
 * 
 */
public interface Region extends Comparable<Region>, Iterable<Block>, Externalizable {
	/**
	 * 
	 * @return All players in the region
	 */
	public Iterable<Player> getPlayers();

	/**
	 * 
	 * @return World this region is in
	 */
	public World getWorld();

	/**
	 * 
	 * @return UUID of the world this region is in
	 */
	public UUID getWorldId();

	/**
	 * 
	 * @return This region's ID
	 */
	public int getUID();

	/**
	 * Sets this region's ID to <code>paramInt</code>
	 * 
	 * @param paramInt
	 */
	public void setUID(int paramInt);

	/**
	 * 
	 * @param paramLocation
	 * @return Whether this region contains the location
	 */
	public boolean contains(Location paramLocation);

	/**
	 * Schedules a BlockState update for block contained in this region.
	 * 
	 * @param paramBlockState
	 */
	public void updateBlockState(BlockState paramBlockState);

	/**
	 * Method called by Synchronized task which calls update() on all
	 * blockstates scheduled to be updated and spawns all entities
	 * 
	 */
	public void update();

	/**
	 * Schedules entity spawn.
	 * 
	 * @param paramClass
	 * @param paramLocation
	 * @param paramVector
	 */
	public void spawnEntity(Class<? extends Entity> paramClass, Location paramLocation, Vector paramVector);

	/**
	 * 
	 * @return WeatherDataExt for this region from WeatherSystem
	 */
	public IWeatherData getWeatherData();

	public void setRegionManager(RegionManager regionManager);
}