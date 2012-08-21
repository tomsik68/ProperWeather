package sk.tomsik68.pw.api;

public abstract interface WeatherFactory<W extends Weather> {
	public abstract W create(Object[] params);

	public abstract WeatherDefaults getDefaults();
}