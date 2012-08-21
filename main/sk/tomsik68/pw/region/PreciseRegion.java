package sk.tomsik68.pw.region;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import sk.tomsik68.pw.region.blockiter.PreciseBlockIterator;

public class PreciseRegion extends BaseRegion {
    private static final long serialVersionUID = -602844574122798463L;
    private List<Rectangle> bounds;
    private Rectangle roughBounds;

    public PreciseRegion() {
        super(null);
    }

    public PreciseRegion(World w) {
        super(w.getUID());
        this.bounds = new ArrayList<Rectangle>();
    }

    public void addSub(Rectangle rect) {
        this.bounds.add(rect);
    }

    public void addSub(int x1, int z1, int x2, int z2) {
        addSub(new Rectangle(x1, z1, x2 - x1, z2 - z1));
    }

    public Rectangle getBounds() {
        return this.roughBounds;
    }

    public Rectangle getRoughBounds() {
        return this.roughBounds;
    }

    public void setRoughBounds(Rectangle roughBounds1) {
        this.roughBounds = roughBounds1;
    }

    public boolean contains(Location location) {
        if (!isWorldLoaded())
            return false;
        if ((this.roughBounds.contains(location.getBlockX(), location.getBlockZ())) && (location.getWorld().equals(this.world))) {
            for (Rectangle rect : this.bounds) {
                if (rect.contains(location.getBlockX(), location.getBlockZ()))
                    return true;
            }
        }
        return false;
    }

    public Player[] getPlayers() {
        if (!isWorldLoaded())
            return new Player[0];
        Player[] result = new Player[0];
        for (Player p : getWorld().getPlayers()) {
            if ((!this.roughBounds.contains(p.getLocation().getBlockX(), p.getLocation().getBlockZ())) || (!contains(p.getLocation())))
                continue;
            Player[] result2 = new Player[result.length + 1];
            System.arraycopy(result, 0, result2, 0, result.length);
            result2[result.length] = p;
            result = result2;
        }

        return result;
    }

    public Iterator<Block> iterator() {
        return new PreciseBlockIterator(this);
    }

    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.roughBounds = ((Rectangle) in.readObject());
        this.bounds = ((List<Rectangle>) in.readObject());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(this.roughBounds);
        out.writeObject(this.bounds);
    }

    @Override
    public boolean isProbabilityOn() {
        return false;
    }
}