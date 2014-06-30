package sk.tomsik68.pw.plugin;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

/**
 * This class kills remained fireballs & arrows on server shutdown/reload to
 * prevent bugs
 * 
 * @author Tomsik68
 * 
 */
public class ProjectileManager {
    private static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static int cacheSize = 468;

    public static final void killAll() {
        for (Entity e : entities) {
            if (e != null)
                e.remove();
        }
        entities.clear();
    }

    public static final void add(Entity e) {
        entities.add(e);
        if (entities.size() > cacheSize)
            for (int i = 0; i < 25; i++) {
                entities.get(i).remove();
                entities.remove(i);
            }
    }

    public static int size() {
        return entities.size();
    }

    public static void killAll(Class<? extends Projectile> clazz) {
        for (int i = 0; i < entities.size(); i++) {
            if (clazz.isInstance(entities.get(i))) {
                entities.get(i).remove();
                entities.remove(i);
            }
        }
    }
}
