package sk.tomsik68.pw.weather;

import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;
import sk.tomsik68.pw.impl.WeatherController;

public class WeatherDefined extends Weather {
    private final WeatherDefinition defi;
    public static final WeatherDefaults def = new BasicWeatherDefaults(0);

    public WeatherDefined(WeatherDescription wd1, Integer reg, WeatherDefinition d) {
        super(wd1, reg);
        this.defi = d;
    }

    public void doInitWeather() {
        WeatherController wc = getController();
        if (defi.getCloudsColor() != null)
            wc.setCloudsColor(this.defi.getCloudsColor().getRGB());
        wc.setCloudsHeight(this.defi.getCloudsHeight());
        if (defi.getFogColor() != null)
            wc.setFogColor(this.defi.getFogColor().getRGB());
        wc.setMoonSize(this.defi.getMoonSize());
        wc.setRaining(this.defi.isRaining());
        if (defi.getSkyColor() != null)
            wc.setSkyColor(this.defi.getSkyColor().getRGB());
        wc.setStarFrequency(this.defi.getStarFrequency());
        wc.setSunSize(this.defi.getSunSize());
        wc.setClouds(defi.isCloudsVisible());
        wc.setMoon(defi.isMoonVisible());
        wc.setStars(defi.isStars());
        wc.setSun(defi.isSunVisible());
        wc.setThundering(defi.isThunderingAllowed());
    }

    public void onRandomTime() {
    }
}