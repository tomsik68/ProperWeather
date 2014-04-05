/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
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
     * @param previousID
     * @return Whether this weather can be started after previous one. This
     *         option is specified by weather's {@link WeatherDescription}. Devs
     *         can only make defaults.
     */
    public final boolean canBeStarted(String previous) {
        return this.wd.canBeAfter(previous);
        // return this.wd.canBeAfter(previous) &&
        // ((!(getController().getRegion() instanceof BiomeRegion)) ||
        // (getController().getRegion() instanceof BiomeRegion &&
        // wd.getAllowedBiomes().contains(((BiomeRegion)getController().getRegion()).getBiome().name().toLowerCase())));
    }

    /**
     * 
     * @return Random time probability.
     */
    public final int getRandomTimeProbability() {
        if (this.wd == null)
            this.wd = ProperWeather.instance().getWeatherDescription(getClass().getSimpleName().replace("Weather", ""));
        return this.wd.getRandomTimeProbability();
    }

    /**
     * 
     * @return Probability of this weather.
     */
    public final int getProbability() {
        return this.wd.getProbability();
    }

    /**
     * Inits weather (start raining etc.)
     * 
     */
    public abstract void doInitWeather();

    public final void initWeather() {
        List<String> elements = wd.getActiveElements();
        for(String elem : elements){
            BaseWeatherElement element = ProperWeather.instance().getWeatherElementFactories().get(elem).create(getController());
            getController().activateElement(element);
        }
        doInitWeather();
    }

    /**
     * What happens on random time of the weather (if nothing, random time
     * probability should be 0.
     */
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

    public int getMinDuration() {
        return wd.getMinDuration();
    }
}