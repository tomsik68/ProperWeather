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
package sk.tomsik68.pw;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import sk.tomsik68.bukkitbp.v1.PackageResolver;
import sk.tomsik68.bukkitbp.v1.ReflectionUtils;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.plugin.ProperWeather;

//NOTBUKKIT > All the ugly reflection goes here...
public class Util {

    public static List<Player> getPlayers(World world) {
        try {
            List<Player> result = new ArrayList<Player>();
            @SuppressWarnings("unchecked")
            // List<Entity> entities = new
            // ArrayList<Entity>(world.getHandle().entityList);
            List<Object> entities = new ArrayList<Object>((Collection<Object>) ReflectionUtils.get(ReflectionUtils.call(PackageResolver.getBukkitClass("CraftWorld").cast(world), "getHandle"), "entityList"));
            for (Object e : entities) {

                if (e != null && ReflectionUtils.call(e, "getBukkitEntity") != null && ReflectionUtils.call(e, "getBukkitEntity") instanceof Player) {
                    result.add((Player) ReflectionUtils.call(e, "getBukkitEntity"));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList(Bukkit.getOnlinePlayers());
    }

    public static boolean hasPlayers(World world) {
        try {
            if (ReflectionUtils.get(ReflectionUtils.call(world, "getHandle"), "entityList") == null || ((List) ReflectionUtils.get(ReflectionUtils.call(world, "getHandle"), "entityList")).isEmpty())
                return false;
            @SuppressWarnings("unchecked")
            List<Object> entities = new ArrayList<Object>((Collection<Object>) ReflectionUtils.get(ReflectionUtils.call(world, "getHandle"), "entityList"));
            for (Object e : entities) {
                if (e != null && ReflectionUtils.call(e, "getBukkitEntity") != null && ReflectionUtils.call(e, "getBukkitEntity") instanceof Player) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WeatherDefaults getWeatherDefaults(Class<? extends Weather> wClazz) throws Exception {
        Field[] fields = wClazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getType().isAssignableFrom(WeatherDefaults.class) || WeatherDefaults.class.isAssignableFrom(f.getType())) {
                return (WeatherDefaults) f.get(null);
            }
        }

        return null;
    }

    public static void setRaining(Player p, boolean isRaining) {
        try {
            Object mcPlayer = ReflectionUtils.call(PackageResolver.getBukkitClass("entity.CraftPlayer").cast(p), "getHandle");
            Object connection = ReflectionUtils.get(mcPlayer, "playerConnection");
            Object packet = PackageResolver.getMinecraftClass("Packet70Bed").getConstructor(Integer.TYPE, Integer.TYPE).newInstance(isRaining ? 1 : 2, 0);
            ReflectionUtils.call(connection, "sendPacket", packet);
        } catch (Exception e) {
            ProperWeather.log.severe("Raining set failed.");
            e.printStackTrace();
        }
    }
}
