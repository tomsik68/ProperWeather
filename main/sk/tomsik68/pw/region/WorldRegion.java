package sk.tomsik68.pw.region;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.region.blockiter.WorldBlockIterator;

public class WorldRegion extends BaseRegion {
    private static final long serialVersionUID = 4642185874482764224L;

    public WorldRegion() {
        super(null);
    }

    public WorldRegion(UUID world1) {
        super(world1);
    }

    public Player[] getPlayers() {
        if (!isWorldLoaded()) {
            return new Player[0];
        }
        Player[] result = new Player[getWorld().getPlayers().size()];
        result = Util.getPlayers(getWorld()).toArray(new Player[0]);
        return result;
    }

    public boolean contains(Location location) {
        if (!isWorldLoaded())
            return false;
        return location.getWorld().getUID().equals(getWorld().getUID());
    }

    public Iterator<Block> iterator() {
        return new WorldBlockIterator(this);
    }

    public String toString() {
        return "World '" + Bukkit.getWorld(this.world).getName() + "'";
    }

}