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
package sk.tomsik68.pw.api;

import java.util.List;

import org.bukkit.World;

import sk.tomsik68.pw.region.Region;

public abstract interface WeatherSystem {
    public abstract void startCycle(String cycle, String world, String startWeather);

    public abstract void deInit();

    public abstract void init();

    public abstract void update(World paramWorld);

    public abstract WeatherController getWeatherController(Region paramRegion);

    public abstract boolean canNowBeLightning(Region paramRegion);

    public abstract void setWeatherController(Region paramRegion, WeatherController paramWeatherController);

    public abstract void setWeatherController(int paramInt, WeatherController paramWeatherController);

    public abstract WeatherController getWeatherController(int paramInt);

    public abstract RegionManager getRegionManager();

    public abstract void setRegionManager(RegionManager paramRegionManager);

    public abstract List<String> getWorldList();

    public abstract void setRegionData(Region region, IWeatherData wd);

    public abstract IWeatherData getRegionData(Region region);

    public abstract void startCycleInRegion(String cycleName, int region, String startWeather);

    public abstract void unHook(String worldName);

    public abstract boolean isHooked(World world);
}