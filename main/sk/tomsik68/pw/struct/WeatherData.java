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
package sk.tomsik68.pw.struct;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import sk.tomsik68.pw.plugin.ProperWeather;

/**
 * Another old, deprecated data structure
 * 
 */
@Deprecated
public class WeatherData implements Externalizable {
    private static final long serialVersionUID = -7099393484035l;

    private LinkedList<Integer> list;
    private int numberOfWeather;
    private int duration;
    private int region;
    private boolean canEverChange;

    public WeatherData() {
        list = new LinkedList<Integer>();
    }

    public void setDuration(int duration1) {
        this.duration = duration1;
    }

    public int getDuration() {
        return duration;
    }

    public void setCanEverChange(boolean canEverChange1) {
        this.canEverChange = canEverChange1;
    }

    public boolean canEverChange() {
        return canEverChange;
    }

    public int decrementDuration() {
        return duration -= ProperWeather.TASK_PERIOD;
    }

    public Integer getPreviousWeather() {
        if (list.size() > 0)
            return list.get(list.size() - 1);
        return 0;
    }

    public List<Integer> getPreviousWeathers() {
        return list;
    }

    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        canEverChange = in.readBoolean();
        duration = in.readInt();
        region = in.readInt();
        numberOfWeather = in.readInt();
        list = ((LinkedList<Integer>) in.readObject());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        throw new NotImplementedException("Don't dare to write the old format!!!");
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region1) {
        this.region = region1;
    }

    @Override
    public String toString() {
        return "WeatherData[region=" + region + " weatherNO=" + numberOfWeather + " canEverChange=" + canEverChange + " duration=" + duration + "]";
    }

    public int getNumberOfWeather() {
        return numberOfWeather;
    }

    public void setNumberOfWeather(int numberOfWeather) {
        this.numberOfWeather = numberOfWeather;
    }
}