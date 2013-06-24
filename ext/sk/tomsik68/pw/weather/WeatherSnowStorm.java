package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.weather.element.Wind;

public class WeatherSnowStorm extends Weather {
    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 50, 50, new String[] { "meteorstorm", "hot" }, Wind.class.getName());
    private static final Random rand = new Random();

    public WeatherSnowStorm(WeatherDescription wd1, int region) {
        super(wd1, region);
    }

    @Override
    public void initWeather() {
        WeatherController wc = getController();
        wc.clear();
        // well, we don't know if setSnowing actually works, so call setRaining
        // first, so setSnowing can override it in case it works
        wc.setRaining(true);
        wc.setSnowing(true);
    }

    @Override
    public void onRandomTime() {
        Region region = getController().getRegion();
        for (Block block : region) {
            if (rand.nextInt(100) < 43) {
                BlockState state = block.getRelative(BlockFace.UP).getState();
                if (state.getType() == Material.AIR) {
                    state.setType(Material.SNOW_BLOCK);
                    region.updateBlockState(state);
                }
            }
        }
    }
}
