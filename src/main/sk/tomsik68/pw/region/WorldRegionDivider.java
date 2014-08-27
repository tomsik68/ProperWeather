package sk.tomsik68.pw.region;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class WorldRegionDivider implements RegionDivider {

    @Override
    public List<Region> divideToRegions(World world) {
        return Arrays.asList((Region) new WorldRegion(world.getUID()));
    }

}
