package sk.tomsik68.pw.files.impl.weatherdata;

import java.io.File;
import java.util.ArrayList;

import javax.naming.NameAlreadyBoundException;

import sk.tomsik68.pw.files.api.DataFile;

public class WeatherDataFile extends DataFile<WeathersFileFormat> {
    private static final Weathers110Format empty = new Weathers110Format(new ArrayList<WeatherSaveEntry>());

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
    protected Weathers110Format getEmptyData() {
        return empty;
    }

}
