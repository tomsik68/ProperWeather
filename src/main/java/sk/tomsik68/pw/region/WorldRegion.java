package sk.tomsik68.pw.region;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

final class WorldRegion extends BaseRegion {
	private static final long serialVersionUID = 4642185874482764224L;

	public WorldRegion() {
		super(null);
	}

	public WorldRegion(UUID world1) {
		super(world1);
	}

	public boolean contains(Location location) {
		if (!isWorldLoaded())
			return false;
		return location.getWorld().getUID().equals(getWorld().getUID());
	}

	public Iterator<BlockState> iterator() {
		return new WorldBlockIterator(this);
	}

	public String toString() {
		return "World '" + Bukkit.getWorld(this.world).getName() + "'";
	}

}