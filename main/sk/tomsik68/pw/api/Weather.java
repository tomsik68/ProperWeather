package sk.tomsik68.pw.api;

import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.plugin.ProperWeather;

public abstract class Weather implements Cloneable {
    private int regionID;
    protected WeatherDescription wd;

    public Weather(WeatherDescription wd1, Integer region) {
        this.regionID = region.intValue();
        this.wd = wd1;
    }

    public final WeatherController getController() {
        return ProperWeather.instance().getWeatherSystem().getWeatherController(regionID);
    }

    public final boolean canBeStarted(Integer previousID) {
        return this.wd.canBeAfter(previousID.intValue());
    }

    public final int getRandomTimeProbability() {
        if (this.wd == null)
            this.wd = ProperWeather.instance().getWeatherDescription(getClass().getSimpleName().replace("Weather", ""));
        return this.wd.getRandomTimeProbability();
    }

    public final int getProbability() {
        return this.wd.getProbability();
    }

    public abstract void initWeather();

    public void onRandomTime() {
    }

    public final int getMaxDuration() {
        return this.wd.getMaxDuration();
    }

    public final String getName() {
        return this.wd.getName();
    }

    public String toString() {
        return getName();
    }
}