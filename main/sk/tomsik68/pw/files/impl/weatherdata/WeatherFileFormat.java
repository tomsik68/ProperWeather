package sk.tomsik68.pw.files.impl.weatherdata;

import java.util.ArrayList;

import sk.tomsik68.pw.files.api.IData;

public class WeatherFileFormat implements IData {
    private final ArrayList<WeatherSaveEntry> data;

    public WeatherFileFormat(ArrayList<WeatherSaveEntry> data) {
        this.data = data;
    }

    public ArrayList<WeatherSaveEntry> getData() {
        return data;
    }
}
