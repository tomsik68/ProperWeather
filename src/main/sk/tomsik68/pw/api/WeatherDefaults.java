package sk.tomsik68.pw.api;

import java.util.HashMap;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract interface WeatherDefaults extends ConfigurationSerializable {
    public abstract int getDefRandomTimeProbability();

    public abstract String[] getDefElements();

    public HashMap<String, Object> getCustomNodes();
}