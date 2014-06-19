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
import java.util.ArrayList;
import java.util.Random;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;

public class RandomWeatherCycle extends WeatherCycle {
    private static final Random rand = new Random();

    public RandomWeatherCycle(WeatherSystem ws, String name) {
        super(ws, name);
    }

    @Override
    public IWeatherData nextWeatherData(IWeatherData wd) {
        // just choose random weather and start it with no rules
        wd.decrementDuration();
        if (wd.getDuration() <= 0) {
            ArrayList<String> weathers = new ArrayList<String>(ProperWeather.instance().getWeathers().getRegistered());

            Weather weather = ProperWeather.instance().getWeathers().createWeather(weathers.get(rand.nextInt(weathers.size())), wd.getRegion());

            weatherSystem.setRegionalWeather(weather, wd.getRegion());

            wd.setDuration(rand.nextInt(36000));

        }
        return wd;
    }

    @Override
    public void loadState(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    @Override
    public void saveState(ObjectOutput out) throws IOException {
    }

}
