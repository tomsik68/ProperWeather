package sk.tomsik68.pw.api;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract interface WeatherDefaults extends ConfigurationSerializable
{
  public abstract int getDefMaxDuration();

  public abstract String[] getDefCantBeAfter();

  public abstract int getDefRandomTimeProbability();

  public abstract int getDefProbability();
}