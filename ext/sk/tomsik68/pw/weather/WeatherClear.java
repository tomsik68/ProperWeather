package sk.tomsik68.pw.weather;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;

public class WeatherClear extends Weather {
    public static final WeatherDefaults def = new BasicWeatherDefaults(50);

    public WeatherClear(WeatherDescription wd1, Integer uid) {
        super(wd1, uid);
    }

    public void onRandomTime() {
    }

    @Override
    public void doInitWeather() {
        
    }

}