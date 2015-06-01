package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.region.Region;

public final class WeatherStorm extends Weather {
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
		for (BlockState state : region) {
			if (state == null)
				continue;
			if (state.getType() == Material.FIRE && rand.nextInt(10) == 7) {
				state.setType(Material.AIR);
				region.updateBlockState(state);
			}
			if (rand.nextInt(100000) != 0 || state.getType() == Material.SAND)
				continue;
			controller.strike(state.getLocation());

		}
	}
}