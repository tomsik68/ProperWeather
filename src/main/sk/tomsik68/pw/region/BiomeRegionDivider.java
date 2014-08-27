package sk.tomsik68.pw.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Biome;

public class BiomeRegionDivider implements RegionDivider {

    @Override
    public List<Region> divideToRegions(World world) {
        List<Region> result = new ArrayList<Region>();
        for (Biome biome : Biome.values()) {
            result.add(new BiomeRegion(world.getUID(), biome));
        }
        return result;
    }
}
