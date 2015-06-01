package sk.tomsik68.pw.impl;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

import sk.tomsik68.pw.api.BiomeMapper;
import sk.tomsik68.pw.api.BiomeMapperManager;
import sk.tomsik68.pw.plugin.ProperWeather;

public final class DefaultBiomeMapperManager implements BiomeMapperManager, Runnable {
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

	@EventHandler
	public void onWorldLoaded(WorldLoadEvent event) {
		scanWorld(event.getWorld());
	}

	protected void scanWorld(World world) {
		Chunk[] chunks = world.getLoadedChunks();
		UUID worldId = world.getUID();
		for (Chunk chunk : chunks) {
			if (chunk == null)
				break;
			synchronized (mappers) {
				if (!mappers.containsKey(world)) {
					mappers.put(worldId, new DefaultBiomeMapper());
				}
				if (!mappers.get(worldId).isScanned(chunk.getX(), chunk.getZ())) {
					mappers.get(worldId).scan(chunk);
				}
			}
		}
	}

	@Override
	public void scheduleCompleteScan() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(ProperWeather.instance(), this);
	}

	// this is called from scheduler to scan all worlds
	@Override
	public void run() {
		for (String w : ProperWeather.instance().getWeatherSystem().getWorldList()) {
			World world = Bukkit.getWorld(w);
			scanWorld(world);
		}
	}

}
