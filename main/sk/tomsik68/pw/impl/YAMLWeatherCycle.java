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
import java.util.List;
import java.util.Random;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.config.EOrder;
import sk.tomsik68.pw.plugin.ProperWeather;

public class YAMLWeatherCycle extends WeatherCycle {
    private final List<String> weathers;
    private int last = 0;
    private final boolean stop;
    private final EOrder order;
    private final Random rand = new Random();
    private LinkedList<String> previousWeathers = new LinkedList<String>();

    public YAMLWeatherCycle(WeatherSystem ws, boolean stop, EOrder order, String name, List<String> weathers) {
        super(ws, name);
        this.stop = stop;
        this.order = order;
        this.weathers = weathers;
    }

    @Override
    public IWeatherData nextWeatherData(IWeatherData wd) {
        if (stop)
            return wd;
        wd.decrementDuration();
        if (wd.getDuration() <= 0) {
            switch (order) {
            case RANDOM:
                // recursity was removed, using while instead...
                boolean done = false;
                while (!done) {
                    Weather weather = ProperWeather.instance().getWeathers()
                            .createWeather(weathers.get(rand.nextInt(weathers.size())), wd.getRegion());
                    if (weather.canBeStarted(getPreviousWeather()) && !weather.getName().equalsIgnoreCase(getPreviousWeather())
                            && (!wasWeather(weather)) && (rand.nextInt(100) < weather.getProbability())) {
                        addPrevWeather(weather.getName());

                        weatherSystem.setRegionalWeather(weather, wd.getRegion());

                        wd.setDuration(weather.getMinDuration() + rand.nextInt(weather.getMaxDuration() - weather.getMinDuration()));
                        done = true;
                    }
                }
                break;
            case SPECIFIED:
                Weather weather = ProperWeather.instance().getWeathers().createWeather(weathers.get(last++), wd.getRegion());
                if (last == weathers.size())
                    last = 0;
                weatherSystem.setRegionalWeather(weather, wd.getRegion());
                wd.setDuration(weather.getMinDuration() + rand.nextInt(weather.getMaxDuration() - weather.getMinDuration()));
                break;
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

    @Override
    public void loadState(ObjectInput in) throws IOException, ClassNotFoundException {
        last = in.readInt();
    }

    @Override
    public void saveState(ObjectOutput out) throws IOException {
        out.writeInt(last);
    }

}
