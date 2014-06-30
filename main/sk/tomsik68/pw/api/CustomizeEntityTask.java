package sk.tomsik68.pw.api;

import org.bukkit.entity.Entity;

/**
 * This class takes care of entity once it gets spawned in the world by region
 * manager.
 * 
 * @author Tomsik68
 * 
 */
public interface CustomizeEntityTask {
    public void perform(Entity entity);
}
