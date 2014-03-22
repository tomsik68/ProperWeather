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
package sk.tomsik68.pw.weather;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;

public class WeatherDefined extends Weather {
    private final WeatherDefinition defi;
    public static final WeatherDefaults def = new BasicWeatherDefaults(0, 0, 0, 0, new String[] { "" });

    public WeatherDefined(WeatherDescription wd1, Integer reg, WeatherDefinition d) {
        super(wd1, reg);
        this.defi = d;
    }

    public void doInitWeather() {
        WeatherController wc = getController();
        wc.setCloudsColor(this.defi.getCloudsColor());
        wc.setCloudsHeight(this.defi.getCloudsHeight());
        wc.setFogColor(this.defi.getFogColor());
        wc.setMoonSize(this.defi.getMoonSize());
        wc.setRaining(this.defi.isRaining());
        wc.setSkyColor(this.defi.getSkyColor());
        wc.setStarFrequency(this.defi.getStarFrequency());
        wc.setSunSize(this.defi.getSunSize());
        wc.setClouds(defi.isCloudsVisible());
        wc.setMoon(defi.isMoonVisible());
        wc.setStars(defi.isStars());
        wc.setSun(defi.isSunVisible());
        wc.setThundering(defi.isThunderingAllowed());
    }

    public void onRandomTime() {
    }
}