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
package sk.tomsik68.pw.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.NameAlreadyBoundException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.WeatherInfoManager;
import sk.tomsik68.pw.api.BiomeMapperManager;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.command.CommandHandler;
import sk.tomsik68.pw.command.PWCommand;
import sk.tomsik68.pw.config.ConfigFile;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.DefaultBiomeMapperManager;
import sk.tomsik68.pw.impl.DefaultWeatherSystem;
import sk.tomsik68.pw.impl.factory.DefinedWeatherFactory;
import sk.tomsik68.pw.impl.registry.ServerBackendMatcherRegistry;
import sk.tomsik68.pw.impl.registry.WeatherCycleFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherElementFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.transl.Translator;

public class ProperWeather extends JavaPlugin {
    public static Logger log;
    public static boolean isSpout = true;

    public static final int TASK_PERIOD = 88;

    private PWWeatherListener weatherListener;
    private WeatherSystem weatherSystem;
    private PWPlayerListener playerListener;

    public EPermissions permissions;

    private ConfigFile config;
    private FileConfiguration weatherSettings = null;

    public static ChatColor factColor = ChatColor.GRAY;
    public static ChatColor color = ChatColor.GREEN;

    private WeatherInfoManager wim = new WeatherInfoManager();

    private int weatherUpdateTask;
    private int regionUpdateTask;

    private final BiomeMapperManager mapperManager = new DefaultBiomeMapperManager();

    private WeatherFactoryRegistry weatherFactoryRegistry;
    private WeatherCycleFactoryRegistry weatherCycleFactoryRegistry;
    private WeatherElementFactoryRegistry weatherElementFactoryRegistry;
    private ServerBackendMatcherRegistry serverBackendMatcherRegistry;
    private IServerBackend backend;

    public ProperWeather() {
    }

    public void onDisable() {
        // maybe we're disabling because of an error.
        if (weatherSystem != null) {
            log.fine("Killing bad projectiles...");
            int c = ProjectileManager.size();
            ProjectileManager.killAll();
            log.fine("Killed " + c + " projectiles ;)");
            try {
                weatherSystem.deInit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getServer().getScheduler().cancelTask(weatherUpdateTask);
            getServer().getScheduler().cancelTask(regionUpdateTask);
            log.info("ProperWeather disabled");
        }
    }

    public void onEnable() {
        log = getLogger();

        File dataFolder = new File("plugins", getDescription().getName());
        if (!dataFolder.exists())
            dataFolder.mkdir();

        log.info("Looking for suitable backend...");
        serverBackendMatcherRegistry = new ServerBackendMatcherRegistry();
        try {
            serverBackendMatcherRegistry.load(dataFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        backend = serverBackendMatcherRegistry.getBackend(getServer());
        if (backend == null) {
            log.severe("No server backend was found for your server. ProperWeather cannot work without a backend, so it will shutdown. You can request to implement backend for your server software at plugin's homepage (http://dev.bukkit.org/server-mods/properweather/)");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        log.info("Using backend: " + backend.getClass().getSimpleName());
        initFactories();

        if (!new File(dataFolder, "config.yml").exists()) {
            ConfigFile.generateDefaultConfig(new File(getDataFolder(), "config.yml"));
        }
        config = new ConfigFile(getConfig());
        permissions = config.getPerms();
        color = config.getColorTheme()[0];
        factColor = config.getColorTheme()[1];
        weatherSystem = new DefaultWeatherSystem(weatherFactoryRegistry, weatherCycleFactoryRegistry, backend);

        weatherListener = new PWWeatherListener(weatherSystem);
        playerListener = new PWPlayerListener(weatherSystem);
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (permissions == null)
            permissions = EPermissions.OP;
        getCommand("pw").setExecutor(new PWCommand(new CommandHandler(weatherSystem)));

        registerTasks();
        log.fine("Permissions system: " + permissions.toString());

        initTranslations();

        try {
            weatherSystem.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (config.shouldMapBiomes()) {
            // initiate a complete scan...
            mapperManager.completeScan();
            getServer().getPluginManager().registerEvents(mapperManager, this);
        }
        log.info(getDescription().getVersion() + " is enabled");
    }

    private void initFactories() {
        weatherFactoryRegistry = new WeatherFactoryRegistry(wim = new WeatherInfoManager());
        weatherCycleFactoryRegistry = new WeatherCycleFactoryRegistry();
        weatherElementFactoryRegistry = new WeatherElementFactoryRegistry();
        try {
            weatherFactoryRegistry.load(getDataFolder());
            weatherCycleFactoryRegistry.load(getDataFolder());
            weatherElementFactoryRegistry.load(getDataFolder());
        } catch (IOException e1) {
            log.severe("ERROR: Failed to load");
            e1.printStackTrace();
        }
    }

    private void registerTasks() {
        registerAllEvents(PWPlayerListener.class, playerListener);
        registerAllEvents(PWWeatherListener.class, weatherListener);
        weatherUpdateTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new WeatherUpdateTask(weatherSystem), 88L, TASK_PERIOD)
                .getTaskId();
        regionUpdateTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new RegionUpdateTask(weatherSystem.getRegionManager()), 88L,
                88L);
        if ((weatherUpdateTask == -1) || (regionUpdateTask == -1)) {
            log.severe(ChatColor.RED + "FATAL ERROR: Task scheduling failed! Plugin will now shut down itself");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void initTranslations() {
        try {
            Translator.init(config.getTranslationFilePath());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Translator.init(new File(getDataFolder(), config.getTranslationFilePath()).getAbsolutePath());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static ProperWeather instance() {
        return (ProperWeather) Bukkit.getServer().getPluginManager().getPlugin("ProperWeather");
    }

    public WeatherSystem getWeatherSystem() {
        return weatherSystem;
    }

    public static String[] getVersionInfo() {
        return new String[] {
                "ProperWeather v" + instance().getDescription().getVersion(), "by: Tomsik68", "Homepage:",
                "http://dev.bukkit.org/server-mods/properweather/"
        };
    }

    public void setupPermissions() {
        permissions = config.getPerms();
    }

    public void reload() {
        onDisable();
        onEnable();
    }

    public ConfigFile getConfigFile() {
        return config;
    }

    public void registerAllEvents(Class<?> listenerClazz, Listener target) {
        getServer().getPluginManager().registerEvents(target, this);
    }

    public WeatherDescription getWeatherDescription(String weatherName) {
        return wim.getWeatherDescription(weatherName);
    }

    public WeatherDefinition getWeatherDefinition(String name) {
        return wim.getWeatherDefinition(name);
    }

    public WeatherDefaults getWeatherDefaults(String weather) {
        return wim.getWeatherDefaults(weather);
    }

    public BiomeMapperManager getMapperManager() {
        return mapperManager;
    }

    public WeatherFactoryRegistry getWeathers() {
        return weatherFactoryRegistry;
    }

    public WeatherCycleFactoryRegistry getCycles() {
        return weatherCycleFactoryRegistry;
    }

    public WeatherElementFactoryRegistry getWeatherElements() {
        return weatherElementFactoryRegistry;
    }
}