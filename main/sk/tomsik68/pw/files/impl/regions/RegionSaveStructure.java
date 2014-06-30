package sk.tomsik68.pw.files.impl.regions;

import java.util.ArrayList;

import sk.tomsik68.pw.files.api.IData;
import sk.tomsik68.pw.region.Region;

public class RegionSaveStructure implements IData {
    private final ArrayList<Region> regions;

    public RegionSaveStructure(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

}
