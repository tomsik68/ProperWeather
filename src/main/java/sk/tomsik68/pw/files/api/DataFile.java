package sk.tomsik68.pw.files.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.lang.Validate;

import sk.tomsik68.pw.plugin.ProperWeather;

public abstract class DataFile<D extends IData> extends IntRegistry<IDataIO<D>> {
	protected final int OLD_VERSION_MIGRATOR = -1;
	private final File file;

	public DataFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public D loadData() throws Exception {
		if (!file.exists()) {
			return getEmptyData();
		}
		FileInputStream fis;
		DataInputStream dis = new DataInputStream(fis = new FileInputStream(file));
		int version = dis.readInt();
		IDataIO<D> io = get(version);
		D data = null;
		if (io == null) {
			dis.close();
			io = get(OLD_VERSION_MIGRATOR);
			if (io != null) {
				FileInputStream in = new FileInputStream(file);
				data = io.load(in);
				in.close();
			} else {
				ProperWeather.log.warning("Unknown data format. Please contact author to fix it... [VERSION=" + version + "]");
			}
		} else {
			data = io.load(dis);
		}
		dis.close();
		fis.close();
		return data;
	}

	protected abstract D getEmptyData();

	protected abstract int getDefaultIO();

	public void saveData(D data) throws Exception {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		} else {
			file.delete();
		}
		file.createNewFile();
		int version = getDefaultIO();
		IDataIO<D> io = get(version);
		Validate.notNull(io);
		FileOutputStream fos = new FileOutputStream(file, false);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeInt(version);
		io.save(fos, data);
		fos.flush();
		fos.close();
	}

}
