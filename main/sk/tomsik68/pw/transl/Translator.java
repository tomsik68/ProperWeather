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
package sk.tomsik68.pw.transl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import sk.tomsik68.pw.plugin.ProperWeather;

public class Translator {
    private final Properties properties;
    private static Translator instance;

    public static void init(String s) {
        instance = new Translator(s);
    }

    private Translator(String path) {
        if (instance == null)
            instance = this;
        properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            System.out.println("[ProperWeather] Localisation file not found. Defaulting to built-in");
            try {
                properties.load(Translator.class.getResourceAsStream("en.txt"));
                try {
                    System.out.println("[ProperWeather] Extracting built-in localisation file...");
                    InputStream is = Translator.class.getResourceAsStream("en.txt");
                    File dest = new File(ProperWeather.instance().getDataFolder(), "en.txt");
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    is.close();
                    FileOutputStream fos = new FileOutputStream(dest);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                    System.out.println("[ProperWeather] Extracting complete.");
                } catch (Exception e2) {
                    System.out.println("[ProperWeather] Extraction error:");
                    e2.printStackTrace();
                }
            } catch (Exception e1) {
                System.out.println("[ProperWeather] FATAL ERROR: Can't find built-in localisation file. No messages... :(");
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String translateString(String key, Object... params) {
        return instance.translate(key, params);
    }

    public String translate(String key, Object[] params) {
        String result = properties.getProperty(key);
        if (params != null) {
            int len = params.length;
            for (int i = 0; i < len; i++) {
                if (params[i] != null)
                    result = result.replace("{" + i + "}", ProperWeather.factColor + params[i].toString() + ProperWeather.color);
            }
        }
        return result;
    }
}