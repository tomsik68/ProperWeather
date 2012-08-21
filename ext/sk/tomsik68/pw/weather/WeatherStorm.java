package sk.tomsik68.pw.weather;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherStorm extends WeatherRain {

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 45, 45, new String[] { "MeteorStorm,Storm,ItemRain" });

    public WeatherStorm(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void initWeather() {
        super.initWeather();
        getController().allowThundering();
    }

    public void onRandomTime() {
        final WeatherController controller = getController();
        final World world = controller.getRegion().getWorld();
        final Region region = controller.getRegion();
        final Random rand = new Random(world.getSeed() * world.getFullTime());
        for (Block block : region) {
            if (block == null)
                continue;
            if (rand.nextInt(100000) != 0 || block.getType() == Material.SAND)
                continue;
            controller.strike(block.getLocation());
        }
    }
}