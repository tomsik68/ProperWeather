package sk.tomsik68.pw.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;

public class StoppedWeatherCycle extends WeatherCycle {

    public StoppedWeatherCycle(WeatherSystem ws, String name) {
        super(ws, name);
    }

    @Override
    public IWeatherData nextWeatherData(IWeatherData current) {
        return current;
    }

    @Override
    public void loadState(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    @Override
    public void saveState(ObjectOutput out) throws IOException {
    }

}
