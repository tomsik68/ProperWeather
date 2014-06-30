package sk.tomsik68.pw.files.impl.regions;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sk.tomsik68.pw.files.api.IDataIO;
import sk.tomsik68.pw.region.Region;

public class Regions103IO implements IDataIO<RegionSaveStructure> {

    @Override
    public RegionSaveStructure load(InputStream in) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(in);
        @SuppressWarnings("unchecked")
        ArrayList<Region> regions = (ArrayList<Region>) ois.readObject();
        return new RegionSaveStructure(regions);
    }

    @Override
    public void save(OutputStream out, RegionSaveStructure data) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(data.getRegions());
    }

}
