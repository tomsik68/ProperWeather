package sk.tomsik68.pw.api;

public abstract interface WeatherFactory<W extends Weather> {
	public abstract W create(int region);

	public abstract WeatherDefaults getDefaults();
}