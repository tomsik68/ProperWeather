package sk.tomsik68.pw.files.impl.weatherdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sk.tomsik68.pw.files.api.IDataIO;

public class Weather110IO implements IDataIO<WeatherFileFormat> {

    @Override
    public WeatherFileFormat load(InputStream in) throws Exception {
        DataInputStream dis = new DataInputStream(in);
        ArrayList<WeatherSaveEntry> entries = new ArrayList<WeatherSaveEntry>();
        int length = dis.readInt();
        for (int i = 0; i < length; ++i) {
            WeatherSaveEntry entry = new WeatherSaveEntry();
            entry.region = dis.readInt();
            entry.duration = dis.readInt();
            entry.weather = dis.readUTF();
            entry.cycle = dis.readUTF();
            int dataLength = dis.readInt();
            entry.cycleData = new byte[dataLength];
            dis.read(entry.cycleData, 0, dataLength);
            entries.add(entry);
        }
        return new WeatherFileFormat(entries);
    }

    @Override
    public void save(OutputStream out, WeatherFileFormat data) throws Exception {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.getData().size());
        for (WeatherSaveEntry entry : data.getData()) {
            dos.writeInt(entry.region);
            dos.writeInt(entry.duration);
            dos.writeUTF(entry.weather);
            dos.writeUTF(entry.cycle);
            dos.writeInt(entry.cycleData.length);
            dos.write(entry.cycleData);
        }
    }

}
