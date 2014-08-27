package sk.tomsik68.pw.region;

import java.util.List;

import org.bukkit.World;

public interface RegionDivider {
    public List<Region> divideToRegions(World world);
}
