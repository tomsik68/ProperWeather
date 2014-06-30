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