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

import sk.tomsik68.pw.region.Region;

public abstract class BaseWeatherCycle implements WeatherCycle {
    protected WeatherSystem weatherSystem;

    public BaseWeatherCycle(WeatherSystem ws) {
        this.weatherSystem = ws;
    }

    public WeatherSystem getWeatherSystem() {
        return this.weatherSystem;
    }

    public abstract Weather nextWeather(Region region);

    public abstract String getName();
}