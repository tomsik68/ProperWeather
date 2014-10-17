package sk.tomsik68.pw.files.impl.weatherdata;

import java.util.Iterator;
import java.util.List;

import sk.tomsik68.pw.files.api.IData;

public class SavedWeathersFormat implements IData, Iterable<WeatherSaveEntry> {
	private final List<WeatherSaveEntry> entries;

	public SavedWeathersFormat(List<WeatherSaveEntry> list) {
		entries = list;
	}

	@Override
	public Iterator<WeatherSaveEntry> iterator() {
		return entries.iterator();
	}

	public int count() {
		return entries.size();
	}
}
