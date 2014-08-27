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
