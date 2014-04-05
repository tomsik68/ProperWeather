package sk.tomsik68.pw.impl;

import sk.tomsik68.pw.api.BaseWeatherElement;

public abstract class WeatherElementFactory<W extends BaseWeatherElement> {
    public abstract W create(WeatherController wc);
}
