package sk.tomsik68.pw.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import sk.tomsik68.pw.RegionType;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.BiomeRegion;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.region.WorldRegion;

public class SimpleRegionManager implements RegionManager {
    private final Map<Integer, Region> regions = new HashMap<Integer, Region>();
    private final Map<UUID, Integer[]> worldRegions = new HashMap<UUID, Integer[]>();

    private static int lastRegionID = -1;

    public Region getRegion(Integer id) {
        return regions.get(id);
    }

    public int addRegion(Region region) {
        int r = lastRegionID++;
        while (regions.containsKey(Integer.valueOf(r))) {
            r++;
        }
        region.setUID(r);
        regions.put(Integer.valueOf(r), region);
        if (!worldRegions.containsKey(region.getWorldId())) {
            worldRegions.put(region.getWorldId(), new Integer[] { Integer.valueOf(r) });
        } else {
            Integer[] array = worldRegions.get(region.getWorldId());
            Integer[] array2 = new Integer[array.length + 1];
            System.arraycopy(array, 0, array2, 0, array.length);
            array2[array.length] = Integer.valueOf(r);
            worldRegions.put(region.getWorldId(), array2);
        }
        return r;
    }

    public Region getRegionAt(Location location) {
        List<Region> check = new ArrayList<Region>();
        Integer[] regionIds = worldRegions.get(location.getWorld().getUID());
        for (Integer i : regionIds) {
            check.add(regions.get(i));
        }
        for (Region region : check) {
            if (region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public void unHook(World world) {
        Integer[] toRemove = worldRegions.get(world.getUID());
        Integer[] arrayOfInteger1;
        int j = (arrayOfInteger1 = toRemove).length;
        for (int i = 0; i < j; i++) {
            int r = arrayOfInteger1[i].intValue();
            Region region = getRegion(Integer.valueOf(r));
            if (region.getWorld().equals(world)) {
                regions.remove(Integer.valueOf(region.getUID()));
            }
        }
        worldRegions.remove(world.getUID());
    }

    public void hook(World world, RegionType regionType) {
        List<Integer> ids = new ArrayList<Integer>();

        switch (regionType) {
        case BIOME:
            for (Biome biome : Biome.values()) {
                ids.add(addRegion(new BiomeRegion(world.getUID(), biome)));
            }
            break;
        default:
            ids.add(addRegion(new WorldRegion(world.getUID())));
        }
    }

    public List<Integer> getRegions(World world) {
        if (worldRegions.containsKey(world.getUID())) {
            return Arrays.asList(worldRegions.get(world.getUID()));
        }
        return null;
    }

    public boolean isHooked(World world) {
        return worldRegions.containsKey(world.getUID());
    }

    public List<String> getWorlds() {
        List<String> result = new ArrayList<String>();
        for (UUID uid : worldRegions.keySet()) {
            result.add(Bukkit.getWorld(uid).getName());
        }
        return result;
    }

    public void saveRegions() {
        System.out.println("[ProperWeather] Saving region data...");
        File file = new File(ProperWeather.instance().getDataFolder(), "regions.dat");
        try {
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(new ArrayList<Region>(regions.values()));
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRegions() {
        System.out.println("[ProperWeather] Loading region data(automatically dropping broken regions)...");
        File file = new File(ProperWeather.instance().getDataFolder(), "regions.dat");
        regions.clear();
        if (file.exists())
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

                @SuppressWarnings("unchecked")
                ArrayList<Region> regs = (ArrayList<Region>) ois.readObject();
                ois.close();
                HashMap<UUID, ArrayList<Integer>> worldregs = new HashMap<UUID, ArrayList<Integer>>();
                for (Region region : regs) {
                    if(region.getWorldId() == null)
                        continue;
                    regions.put(Integer.valueOf(region.getUID()), region);
                    if (!worldregs.containsKey(region.getWorldId())) {
                        worldregs.put(region.getWorldId(), new ArrayList<Integer>());
                    }
                    ArrayList<Integer> worldRegionss = worldregs.get(region.getWorldId());
                    worldRegionss.add(Integer.valueOf(region.getUID()));
                    worldregs.put(region.getWorldId(), worldRegionss);
                }
                if (regs.size() > 0)
                    lastRegionID = regs.get(regs.size() - 1).getUID();
                else
                    lastRegionID = 0;
                for (Entry<UUID, ArrayList<Integer>> entry : worldregs.entrySet()) {
                    worldRegions.put(entry.getKey(), entry.getValue().toArray(new Integer[0]));
                }
                System.out.println("[ProperWeather] Loaded " + regions.size() + " regions.");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public Collection<Region> getAllRegions() {
        return regions.values();
    }

    @Override
    public List<Integer> getRegions(UUID worldId) {
        if (worldRegions.containsKey(worldId)) {
            return Arrays.asList(worldRegions.get(worldId));
        }
        return null;
    }
}