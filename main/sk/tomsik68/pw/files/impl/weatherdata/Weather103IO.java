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
package sk.tomsik68.pw.files.impl.weatherdata;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import sk.tomsik68.pw.Util;
import sk.tomsik68.pw.files.api.IDataIO;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.struct.WeatherData;
import sk.tomsik68.pw.struct.WeatherDataExt;
import sk.tomsik68.pw.struct.WeatherDatav4;

public class Weather103IO implements IDataIO<WeatherFileFormat> {

    @SuppressWarnings("deprecation")
    @Override
    public WeatherFileFormat load(InputStream in) throws Exception {
        ArrayList<WeatherSaveEntry> entries = new ArrayList<WeatherSaveEntry>();
        ObjectInputStream ois = new ObjectInputStream(in);
        List<?> loadedList = (List<?>) ois.readObject();

        if ((loadedList != null) && (loadedList.size() > 0)) {
            Object test = loadedList.get(0);
            if ((test instanceof WeatherData)) {
                ProperWeather.log.fine("Detected v2 data file. Converting...");
                HashMap<Integer, String> oldWeatherMap = Util.generateOLDIntWeatherLookupMap();
                for (Object obj : loadedList) {
                    WeatherData wd = (WeatherData) obj;
                    if (wd != null) {
                        WeatherSaveEntry entry = new WeatherSaveEntry();
                        entry.duration = wd.getDuration();
                        entry.region = wd.getRegion();
                        entry.weather = oldWeatherMap.get(wd.getNumberOfWeather());
                        entry.cycle = wd.canEverChange() ? "random" : "stop";
                        entries.add(entry);
                    }
                }
                ProperWeather.log.fine("Conversion finished.");
            } else if (test instanceof WeatherDataExt) {
                ProperWeather.log.fine("Detected v3 data file. Converting...");
                for (Object obj : loadedList) {
                    WeatherDataExt oldWD = (WeatherDataExt) obj;
                    if (oldWD != null) {
                        WeatherSaveEntry entry = new WeatherSaveEntry();
                        entry.cycle = oldWD.canEverChange() ? "random" : "stop";
                        entry.duration = oldWD.getDuration();
                        entry.region = oldWD.getRegion();
                        entry.weather = oldWD.sCurrentWeather;
                        entries.add(entry);
                    }
                }
                ProperWeather.log.fine("Conversion finished.");
            } else if (test instanceof WeatherDatav4) {
                ProperWeather.log.fine("Detected v4 data file. Converting...");
                for (Object obj : loadedList) {
                    WeatherDatav4 wd = (WeatherDatav4) obj;
                    if (wd != null) {
                        WeatherSaveEntry entry = new WeatherSaveEntry();
                        entry.weather = wd.weather;
                        entry.cycle = wd.cycleName;
                        entry.duration = wd.getDuration();
                        entry.region = wd.getRegion();
                        entries.add(entry);
                    }
                }
                ProperWeather.log.fine("Conversion finished.");
            } else
                ProperWeather.log.severe("Detected corrupted/incompatible save file. Class=" + test.getClass());
        }
        return new WeatherFileFormat(entries);
    }

    @Override
    public void save(OutputStream out, WeatherFileFormat data) throws Exception {
        throw new NotImplementedException("Don't use this format to save data!");
    }

}
