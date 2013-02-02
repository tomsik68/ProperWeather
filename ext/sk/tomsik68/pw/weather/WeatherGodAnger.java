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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.region.Region;

public class WeatherGodAnger extends Weather {
    @Defaults
    public static final BasicWeatherDefaults def = new BasicWeatherDefaults(36000, 35, 55, new String[] {});
    private final Random rand = new Random();
    private final List<Class<? extends Monster>> monsters = Arrays.asList(Zombie.class,Skeleton.class,Creeper.class,Spider.class);
    public WeatherGodAnger(WeatherDescription wd1, Integer region) {
        super(wd1, region);
    }

    @Override
    public void initWeather() {
        getController().clear();
        getController().allowThundering();
        getController().setRaining(true);
    }

    @Override
    public void onRandomTime() {
        final WeatherController controller = getController();
        final World world = controller.getRegion().getWorld();
        final Region region = controller.getRegion();
        for (Block block : region) {
            if (block == null)
                continue;
            if (rand.nextInt(100000) != 0 || block.getType() == Material.SAND)
                continue;
            int r = rand.nextInt(10);
            if (r == 5) {
                controller.strike(block.getLocation());
                world.createExplosion(block.getLocation(), rand.nextFloat() * 4);
            }else if(r == 7){
                region.spawnEntity(monsters.get(rand.nextInt(monsters.size())), block.getRelative(BlockFace.UP,3).getLocation(), null);
            }
        }
        Player[] players = region.getPlayers();
        for (Player player : players) {
            if (rand.nextInt(137) == 0x7F) {
                getController().strikeEntity(player);
                player.damage(player.getHealth() - 1);
            }
        }

    }

}
