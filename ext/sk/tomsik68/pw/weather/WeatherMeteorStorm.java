package sk.tomsik68.pw.weather;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherMeteorStorm extends Weather {
    private static final Vector velocity = new Vector(0, -0.5d, 0);

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(18000, 35, 75, new String[] { "Clear,Hot,ArrowRain" });

    public WeatherMeteorStorm(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void initWeather() {
        WeatherController controller = getController();
        getController().clear();
        controller.allowThundering();
        controller.setRaining(true);
    }

    public void onRandomTime() {
        WeatherController controller = getController();
        World world = controller.getRegion().getWorld();
        Region region = controller.getRegion();
        Random rand = new Random(world.getSeed() * world.getFullTime());
        for (Block block : region) {
            if (block == null)
                continue;
            int x = block.getX();
            int z = block.getZ();
            if (rand.nextInt(1000) == 854) {
                Location loc = new Location(world, x, 128.0D, z);
                loc.setPitch(0f);
                loc.setYaw(0f);
                region.spawnEntity(Fireball.class, loc, velocity);
            }
        }
    }
}