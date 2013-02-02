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
