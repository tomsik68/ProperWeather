package sk.tomsik68.pw.impl;

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