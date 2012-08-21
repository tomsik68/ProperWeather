package sk.tomsik68.pw.weather;

import sk.tomsik68.pw.Defaults;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherController;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.BasicWeatherDefaults;

public class WeatherDefined extends Weather {
    private final WeatherDefinition defi;

    @Defaults
    public static final WeatherDefaults def = new BasicWeatherDefaults(0, 0, 0, new String[] { "" });

    public WeatherDefined(WeatherDescription wd1, Integer reg, WeatherDefinition d) {
        super(wd1, reg);
        this.defi = d;
    }

    public void initWeather() {
        WeatherController wc = getController();
        wc.setCloudsColor(this.defi.getCloudsColor());
        wc.setCloudsHeight(this.defi.getCloudsHeight());
        wc.setFogColor(this.defi.getFogColor());
        wc.setMoonSize(this.defi.getMoonSize());
        wc.setRaining(this.defi.isRaining());
        wc.setSkyColor(this.defi.getSkyColor());
        wc.setStarFrequency(this.defi.getStarFrequency());
        wc.setSunSize(this.defi.getSunSize());
        if (this.defi.isCloudsVisible())
            wc.showClouds();
        else
            wc.hideClouds();
        if (this.defi.isMoonVisible())
            wc.showMoon();
        else
            wc.hideMoon();
        if (this.defi.isStars())
            wc.showStars();
        else
            wc.hideStars();
        if (this.defi.isSunVisible())
            wc.showSun();
        else
            wc.hideSun();
        if (this.defi.isThunderingAllowed())
            wc.allowThundering();
        else
            wc.denyThundering();
    }

    public void onRandomTime() {
    }
}