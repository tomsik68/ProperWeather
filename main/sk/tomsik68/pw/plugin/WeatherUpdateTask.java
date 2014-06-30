package sk.tomsik68.pw.plugin;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import sk.tomsik68.pw.api.WeatherSystem;

public class WeatherUpdateTask implements Runnable {
    private final WeatherSystem weatherSystem;

    public WeatherUpdateTask(WeatherSystem ws) {
        this.weatherSystem = ws;
    }

    public void run() {
        List<World> worlds = Collections.synchronizedList(Bukkit.getServer().getWorlds());
        for (World world : worlds)
            if (this.weatherSystem.isHooked(world))
                this.weatherSystem.update(world);
    }
}