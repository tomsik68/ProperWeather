package sk.tomsik68.pw.region;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.RegionManager;
import sk.tomsik68.pw.plugin.ProjectileManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.struct.SpawnListEntry;

abstract class BaseRegion implements Region {
	private static final long serialVersionUID = 3917523437018474830L;
	protected UUID world;
	protected int u = -1;
	private ArrayList<BlockState> changedBlocks = new ArrayList<BlockState>();
	private ArrayList<SpawnListEntry> spawnList = new ArrayList<SpawnListEntry>();
	private RegionManager parent;

	public BaseRegion(UUID w) {
		world = w;
	}

	public final Iterable<Player> getPlayers() {
		if (parent == null) {
			throw new NullPointerException("Parent is null!");
		}
		return parent.getPlayers(getUID());
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	@Override
	public UUID getWorldId() {
		return world;
	}

	public int getUID() {
		return u;
	}

	public void setUID(int uid) {
		u = uid;
	}

	public abstract boolean contains(Location paramLocation);

	public int compareTo(Region arg0) {
		if (arg0.getUID() > getUID())
			return -1;
		if (arg0.getUID() < getUID())
			return 1;

		throw new IllegalArgumentException("[ProperWeather] ERROR: {'" + arg0.getUID() + "'='" + getUID() + "'" + "}Region id not unique!");
	}

	protected boolean isWorldLoaded() {
		return Bukkit.getWorld(world) != null;
	}

	public abstract Iterator<BlockState> iterator();

	public synchronized void update() {
		synchronized (changedBlocks) {
			for (BlockState bs : changedBlocks) {
				bs.update(true);
			}
			changedBlocks.clear();
		}
		synchronized (spawnList) {
			for (SpawnListEntry entry : spawnList) {
				Entity entity = getWorld().spawn(entry.getLocation(), entry.getEntityClass());
				if (entry.getVelocity() != null)
					entity.setVelocity(entry.getVelocity());
				if (entity instanceof Projectile)
					ProjectileManager.add(entity);
				if (entry.getCustomizer() != null)
					entry.getCustomizer().perform(entity);
			}
			spawnList.clear();
		}
	}

	public synchronized void updateBlockState(BlockState bs) {
		synchronized (changedBlocks) {
			changedBlocks.add(bs);
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.write(u);
		out.writeObject(world);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		u = in.read();
		world = ((UUID) in.readObject());
	}

	public synchronized void spawnEntity(Class<? extends Entity> entityClass, Location location, Vector velocity) {
		synchronized (spawnList) {
			spawnList.add(new SpawnListEntry(location, entityClass, velocity));
		}
	}

	@Override
	public IWeatherData getWeatherData() {
		return ProperWeather.instance().getWeatherSystem().getRegionData(this);
	}

	public void setRegionManager(RegionManager regionManager) {
		Validate.notNull(regionManager);
		this.parent = regionManager;
	}
}