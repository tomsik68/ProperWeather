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
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
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
