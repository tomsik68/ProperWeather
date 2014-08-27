package sk.tomsik68.pw.files.impl.weatherdata;

import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.files.api.IData;

public interface WeathersFileFormat extends IData {
    public void loadDataToWS(WeatherSystem ws) throws Exception;
}
