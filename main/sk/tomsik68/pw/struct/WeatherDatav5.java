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
package sk.tomsik68.pw.struct;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherDatav5 implements IWeatherData {
    private Weather currentWeather;
    private int duration;
    private int region;
    private WeatherCycle cycle;

    @Override
    public Weather getCurrentWeather() {
        return currentWeather;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int decrementDuration() {
        return duration -= ProperWeather.TASK_PERIOD;
    }

    @Override
    public int getRegion() {
        return region;
    }

    @Override
    public WeatherCycle getCycle() {
        return cycle;
    }

    @Override
    public void setRegion(int regionID) {
        this.region = regionID;
    }

    @Override
    public void setCycle(WeatherCycle c) {
        this.cycle = c;
    }

    @Override
    public void setCurrentWeather(Weather w) {
        this.currentWeather = w;
    }

    @Override
    public void setDuration(int d) {
        duration = d;
    }
}
