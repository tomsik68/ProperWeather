package sk.tomsik68.pw.struct;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class SpawnListEntry {
	private final Location location;
	private final Class<? extends Entity> entityClass;
	private final Vector velocity;

	public SpawnListEntry(Location l, Class<? extends Entity> c, Vector v) {
		this.location = l;
		this.entityClass = c;
		this.velocity = v;
	}

	public Location getLocation() {
		return this.location;
	}

	public Vector getVelocity() {
		return this.velocity;
	}

	public Class<? extends Entity> getEntityClass() {
		return this.entityClass;
	}
}