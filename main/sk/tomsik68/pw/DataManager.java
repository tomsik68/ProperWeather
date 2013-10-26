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
package sk.tomsik68.pw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.transl.Translator;

public class DataManager {
    public static File dataFile = new File(ProperWeather.instance().getDataFolder(), "data.dat");

    public static <T> void save(T obj) {
        try {
            if (!dataFile.exists())
                dataFile.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile, false));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            ProperWeather.log.severe(Translator.translateString("error.save"));
            e.printStackTrace();
        }
    }

    public static <T> T load() {
        if (!dataFile.exists())
            return null;

        Object result = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
            result = ois.readObject();
            ois.close();
            return (T) result;
        } catch (Exception e) {
            ProperWeather.log.severe(Translator.translateString("error.load"));
            e.printStackTrace();
        }
        return (T) result;
    }
}