package sk.tomsik68.pw.impl.factory;

import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;

public abstract class WeatherCycleFactory {
    public abstract WeatherCycle create(WeatherSystem ws);
}
