package sk.tomsik68.pw.impl;

import java.util.Map;

public class WeatherSpec {
    private final Map<String, Object> map;

    public WeatherSpec(Map<String, Object> map) {
        this.map = map;
    }

    public int getProbability() {
        return (Integer) map.get("probability");
    }

    public int getMinDuration() {
        return (Integer) map.get("min-duration");
    }

    public String getWeatherName() {
        return map.get("weather").toString();
    }

    public int getMaxDuration() {
        return (Integer) map.get("max-duration");
    }

    public Object getProperty(String key) {
        return map.get(key);
    }
}
