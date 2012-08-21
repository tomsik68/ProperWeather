package sk.tomsik68.pw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.transl.Translator;

public class DataManager {
	public static File dataFile = new File(ProperWeather.instance()
			.getDataFolder(), "data.dat");

	public static <T> void save(T obj) {
		try {
			if (!dataFile.exists())
				dataFile.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(dataFile, false));
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println(Translator.instance.translate("error.save",
					new Object[] { "" }));
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T load() {
		if (!dataFile.exists())
			return null;
		Object result = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					dataFile));
			result = ois.readObject();
			ois.close();
			return (T) result;
		} catch (Exception e) {
			System.out.println(Translator.instance.translate("error.load",
					new Object[] { "" }));
			e.printStackTrace();
		}
		return (T) result;
	}
}