package sk.tomsik68.pw.transl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import sk.tomsik68.pw.plugin.ProperWeather;

public final class Translator {
	private final Properties file, defaultFile;

	private static Translator instance;

	public static void init(String s, InputStream resource) {
		instance = new Translator(s, resource);
	}

	private Translator(String path, InputStream resource) {
		if (instance == null)
			instance = this;
		file = new Properties();
		defaultFile = new Properties();
		try {
			file.load(new FileInputStream(path));
			defaultFile.load(resource);
		} catch (FileNotFoundException e) {
			ProperWeather.log.info("Localisation file not found. Defaulting to built-in");
			try {
				file.load(resource);
				try {
					ProperWeather.log.fine("Extracting built-in localisation file...");
					File dest = new File(ProperWeather.instance().getDataFolder(), "en.txt");
					byte[] bytes = new byte[resource.available()];
					resource.read(bytes);
					resource.close();
					FileOutputStream fos = new FileOutputStream(dest);
					fos.write(bytes);
					fos.flush();
					fos.close();
					ProperWeather.log.fine("Extracting complete.");
				} catch (Exception e2) {
					ProperWeather.log.severe("Extraction error:");
					e2.printStackTrace();
				}
			} catch (Exception e1) {
				ProperWeather.log.severe("FATAL ERROR: Can't find built-in localisation file. No messages... :(");
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
		String result = key;
		if (file.containsKey(key)) {
			result = file.getProperty(key);
		} else
			result = defaultFile.getProperty(key);

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