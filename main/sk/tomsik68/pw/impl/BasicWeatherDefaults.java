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
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/package sk.tomsik68.pw.impl;

import java.util.HashMap;
import java.util.Map;

import sk.tomsik68.pw.api.WeatherDefaults;

public class BasicWeatherDefaults implements WeatherDefaults {
    private final int randTimeProbability;
    private final String[] activeElements;
    private HashMap<String, Object> customNodes = new HashMap<String, Object>();

    public BasicWeatherDefaults(int randTimeProbability1, String... elements) {
        this.randTimeProbability = randTimeProbability1;
        this.activeElements = elements;
    }

    public int getDefRandomTimeProbability() {
        return this.randTimeProbability;
    }

    @Override
    public String[] getDefElements() {
        return activeElements;
    }

    @Override
    public HashMap<String, Object> getCustomNodes() {
        return customNodes;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rand-time-probability", randTimeProbability);
        result.put("custom", customNodes);
        result.put("active-elements", activeElements);
        return result;
    }
}