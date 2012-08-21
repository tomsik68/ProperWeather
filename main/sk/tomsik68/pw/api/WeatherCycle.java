package sk.tomsik68.pw.api;

import sk.tomsik68.pw.region.Region;

public abstract interface WeatherCycle {
    public abstract WeatherSystem getWeatherSystem();

    public abstract Weather nextWeather(Region region);
}