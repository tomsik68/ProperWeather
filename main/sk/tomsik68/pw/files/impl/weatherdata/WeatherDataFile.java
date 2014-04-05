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

import java.io.File;
import java.util.ArrayList;

import javax.naming.NameAlreadyBoundException;

import sk.tomsik68.pw.files.api.DataFile;

public class WeatherDataFile extends DataFile<WeatherFileFormat> {
    private static final WeatherFileFormat empty = new WeatherFileFormat(new ArrayList<WeatherSaveEntry>());
    public WeatherDataFile(File file) {
        super(file);
        try {
            register(OLD_VERSION_MIGRATOR, new Weather103IO());
            register(1, new Weather110IO());
        } catch (NameAlreadyBoundException ignore) {
        }
    }

    @Override
    protected int getDefaultIO() {
        return 1;
    }

    @Override
    protected WeatherFileFormat getEmptyData() {
        return empty;
    }

}
