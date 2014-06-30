package sk.tomsik68.pw.files.impl.regions;

import java.io.File;
import java.util.ArrayList;

import javax.naming.NameAlreadyBoundException;

import sk.tomsik68.pw.files.api.DataFile;
import sk.tomsik68.pw.region.Region;

public class RegionsDataFile extends DataFile<RegionSaveStructure> {
    private static final RegionSaveStructure empty = new RegionSaveStructure(new ArrayList<Region>());

    public RegionsDataFile(File file) {
        super(file);
        try {
            register(OLD_VERSION_MIGRATOR, new Regions103IO());
        } catch (NameAlreadyBoundException ignored) {
        }
    }

    @Override
    protected int getDefaultIO() {
        return OLD_VERSION_MIGRATOR;
    }

    @Override
    protected RegionSaveStructure getEmptyData() {
        return empty;
    }
}
