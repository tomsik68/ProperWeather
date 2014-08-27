package sk.tomsik68.pw.weather;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Cauldron;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherRain extends Weather {

    public static final WeatherDefaults def = new BasicWeatherDefaults(50);

    public WeatherRain(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void doInitWeather() {
        getController().setRaining(true);
    }

    @SuppressWarnings("deprecation")
    public void onRandomTime() {
        Region region = getController().getRegion();
        for (Block block : region) {
            if (block == null)
                continue;
            // extinguish all fire
            if (block.getType() == Material.FIRE) {
                BlockState state = block.getState();
                state.setType(Material.AIR);
                region.updateBlockState(state);
            } // add water to cauldrons
            else if (block.getType() == Material.CAULDRON) {
                BlockState state = block.getState();

                Cauldron data = (Cauldron) state.getData();
                if (!data.isFull()) {
                    data.setData((byte) (data.getData() + 1));
                }
                
                state.setData(data);
                region.updateBlockState(state);
            }

        }
    }
}