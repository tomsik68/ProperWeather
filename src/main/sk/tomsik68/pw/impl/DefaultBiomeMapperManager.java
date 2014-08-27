package sk.tomsik68.pw.impl;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;

import sk.tomsik68.pw.api.BiomeMapper;
import sk.tomsik68.pw.api.BiomeMapperManager;
import sk.tomsik68.pw.plugin.ProperWeather;

public class DefaultBiomeMapperManager implements BiomeMapperManager {
    private HashMap<UUID, BiomeMapper> mappers = new HashMap<UUID, BiomeMapper>();

    @Override
    public BiomeMapper getMapper(World world) {
        return getMapper(world.getUID());
    }

    @Override
    public BiomeMapper getMapper(String world) {
        return getMapper(Bukkit.getWorld(world).getUID());
    }

    @Override
    public BiomeMapper getMapper(UUID world) {
        return mappers.get(world);
    }

    @Override
    public void setBiomeMapper(UUID world, BiomeMapper mapper) {
        synchronized (mappers) {
            mappers.put(world, mapper);
        }
    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoaded(ChunkLoadEvent event) {
        UUID world = event.getChunk().getWorld().getUID();
        synchronized (mappers) {
            if (!mappers.containsKey(world)) {
                mappers.put(world, new DefaultBiomeMapper());
            }
            if (!mappers.get(world).isScanned(event.getChunk().getX(), event.getChunk().getZ())) {
                mappers.get(world).scan(event.getChunk());
            }
        }
    }

    @Override
    public void completeScan() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                final List<String> worlds = ProperWeather.instance().getWeatherSystem().getWorldList();
                for (String s : worlds) {
                    Chunk[] chunks = Bukkit.getWorld(s).getLoadedChunks();
                    for (Chunk chunk : chunks) {
                        if (chunk == null)
                            break;
                        UUID world = chunk.getWorld().getUID();
                        synchronized (mappers) {
                            if (!mappers.containsKey(world)) {
                                mappers.put(world, new DefaultBiomeMapper());
                            }
                            if (!mappers.get(world).isScanned(chunk.getX(), chunk.getZ())) {
                                mappers.get(world).scan(chunk);
                            }
                        }
                    }
                }
            }
        };
        thread.start();
    }

}
