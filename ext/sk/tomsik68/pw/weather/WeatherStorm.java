/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
package sk.tomsik68.pw.weather;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherStorm extends Weather {
    public static final WeatherDefaults def = new BasicWeatherDefaults(3000, 24000, 45, 45, new String[] { "meteorstorm", "storm", "itemrain" });

    public WeatherStorm(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void initWeather() {
        getController().clear();
        getController().setRaining(true);
        getController().allowThundering();
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