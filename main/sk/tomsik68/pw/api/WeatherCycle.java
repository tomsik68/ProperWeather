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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class WeatherCycle {
    protected final WeatherSystem weatherSystem;
    private final String name;

    public WeatherCycle(WeatherSystem ws, String name) {
        this.weatherSystem = ws;
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public abstract IWeatherData nextWeatherData(IWeatherData current);

    public abstract void loadState(ObjectInput in) throws IOException, ClassNotFoundException;

    public abstract void saveState(ObjectOutput out) throws IOException;
}