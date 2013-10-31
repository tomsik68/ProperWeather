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

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherFactory;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;

public class ClassWeatherFactory<W extends Weather> implements WeatherFactory<W> {
    private final Class<W> clazz;

    public ClassWeatherFactory(Class<W> clazz1) {
        this.clazz = clazz1;
    }

    public W create(Object... args) {
        if ((args != null) && (args.length >= 1) && (args[0] != null) && ((args[0] instanceof Integer))) {
            try {
                return clazz.getConstructor(new Class[] { WeatherDescription.class, Integer.class }).newInstance(new Object[] { ProperWeather.instance().getWeatherDescription(clazz.getSimpleName().replace("Weather", "")), args[0] });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public WeatherDefaults getDefaults() {
        try {
            return Util.getWeatherDefaults(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't get WeatherDefaults from " + clazz.getName(), e);
        }
    }
}