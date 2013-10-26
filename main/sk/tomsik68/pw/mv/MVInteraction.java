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
package sk.tomsik68.pw.mv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import sk.tomsik68.pw.plugin.ProperWeather;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.Core;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

public class MVInteraction {
	private Object man = null;
	private static final MVInteraction instance = new MVInteraction();

	public boolean setup(Server server) {
		if (this.man != null)
			return true;
		try {
			Plugin test = server.getPluginManager()
					.getPlugin("Multiverse-Core");
			if ((test != null) && ((test instanceof Core))) {
				this.man = ((Core) test).getMVWorldManager();
				ProperWeather.log.info("Successfully hooked into Multiverse");
				((MultiverseCore) test).incrementPluginCount();
				return true;
			}
			return false;
		} catch (Exception e) {
		}
		return false;
	}

	public void unSetup(Server server) {
		this.man = null;
		Plugin test = server.getPluginManager().getPlugin("Multiverse-Core");
		if ((test != null) && ((test instanceof Core))) {
			this.man = ((Core) test).getMVWorldManager();
			ProperWeather.log.info("unhooked from Multiverse");
			((MultiverseCore) test).decrementPluginCount();
		}
	}

	public boolean isWeatherOn(String worldName) {
		return ((MVWorldManager) this.man).getMVWorld(worldName)
				.isWeatherEnabled();
	}

	public void setWeather(String worldName, boolean on) {
		((MVWorldManager) this.man).getMVWorld(worldName).setEnableWeather(on);
	}

	public static MVInteraction getInstance() {
		return instance;
	}

	public List<World> getControlledWorlds() {
		Collection<MultiverseWorld> worlds = ((MVWorldManager) this.man).getMVWorlds();
		List<World> result = new ArrayList<World>();
		for (MultiverseWorld mvw : worlds) {
			result.add(mvw.getCBWorld());
		}
		return result;
	}

	public void notifyChange(String world, boolean on) {
		if (this.man != null)
			((MVWorldManager) this.man).getMVWorld(world).setEnableWeather(on);
	}
}