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
import java.util.List;
import java.util.Random;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.config.EOrder;
import sk.tomsik68.pw.plugin.ProperWeather;

public class YAMLWeatherCycle extends WeatherCycle {
    private final List<WeatherSpec> specs;
    private int last = 0;
    private final boolean stop;
    private final EOrder order;
    private final Random rand = new Random();

    public YAMLWeatherCycle(WeatherSystem ws, boolean stop, EOrder order, String name, List<WeatherSpec> specs) {
        super(ws, name);
        this.stop = stop;
        this.order = order;
        this.specs = specs;
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
                    WeatherSpec spec = specs.get(rand.nextInt(specs.size()));
                    if ((rand.nextInt(100) < spec.getProbability())) {
                        Weather weather = ProperWeather.instance().getWeathers().createWeather(spec.getWeatherName(), wd.getRegion());

                        weatherSystem.setRegionalWeather(weather, wd.getRegion());

                        wd.setDuration(spec.getMinDuration() + rand.nextInt(spec.getMaxDuration() - spec.getMinDuration()));
                        done = true;
                    }
                }
                break;
            case SPECIFIED:
                WeatherSpec spec = specs.get(last++);
                Weather weather = ProperWeather.instance().getWeathers().createWeather(spec.getWeatherName(), wd.getRegion());
                if (last == specs.size())
                    last = 0;

                weatherSystem.setRegionalWeather(weather, wd.getRegion());

                wd.setDuration(spec.getMinDuration() + rand.nextInt(spec.getMaxDuration() - spec.getMinDuration()));
                break;
            }
        }
        return wd;
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
