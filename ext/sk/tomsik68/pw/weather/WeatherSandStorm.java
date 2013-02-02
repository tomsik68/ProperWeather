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
import sk.tomsik68.pw.weather.element.Wind;

public class WeatherSandStorm extends Weather {
    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 50, 50, new String[] { "meteorstorm","storm","rain","itemrain" }, Wind.class.getName());

    public WeatherSandStorm(WeatherDescription wd, Integer region) {
        super(wd, region);
    }

    public void initWeather() {
        getController().clear();
        getController().activateElement(new Wind(getController()));
    }

    public void onRandomTime() {
        Region region = getController().getRegion();
        World world = region.getWorld();
        Random random = new Random();
        for (Block block : region) {
            if (block == null)
                continue;
            if (random.nextInt(400) == 1) {
                int blocksUp = 1+random.nextInt(2);
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