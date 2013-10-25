/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw.region;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.plugin.ProjectileManager;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.struct.SpawnListEntry;
import sk.tomsik68.pw.struct.WeatherData;

public abstract class BaseRegion implements Region {
    private static final long serialVersionUID = 3917523437018474830L;
    protected UUID world;
    protected int u = -1;
    private ArrayList<BlockState> changedBlocks = new ArrayList<BlockState>();
    private ArrayList<SpawnListEntry> spawnList = new ArrayList<SpawnListEntry>();

    public BaseRegion(UUID w) {
        world = w;
    }

    public abstract Player[] getPlayers();

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

    public abstract Iterator<Block> iterator();

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
                if(entry.getVelocity() != null)
                    entity.setVelocity(entry.getVelocity());
                if (entity instanceof Projectile)
                    ProjectileManager.add(entity);
                if(entry.getCustomizer() != null)
                    entity = entry.getCustomizer().perform(entity);
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
    public WeatherData getWeatherData() {
        return ProperWeather.instance().getWeatherSystem().getCurrentSituation(getUID());
    }
}