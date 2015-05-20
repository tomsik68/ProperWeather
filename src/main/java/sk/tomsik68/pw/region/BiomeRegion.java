package sk.tomsik68.pw.region;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.region.blockiter.BiomeBlockIterator;

public class BiomeRegion extends BaseRegion {
	private static final long serialVersionUID = 1623708448177398641L;
	private Biome biome;

	public BiomeRegion() {
		super(null);
	}

	public BiomeRegion(UUID worldId, Biome biome1) {
		super(worldId);
		biome = biome1;
	}

	public boolean contains(Location location) {
		return (location.getBlock().getBiome().equals(biome)) && (location.getWorld().getUID().equals(world));
	}

	public Iterator<BlockState> iterator() {
		return new BiomeBlockIterator(this);
	}

	public Biome getBiome() {
		return biome;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.write(biome.ordinal());
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		biome = Biome.values()[in.read()];
	}

	public String toString() {
		return "Biome '" + biome.name() + "' in '" + Bukkit.getWorld(world).getName() + "'";
	}
}