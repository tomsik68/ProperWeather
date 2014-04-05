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
import java.util.LinkedList;
import java.util.Random;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;

public class RandomWeatherCycle extends WeatherCycle {
    private static final Random rand = new Random();
    private LinkedList<String> previousWeathers = new LinkedList<String>();

    public RandomWeatherCycle(WeatherSystem ws, String name) {
        super(ws, name);
    }

    @Override
    public IWeatherData nextWeatherData(IWeatherData wd) {
        if (wd.decrementDuration() <= 0) {
            ArrayList<String> weathers = new ArrayList<String>(ProperWeather.instance().getWeathers().getRegistered());
            // recursity was removed, using while instead...
            boolean done = false;
            while (!done) {
                Weather weather = ProperWeather.instance().getWeathers().createWeather(weathers.get(rand.nextInt(weathers.size())), wd.getRegion());
                if (weather.canBeStarted(getPreviousWeather()) && !weather.getName().equalsIgnoreCase(getPreviousWeather()) && (!wasWeather(weather))
                        && (rand.nextInt(100) < weather.getProbability())) {
                    addPrevWeather(weather.getName());
                    wd.setCurrentWeather(weather);
                    weather.initWeather();
                    wd.setDuration(weather.getMinDuration() + rand.nextInt(weather.getMaxDuration() - weather.getMinDuration()));
                    done = true;
                }
            }
        }
        return wd;
    }

    private void addPrevWeather(String name) {
        previousWeathers.add(name);
        if (previousWeathers.size() < 3) {
            previousWeathers.removeFirst();
        }

    }

    private boolean wasWeather(Weather weather) {
        return previousWeathers.contains(weather.getName());
    }

    private String getPreviousWeather() {
        if (previousWeathers.size() == 0)
            return "";
        else
            return previousWeathers.get(previousWeathers.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadState(ObjectInput in) throws IOException, ClassNotFoundException {
        previousWeathers = (LinkedList<String>) in.readObject();
    }

    @Override
    public void saveState(ObjectOutput out) throws IOException {
        out.writeObject(previousWeathers);
    }

}
