package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.EWeatherRarity;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.weather.element.Wind;

public class WeatherWindy extends Weather{
    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(16000, EWeatherRarity.Normal.getApart(), 75, new String[]{"hot","godanger"},Wind.class.getName());
    private static final Random rand = new Random();
    public WeatherWindy(WeatherDescription wd1, Integer region) {
        super(wd1, region);
    }

    @Override
    public void initWeather() {
        getController().clear();
        getController().activateElement(new Wind(getController()));
    }
    @Override
    public void onRandomTime() {
        Region region = getController().getRegion();
        for(Block block : region){
            if(block == null)
                continue;
            if(block.getType() == Material.LEAVES && rand.nextInt(2367) == 1187){
                BlockState state = block.getState();
                state.setTypeId(0);
                region.updateBlockState(state);
            }
        }
    }

}
