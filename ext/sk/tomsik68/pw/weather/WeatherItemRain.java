package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;

public class WeatherItemRain extends Weather {

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(12000, 25, 20, new String[] { "MeteorStorm,Hot,Storm" });

    public WeatherItemRain(WeatherDescription wd1, Integer world) {
        super(wd1, world);
    }

    public void initWeather() {
        getController().clear();
        getController().setRaining(true);
        getController().setStarFrequency(120);
    }

    public void onRandomTime() {
        final WeatherController controller = getController();
        final Random rand = new Random();
        final World world = controller.getRegion().getWorld();
        Region region = controller.getRegion();
        for (final Block block : region) {
            if (block == null)
                continue;
            if (rand.nextInt(300) == 0) {
                final int id = 256 + rand.nextInt(Material.values().length - 256);
                //we have to keep the TickNextTickList synchronized
                Bukkit.getScheduler().scheduleSyncDelayedTask(ProperWeather.instance(), new Runnable() {
                    @Override
                    public void run() {
                        world.dropItem(block.getRelative(BlockFace.UP).getLocation(), new ItemStack(id));
                    }
                });
            }
        }
    }
}