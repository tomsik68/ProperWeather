package sk.tomsik68.pw.api;

public interface IWeatherData {
    public Weather getCurrentWeather();

    public int getDuration();

    public int decrementDuration();

    public int getRegion();

    public WeatherCycle getCycle();

    public void setRegion(int regionID);

    public void setCycle(WeatherCycle randomWeatherCycle);

    public void setCurrentWeather(Weather w);

    public void setDuration(int d);
}
