package sk.tomsik68.pw.weather;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherArrowRain extends Weather {

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(18000, 35, 75, new String[] { "MeteorStorm", "Storm", "ItemRain" });

    public WeatherArrowRain(WeatherDescription wd1, Integer region) {
        super(wd1, region);
    }

    public void onRandomTime() {
        WeatherController controller = getController();
        if (controller == null)
            return;
        Region region = controller.getRegion();
        if (region == null)
            return;
        World world = region.getWorld();
        if (world == null)
            return;
        Random rand = new Random();
        if ((world.getLoadedChunks() == null) || (world.getLoadedChunks().length < 0)) {
            return;
        }
        for (Block block : region) {
            if (block == null)
                continue;
            int x = block.getX();
            int z = block.getZ();
            if (rand.nextInt(10) == 5) {
                Vector vel = new Vector();
                vel.setX(0);
                vel.setZ(0);
                vel.setY(rand.nextDouble());
                Location loc = new Location(world, x - rand.nextInt(16), 100 + rand.nextInt(28), z - rand.nextInt(16));
                region.spawnEntity(Arrow.class, loc, vel);
            }
        }
    }

    public void initWeather() {
        getController().clear();
    }
}