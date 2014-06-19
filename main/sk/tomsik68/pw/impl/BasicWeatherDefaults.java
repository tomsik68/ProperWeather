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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sk.tomsik68.pw.api.WeatherDefaults;

public class BasicWeatherDefaults implements WeatherDefaults {
    private final int maxDuration, minDuration;
    private final int probability;
    private final int randTimeProbability;
    private final String[] cantBeAfter;
    private final String[] activeElements;
    private HashMap<String, Object> customNodes = new HashMap<String, Object>();

    public BasicWeatherDefaults(int minDuration1, int maxDuration1, int probability1, int randTimeProbability1, String[] cantBeAfter1,
            String... elements) {
        this.minDuration = minDuration1;
        this.maxDuration = maxDuration1;
        this.probability = probability1;
        this.randTimeProbability = randTimeProbability1;
        this.cantBeAfter = cantBeAfter1;
        this.activeElements = elements;
    }

    public int getDefMaxDuration() {
        return this.maxDuration;
    }

    public String[] getDefCantBeAfter() {
        return this.cantBeAfter;
    }

    public int getDefRandomTimeProbability() {
        return this.randTimeProbability;
    }

    public int getDefProbability() {
        return this.probability;
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
    public int getMinDuration() {
        return minDuration;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("probability", probability);
        result.put("max-duration", maxDuration);
        result.put("rand-time-probability", randTimeProbability);
        result.put("cant-be-after", Arrays.asList(cantBeAfter));
        result.put("custom", customNodes);
        result.put("active-elements", activeElements);
        return result;
    }
}