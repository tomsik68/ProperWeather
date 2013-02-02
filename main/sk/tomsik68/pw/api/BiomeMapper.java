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

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;
/** Per-world mapper of biomes. Keeps track of which blocks are contained in each biome.
 * 
 * @author Tomsik68
 *
 */
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
