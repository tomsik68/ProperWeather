package sk.tomsik68.pw;

import java.io.InvalidObjectException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;

public class Util {

    public static List<Player> getPlayers(World world) {
        return world.getPlayers();
    }

    public static boolean hasPlayers(World world) {
        return !getPlayers(world).isEmpty();
    }

    public static WeatherDefaults getWeatherDefaults(Class<? extends Weather> wClazz) throws Exception {
        Field[] fields = wClazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getType().isAssignableFrom(WeatherDefaults.class)) {
                System.out.println("WeatherDefaults field of " + wClazz.getSimpleName() + ": " + f.getName());
                Object obj = f.get(null);
                Validate.notNull(obj, wClazz.getName() + " is invalid! WeatherDefaults are null!");
                return (WeatherDefaults) obj;
            }
        }
        throw new InvalidObjectException(wClazz.getName() + " is invalid! No WeatherDefaults found!");
    }

    @Deprecated
    /**
     * This is only used while converting OLD data structures, DO NOT USE!!!
     */
    public static HashMap<Integer, String> generateOLDIntWeatherLookupMap() {
        HashMap<Integer, String> result = new HashMap<Integer, String>();
        HashMap<String, Object> strings = new HashMap<String, Object>();
        strings.put("clear", null);
        strings.put("rain", null);
        strings.put("storm", null);
        strings.put("hot", null);
        strings.put("meteorstorm", null);
        strings.put("itemrain", null);
        strings.put("arrowrain", null);
        strings.put("sandstorm", null);
        strings.put("godanger", null);
        strings.put("windy", null);
        ArrayList<String> stringList = new ArrayList<String>(strings.keySet());
        for (int i = 0; i < stringList.size(); ++i) {
            result.put(i, stringList.get(i));
        }
        return result;
    }

    public static String dumpArray(Object... objects) {
        StringBuilder result = new StringBuilder();
        for (Object obj : objects) {
            result = result.append(obj).append(',');
        }
        if (result.length() > 0) {
            result = result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public static String dumpCollection(Iterable<Object> collection) {
        StringBuilder result = new StringBuilder();
        for (Object obj : collection) {
            result = result.append(obj).append(',');
        }
        if (result.length() > 0) {
            result = result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
