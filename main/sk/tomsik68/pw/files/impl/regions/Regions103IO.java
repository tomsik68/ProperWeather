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
