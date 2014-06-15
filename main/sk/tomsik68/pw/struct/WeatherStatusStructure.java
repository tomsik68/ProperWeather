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


public class WeatherStatusStructure {
    public int skyColor;
    public int cloudsColor;
    public int fogColor;
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
        skyColor = 9742079;
        cloudsColor = 0xFFFFFF;
        fogColor = 0xFFFFFF;
        sunSize = 100;
        moonSize = 100;
        starFrequency = 35;
        moonVisible = true;
        cloudsVisible = true;
        sunVisible = true;
        starsVisible = true;
        cloudsHeight = 110;
    }

    public String getHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(skyColor));
        sb.append(Integer.toHexString(cloudsColor));
        sb.append(Integer.toHexString(fogColor));
        sb.append(Integer.toHexString(sunSize));
        sb.append(Integer.toHexString(moonSize));
        sb.append(Integer.toHexString(starFrequency));
        sb.append(Integer.toHexString(cloudsHeight));
        sb.append(moonVisible ? "1" : "0");
        sb.append(cloudsVisible ? "1" : "0");
        sb.append(sunVisible ? "1" : "0");
        sb.append(starsVisible ? "1" : "0");
        return sb.toString();
    }

/*    public WeatherStatusStructure clone() {
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
    }*/

}
