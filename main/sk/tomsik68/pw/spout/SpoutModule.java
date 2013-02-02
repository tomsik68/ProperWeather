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
/*    */package sk.tomsik68.pw.spout;

/*    */
/*    */import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import sk.tomsik68.pw.plugin.ProperWeather;

/*    */
/*    */public class SpoutModule
/*    */{
    /*    */private ProperWeather plugin;
    /* 28 */private Map<Integer, MainGui> windows = new HashMap<Integer, MainGui>();

    /*    */
    /* 30 */public SpoutModule(ProperWeather instance) {
        this.plugin = instance;
        /*    */}

    /*    */
    /*    */public void init()
    /*    */{
        /*    */}

    /*    */
    /*    */public static boolean verifyPermission(Player player, String p)
    /*    */{
        /* 39 */return ProperWeather.instance().permissions.has(player, p);
        /*    */}

    /*    */
    /*    */public void openWindow(SpoutPlayer player) {
        /* 43 */if (this.windows.containsKey(player.getUniqueId())) {
            /* 44 */this.windows.get(player.getEntityId()).setScreen(this.plugin, player.getCurrentScreen());
            windows.remove(player.getEntityId());
        }
        /*    */else
            /* 46 */this.windows.put((int) player.getEntityId(), new MainGui(player));
        /*    */}
    /*    */
}

/*
 * Location: C:\Downloads\ProperWeather.jar Qualified Name:
 * sk.tomsik68.pw.spout.SpoutModule JD-Core Version: 0.6.0
 */