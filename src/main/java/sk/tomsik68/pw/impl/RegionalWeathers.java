package sk.tomsik68.pw.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

import sk.tomsik68.pw.api.IWeatherData;
import sk.tomsik68.pw.api.Weather;
import sk.tomsik68.pw.api.WeatherCycle;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.files.api.DataFile;
import sk.tomsik68.pw.files.impl.weatherdata.SavedWeathersFormat;
import sk.tomsik68.pw.files.impl.weatherdata.WeatherSaveEntry;
import sk.tomsik68.pw.impl.registry.WeatherCycleFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.struct.WeatherDatav5;

/**
 * This class only takes care of holding weather situation data & keeping them
 * consistent
 * 
 * @author Tomsik68
 *
 */
final class RegionalWeathers {
	private Map<Integer, IWeatherData> weatherData;

	RegionalWeathers() {
		weatherData = new HashMap<Integer, IWeatherData>();
	}

	void updateSituation(IWeatherData wd) {
		Validate.notNull(wd);
		if (weatherData.containsKey(wd.getRegion()))
			remove(wd.getRegion());
		weatherData.put(wd.getRegion(), wd);
	}

	IWeatherData getSituation(int uid) {
		return weatherData.get(uid);
	}

	void remove(int r) {
		weatherData.remove(r);
	}

	IWeatherData createDefaultWeatherData() {
		WeatherDatav5 wd = new WeatherDatav5();
		return wd;
	}

	void loadFrom(WeatherSystem parent, DataFile<SavedWeathersFormat> dataFile, WeatherFactoryRegistry weathers, WeatherCycleFactoryRegistry cycles) throws Exception {
		SavedWeathersFormat entries = dataFile.loadData();
		for (WeatherSaveEntry entry : entries) {
			// duration, cycle, weather, region, cycleData
			IWeatherData wd = convertSaveEntry(parent, entry, weathers, cycles);
			updateSituation(wd);
			wd.getCurrentWeather().initWeather();
		}

	}

	private IWeatherData convertSaveEntry(WeatherSystem parent, WeatherSaveEntry entry, WeatherFactoryRegistry weathers, WeatherCycleFactoryRegistry cycles) {
		IWeatherData wd = createDefaultWeatherData();
		wd.setDuration(entry.duration);
		wd.setRegion(entry.region);
		Weather weather = weathers.createWeather(entry.weather, entry.region);
		wd.setCurrentWeather(weather);
		WeatherCycle cycle = cycles.get(entry.cycle).create(parent);
		wd.setCycle(cycle);
		return wd;
	}

	void save(DataFile<SavedWeathersFormat> dataFile) throws Exception {
		ArrayList<WeatherSaveEntry> toSave = new ArrayList<WeatherSaveEntry>();
		for (Entry<Integer, IWeatherData> entry : weatherData.entrySet()) {
			WeatherSaveEntry save = new WeatherSaveEntry();
			save.duration = entry.getValue().getDuration();
			save.region = entry.getValue().getRegion();
			save.weather = entry.getValue().getCurrentWeather().getName();
			save.cycle = entry.getValue().getCycle().getName();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			entry.getValue().getCycle().saveState(oos);
			oos.flush();
			baos.flush();
			baos.close();
			oos.close();
			save.cycleData = baos.toByteArray();
			toSave.add(save);
		}
		SavedWeathersFormat saveStruct = new SavedWeathersFormat(toSave);
		dataFile.saveData(saveStruct);
	}
}