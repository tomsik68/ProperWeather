package sk.tomsik68.pw.files.impl.weatherdata;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.files.api.IData;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.struct.WeatherDatav5;

public class Weathers110Format implements IData, WeathersFileFormat {
    private final ArrayList<WeatherSaveEntry> data;

    public Weathers110Format(ArrayList<WeatherSaveEntry> data) {
        this.data = data;
    }

    public ArrayList<WeatherSaveEntry> getWDList() {
        return data;
    }

    @Override
    public void loadDataToWS(WeatherSystem ws) throws Exception {
        for (WeatherSaveEntry entry : data) {
            try {
                ws.getRegionManager().getRegion(entry.region).getWorld();
            } catch (Exception e) {
                continue;
            }
            IWeatherData wd = new WeatherDatav5();
            wd.setDuration(entry.duration);
            wd.setRegion(entry.region);

            WeatherCycle cycle = ProperWeather.instance().getCycles().get(entry.cycle).create(ws);
            if (entry.cycleData != null && entry.cycleData.length > 0) {
                cycle.loadState(new ObjectInputStream(new ByteArrayInputStream(entry.cycleData)));
            }
            wd.setCycle(cycle);
            wd.setCurrentWeather(ProperWeather.instance().getWeathers().get(entry.weather).create(entry.region));
            wd.getCurrentWeather().initWeather();
            ws.getWeatherController(entry.region).updateAll();
            ws.setRegionData(ws.getRegionManager().getRegion(entry.region), wd);
        }
    }
}
