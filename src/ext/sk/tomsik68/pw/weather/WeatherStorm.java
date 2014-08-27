package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.region.Region;

public class WeatherStorm extends Weather {
    public static final WeatherDefaults def = new BasicWeatherDefaults(45);

    public WeatherStorm(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void doInitWeather() {
        getController().setRaining(true);
        getController().setThundering(true);
    }

    public void onRandomTime() {
        final WeatherController controller = getController();
        final Region region = controller.getRegion();
        final Random rand = new Random();
        for (Block block : region) {
            if (block == null)
                continue;
            if (block.getType() == Material.FIRE && rand.nextInt(10) == 7) {
                BlockState state = block.getState();
                state.setType(Material.AIR);
                region.updateBlockState(state);
            }
            if (rand.nextInt(100000) != 0 || block.getType() == Material.SAND)
                continue;
            controller.strike(block.getLocation());

        }
    }
}