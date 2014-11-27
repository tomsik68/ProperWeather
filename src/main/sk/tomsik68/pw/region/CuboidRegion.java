package sk.tomsik68.pw.region;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.region.blockiter.CuboidIterator;

public class CuboidRegion extends BaseRegion {
    private int minX, minZ, maxX, maxZ;

    public CuboidRegion() {
        this(null);
    }

    public CuboidRegion(UUID w) {
        super(w);
    }

    @Override
    public boolean contains(Location loc) {
        return loc.getWorld().getUID().equals(world) && loc.getBlockX() > minX && loc.getBlockZ() > minZ && loc.getBlockX() < maxX
                && loc.getBlockZ() < maxZ;
    }

    @Override
    public Iterator<Block> iterator() {
        return new CuboidIterator(this);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }

}
