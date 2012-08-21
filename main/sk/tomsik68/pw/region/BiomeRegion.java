package sk.tomsik68.pw.region;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import sk.tomsik68.pw.region.blockiter.BiomeBlockIterator;

public class BiomeRegion extends BaseRegion {
    private static final long serialVersionUID = 1623708448177398641L;
    private Biome biome;

    public BiomeRegion() {
        super(null);
    }

    public BiomeRegion(UUID uuid, Biome biome1) {
        super(uuid);
        biome = biome1;
    }

    public boolean contains(Location location) {
        return (location.getBlock().getBiome().equals(biome)) && (location.getWorld().getUID().equals(world));
    }

    public Player[] getPlayers() {
        if (!isWorldLoaded())
            return new Player[0];
        Player[] result = new Player[0];
        for (Player p : getWorld().getPlayers()) {
            if (contains(p.getLocation())) {
                Player[] result2 = new Player[result.length + 1];
                System.arraycopy(result, 0, result2, 0, result.length);
                result2[result.length] = p;
                result = result2;
            }
        }
        return result;
    }

    public Rectangle getBounds() {
        return null;
    }

    public Iterator<Block> iterator() {
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
        return "Biome '" + biome.name() + "' in '" + Bukkit.getWorld(world).getName()+"'";
    }

    @Override
    public boolean isProbabilityOn() {
        return false;
    }
}