package sk.tomsik68.pw.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sk.tomsik68.pw.api.WeatherDefaults;

public class BasicWeatherDefaults implements WeatherDefaults {
	private final int maxDuration;
	private final int probability;
	private final int randTimeProbability;
	private final String[] cantBeAfter;

	public BasicWeatherDefaults(int maxDuration1, int probability1,
			int randTimeProbability1, String[] cantBeAfter1) {
		this.maxDuration = maxDuration1;
		this.probability = probability1;
		this.randTimeProbability = randTimeProbability1;
		this.cantBeAfter = cantBeAfter1;
	}

	public int getDefMaxDuration() {
		return this.maxDuration;
	}

	public String[] getDefCantBeAfter() {
		return this.cantBeAfter;
	}

	public int getDefRandomTimeProbability() {
		return this.randTimeProbability;
	}

	public int getDefProbability() {
		return this.probability;
	}

    @Override
    public Map<String, Object> serialize() {
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("probability", probability);
        result.put("max-duration", maxDuration);
        result.put("rand-time-probability", randTimeProbability);
        result.put("cant-be-after", Arrays.asList(cantBeAfter));
        result.put("customs", Arrays.asList(""));
        return result;
    }
}