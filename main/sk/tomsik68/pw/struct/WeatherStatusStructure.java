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
package sk.tomsik68.pw.struct;

import sk.tomsik68.pw.plugin.ProperWeather;

public class WeatherStatusStructure implements Cloneable {
    public java.awt.Color skyColor;
    public java.awt.Color cloudsColor;
    public java.awt.Color fogColor;
    public int sunSize;
    public int moonSize;
    public int starFrequency;
    public boolean moonVisible;
    public boolean cloudsVisible;
    public boolean sunVisible;
    public boolean starsVisible;
    public int cloudsHeight;
    public boolean snowing;
    public boolean thundersAllowed = true;
    public boolean rain = false;

    public WeatherStatusStructure() {
        skyColor = ProperWeather.defaultSkyColor;
        cloudsColor = (fogColor = java.awt.Color.white);
        sunSize = 100;
        moonSize = 100;
        starFrequency = 35;
        moonVisible = true;
        cloudsVisible = true;
        sunVisible = true;
        starsVisible = true;
        cloudsHeight = 110;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof WeatherStatusStructure) {
            WeatherStatusStructure that = (WeatherStatusStructure) obj;
            return (that.cloudsColor.equals(cloudsColor) && that.cloudsVisible == cloudsVisible && that.moonVisible == moonVisible
                    && that.rain == rain && that.snowing == snowing && that.starsVisible == starsVisible && that.sunVisible == sunVisible
                    && that.thundersAllowed == thundersAllowed && that.cloudsHeight == cloudsHeight && that.fogColor.equals(fogColor)
                    && that.moonSize == moonSize && that.skyColor.equals(skyColor) && that.starFrequency == starFrequency && that.sunSize == sunSize);
        }
        return super.equals(obj);
    }

    public WeatherStatusStructure clone() {
        WeatherStatusStructure clone = new WeatherStatusStructure();
        clone.skyColor = skyColor;
        clone.cloudsColor = cloudsColor;
        clone.fogColor = fogColor;
        clone.sunSize = sunSize;
        clone.moonSize = moonSize;
        clone.starFrequency = starFrequency;
        clone.moonVisible = moonVisible;
        clone.cloudsVisible = cloudsVisible;
        clone.snowing = snowing;
        clone.thundersAllowed = thundersAllowed;
        clone.rain = rain;
        return clone;
    }

}
