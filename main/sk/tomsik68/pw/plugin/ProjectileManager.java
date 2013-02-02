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
package sk.tomsik68.pw.plugin;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
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
