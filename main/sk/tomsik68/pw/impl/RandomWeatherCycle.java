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
package sk.tomsik68.pw.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

import sk.tomsik68.pw.WeatherManager;
import sk.tomsik68.pw.api.BaseWeatherCycle;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.struct.WeatherData;

public class RandomWeatherCycle extends BaseWeatherCycle {
    private static final Random rand = new Random();

    public RandomWeatherCycle(WeatherSystem ws) {
        super(ws);
    }

    @Override
    public Weather nextWeather(Region region) {
        WeatherData wd = weatherSystem.getRegionData(region);
        Weather weather = WeatherManager.randomWeather(region);
        if (!weather.canBeStarted(wd.getPreviousWeather()))
            return nextWeather(region);
        if ((WeatherManager.getUID(weather.getClass().getSimpleName()) != wd.getPreviousWeather().intValue()) && (!wd.wasWeather(weather)) && (rand.nextInt(100) < weather.getProbability())) {
            weather.initWeather();
            wd.setDuration(rand.nextInt(weather.getMaxDuration()));
            weatherSystem.setRegionData(region, wd);
            return weather;
        }
        return nextWeather(region);
    }

    @Override
    public void readExternal(ObjectInput arg0) throws IOException, ClassNotFoundException {
    }

    @Override
    public void writeExternal(ObjectOutput arg0) throws IOException {
    }

}
