package sk.tomsik68.pw.weather;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherRain extends Weather {
	public static final WeatherDefaults def = new BasicWeatherDefaults(36000,
			50, 50, new String[] { "Hot" });

	public WeatherRain(WeatherDescription wd1, Integer uid) {
		super(wd1, uid);
	}

	public void initWeather() {
		getController().clear();
		getController().setRaining(true);
	}

	public void onRandomTime() {
	    Region region = getController().getRegion();
	    //extinguish all fire
	    for(Block block : region){
	        if (block == null)
                continue;
	        if(block.getType() == Material.FIRE){
	            BlockState state = block.getState();
	            state.setType(Material.AIR);
	            region.updateBlockState(state);
	        }
	    }
	}
}