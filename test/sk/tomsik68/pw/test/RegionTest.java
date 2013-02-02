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
package sk.tomsik68.pw.test;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.bukkit.block.Biome;
import org.junit.Test;

import sk.tomsik68.pw.impl.SimpleRegionManager;
import sk.tomsik68.pw.region.BiomeRegion;
import sk.tomsik68.pw.region.WorldRegion;

public class RegionTest {

    @Test
    public void test() {
        SimpleRegionManager regionManager = new SimpleRegionManager();
        UUID world1 = UUID.randomUUID();
        UUID world2 = UUID.randomUUID();
        regionManager.addRegion(new WorldRegion(world1));
        regionManager.addRegion(new WorldRegion(world2));
        regionManager.addRegion(new WorldRegion(world2));
        regionManager.addRegion(new BiomeRegion(world1, Biome.BEACH));
        regionManager.addRegion(new BiomeRegion(world1,Biome.DESERT));
        regionManager.addRegion(new BiomeRegion(world2,Biome.DESERT));
        regionManager.addRegion(new WorldRegion(world2));
        regionManager.addRegion(new WorldRegion(world2));
        assertEquals(8,regionManager.getAllRegions().size());
        assertEquals(3, regionManager.getRegions(world1).size());
        assertEquals(5, regionManager.getRegions(world2).size());
    }

}
