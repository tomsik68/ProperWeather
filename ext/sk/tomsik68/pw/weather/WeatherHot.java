package sk.tomsik68.pw.weather;

import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherHot extends Weather {

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 50, 50, new String[] { "MeteorStorm,Storm,Rain,ItemRain" });

    public WeatherHot(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void initWeather() {
        getController().clear();
        getController().setSunSize(200);
    }

    public void onRandomTime() {
        WeatherController controller = getController();

        if (controller == null)
            return;
        World world = controller.getRegion().getWorld();
        if (world == null)
            return;
        Region region = controller.getRegion();
        Random rand = new Random();
        if ((world.getLoadedChunks() == null) || (world.getLoadedChunks().length < 0))
            return;
        for (Block block : region) {
            if (block == null)
                continue;
            if ((block.getType() == Material.WATER) && (block.getBiome() == Biome.DESERT)) {
                BlockState state = block.getState();
                state.setData(new MaterialData(0, (byte) 0));
                region.updateBlockState(state);
                int smokes = rand.nextInt(5);
                for (int i = 0; i < smokes; i++)
                    world.playEffect(block.getLocation(), Effect.SMOKE, 4);
            } else if (block.getBiome() == Biome.DESERT) {
                BlockState state = block.getState();
                state.setData(new MaterialData(Material.FIRE, (byte) 0));
                region.updateBlockState(state);
            }
        }
    }
}