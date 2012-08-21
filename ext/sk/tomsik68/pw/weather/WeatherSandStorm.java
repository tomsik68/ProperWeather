package sk.tomsik68.pw.weather;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherSandStorm extends Weather {
    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 50, 50, new String[] { "MeteorStorm,Storm,Rain,ItemRain" });

    public WeatherSandStorm(WeatherDescription wd, Integer region) {
        super(wd, region);
    }

    public void initWeather() {
        getController().clear();
    }

    public void onRandomTime() {
        Region region = getController().getRegion();
        World world = region.getWorld();
        Random random = new Random();
        for (Block block : region) {
            if (block == null)
                continue;
            if (random.nextInt(100) == 1) {
                int blocksUp = 1+random.nextInt(4);
                for (int y = 0; y < blocksUp; y++) {
                    BlockState bs = block.getRelative(BlockFace.UP, y).getState();
                    bs.setType(Material.SAND);
                    region.updateBlockState(bs);
                }
                if ((random.nextBoolean()) && (world.getHighestBlockAt(block.getLocation()).getType() == Material.SAND)) {
                    BlockState bs = world.getHighestBlockAt(block.getLocation()).getState();
                    bs.setType(Material.AIR);
                    region.updateBlockState(bs);
                }
            }
        }
    }
}