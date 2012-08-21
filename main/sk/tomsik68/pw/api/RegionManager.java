package sk.tomsik68.pw.api;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import sk.tomsik68.pw.RegionType;
import sk.tomsik68.pw.region.Region;

public abstract interface RegionManager {
    /**
     * 
     * @param paramInteger
     * @return Region with specified id
     */
    public abstract Region getRegion(Integer paramInteger);

    /**
     * Adds region to the region manager
     * 
     * @param paramRegion
     * @return Region's id
     */
    public abstract int addRegion(Region paramRegion);

    /**
     * Region at specified location.
     * 
     */
    public abstract Region getRegionAt(Location paramLocation);

    /**
     * Deletes all regions of specified world from the manager.
     * 
     * @param paramWorld
     */
    public abstract void unHook(World paramWorld);

    /**
     * Splits up the world into regions and registers the regions
     * 
     * @param paramWorld
     * @param paramRegionType
     */
    public abstract void hook(World paramWorld, RegionType paramRegionType);

    /**
     * 
     * @param paramWorld
     * @return Region ID list for specified world
     */
    public abstract List<Integer> getRegions(World paramWorld);

    /**
     * 
     * @param worldId
     * @return Region ID list for specified world
     */
    public List<Integer> getRegions(UUID worldId);
    /**
     * 
     * @param paramWorld
     * @return Whether manager is hooked to the world
     */
    public abstract boolean isHooked(World paramWorld);
    /**
     * 
     * @return Worlds this manager is currently hooked to.
     */
    public abstract List<String> getWorlds();
    /** Loads regions.
     * 
     */
    public abstract void loadRegions();
    /** Saves regions.
     * 
     */
    public abstract void saveRegions();
    /**
     * 
     * @return All regions managed by the manager.
     */
    public abstract Collection<Region> getAllRegions();
}