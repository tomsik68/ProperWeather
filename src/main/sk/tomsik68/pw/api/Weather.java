package sk.tomsik68.pw.api;

import java.util.List;

import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.WeatherController;
import sk.tomsik68.pw.plugin.ProperWeather;

/**
 * The weather.
 * 
 * @author Tomsik68
 * 
 */
public abstract class Weather {
    private int regionID;
    protected WeatherDescription wd;

    public Weather(WeatherDescription wd1, Integer region) {
        this.regionID = region.intValue();
        this.wd = wd1;
    }

    /**
     * 
     * @return {@link WeatherController} to be used by the weather.
     */
    public final WeatherController getController() {
        return ProperWeather.instance().getWeatherSystem().getWeatherController(regionID);
    }

    /**
     * 
     * @return Random time probability.
     */
    public final int getRandomTimeProbability() {
        return wd.getRandomTimeProbability();
    }

    /**
     * Inits weather (start raining etc.)
     * 
     */
    public abstract void doInitWeather();

    public final void initWeather() {
        List<String> elements = wd.getActiveElements();
        for(String elem : elements){
            BaseWeatherElement element = ProperWeather.instance().getWeatherElements().get(elem).create(getController());
            getController().activateElement(element);
        }
        doInitWeather();
        getController().updateAll();
    }

    /**
     * What happens on random time of the weather (if nothing, random time
     * probability should be 0.
     */
    public void onRandomTime() {
    }

    public final String getName() {
        return wd.getName();
    }

    public String toString() {
        return getName();
    }
}