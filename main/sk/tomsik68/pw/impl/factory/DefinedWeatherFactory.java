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
package sk.tomsik68.pw.impl.factory;

import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.weather.WeatherDefined;

public class DefinedWeatherFactory implements WeatherFactory<WeatherDefined> {
    private final String name;

    public DefinedWeatherFactory(String weatherName) {
        this.name = weatherName;
    }

    public WeatherDefined create(int region) {
        WeatherDefined result = new WeatherDefined(ProperWeather.instance().getWeatherDescription(this.name), region, ProperWeather.instance().getWeatherDefinition(this.name));
        return result;
    }

    public WeatherDefaults getDefaults() {
        return WeatherDefined.def;
    }
}