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

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
    public static final WeatherDefaults def = new BasicWeatherDefaults(24000, 50, 50, new String[] { "meteorstorm", "storm", "rain", "itemrain" });

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
            if (block.getType() == Material.WATER && rand.nextInt(200) == 57) {
                BlockState state = block.getState();
                state.setData(new MaterialData(0, (byte) 0));
                region.updateBlockState(state);
            }
            if (rand.nextInt(384) == 257 && block.getType() == Material.LEAVES) {
                BlockState state = block.getRelative(BlockFace.UP).getState();
                state.setType(Material.FIRE);
                region.updateBlockState(state);
            }
        }
    }
}