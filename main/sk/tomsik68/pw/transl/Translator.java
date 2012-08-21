package sk.tomsik68.pw.transl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import sk.tomsik68.pw.plugin.ProperWeather;

public class Translator {
    private final Properties properties;
    public static Translator instance;

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
            System.out
                    .println("[ProperWeather] Localisation file not found. Defaulting to built-in");
            try {
                properties.load(Translator.class
                        .getResourceAsStream("/en.txt"));
            } catch (Exception e1) {
                System.out.println("[ProperWeather] FATAL ERROR: Can't find built-in localisation file. No messages... :(");
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
                    result = result.replace("{" + i + "}",
                            ProperWeather.factColor + params[i].toString()
                                    + ProperWeather.color);
            }
        }
        return result;
    }
}