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
    public Player[] getPlayers();

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
}