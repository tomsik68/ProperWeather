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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherMeteorStorm extends Weather {
    private static final Vector velocity = new Vector(0, -4.2d, 0);
    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(18000, 35, 45, new String[] { "clear", "hot", "arrowrain" });

    public WeatherMeteorStorm(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void initWeather() {
        WeatherController controller = getController();
        getController().clear();
        controller.allowThundering();
        controller.setRaining(true);
    }

    public void onRandomTime() {
        WeatherController controller = getController();
        World world = controller.getRegion().getWorld();
        Region region = controller.getRegion();
        Random rand = new Random();
        for (Block block : region) {
            if (block == null)
                continue;
            int x = block.getX();
            int z = block.getZ();
            if (rand.nextInt(1273) == 52) {
                Location loc = new Location(world, x, 128.0D, z);
                region.spawnEntity(Fireball.class, loc, velocity);
            }
            if (block.getType() == Material.FIRE) {
                BlockState state = block.getState();
                state.setType(Material.AIR);
                region.updateBlockState(state);
            }
        }
    }
}