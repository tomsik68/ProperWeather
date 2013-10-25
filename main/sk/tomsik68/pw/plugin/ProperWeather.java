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

import java.awt.Color;
import java.io.File;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import sk.tomsik68.bukkitbp.v1.PackageResolver;
import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.WeatherInfoManager;
import sk.tomsik68.pw.WeatherManager;
import sk.tomsik68.pw.api.BiomeMapperManager;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.command.CommandHandler;
import sk.tomsik68.pw.command.PWCommand;
import sk.tomsik68.pw.config.ConfigFile;
import sk.tomsik68.pw.config.WeatherDefinition;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.DefaultBiomeMapperManager;
import sk.tomsik68.pw.impl.DefaultWeatherSystem;
import sk.tomsik68.pw.impl.DefinedWeatherFactory;
import sk.tomsik68.pw.spout.SpoutModule;
import sk.tomsik68.pw.transl.Translator;

public class ProperWeather extends JavaPlugin {
    public static boolean isSpout = true;
    
    public static final Color defaultSkyColor = new Color(9742079, false);
    
    public static final int TASK_PERIOD = 88;
    
    private PWWeatherListener weatherListener;
    private WeatherSystem weatherSystem;
    private final PWServerListener serverListener;
    private PWPlayerListener playerListener;
    
    public EPermissions permissions;
    
    private SpoutModule sm;
    
    private ConfigFile config;
    private FileConfiguration weatherSettings = null;
    
    public static ChatColor factColor = ChatColor.GRAY;
    public static ChatColor color = ChatColor.GREEN;
    
    private final WeatherInfoManager wim = new WeatherInfoManager();
    
    private int weatherUpdateTask;
    private int regionUpdateTask;
    
    private final BiomeMapperManager mapperManager = new DefaultBiomeMapperManager();

    public ProperWeather() {
        serverListener = new PWServerListener();
    }

    public void onDisable() {
        // maybe we're disabling because of an error.
        if (weatherSystem != null) {
            System.out.println("[ProperWeather] Killing bad projectiles...");
            int c = ProjectileManager.size();
            ProjectileManager.killAll();
            System.out.println("[ProperWeather] Killed " + c + " projectiles ;)");
            weatherSystem.deInit();
            getServer().getScheduler().cancelTask(weatherUpdateTask);
            getServer().getScheduler().cancelTask(regionUpdateTask);
            System.out.println("ProperWeather disabled");
        }
    }

    public void onEnable() {
        try {
            PackageResolver.init(Bukkit.class.getClassLoader());
            CompatibilityChecker.test();
            System.out.println("[ProperWeather] Bukkit compatibility test done. ");
        } catch (Exception e) {
            // DEBUG
            e.printStackTrace();
            System.out.println("[ProperWeather] Incompatible CraftBukkit version. Plugin will now shutdown to prevent further issues.");
            System.out.println("[ProperWeather] Error: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        File dataFolder = new File("plugins", getDescription().getName());
        System.out.println("Enabling ProperWeather...");
        if (!dataFolder.exists())
            dataFolder.mkdir();
        wim.init(dataFolder);
        if (!new File(dataFolder, "config.yml").exists()) {
            ConfigFile.generateDefaultConfig(new File(getDataFolder(), "config.yml"));
        }
        config = new ConfigFile(getConfig());
        permissions = config.getPerms();
        color = config.getColorTheme()[0];
        factColor = config.getColorTheme()[1];
        weatherSystem = new DefaultWeatherSystem();

        weatherSettings = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "weathers.yml"));
        weatherListener = new PWWeatherListener(weatherSystem);
        playerListener = new PWPlayerListener(weatherSystem);
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (permissions == null)
            permissions = EPermissions.OP;
        getCommand("pw").setExecutor(new PWCommand(new CommandHandler(weatherSystem)));
        setupSpout(true, true);

        registerAllEvents(PWPlayerListener.class, playerListener);
        registerAllEvents(PWServerListener.class, serverListener);
        registerAllEvents(PWWeatherListener.class, weatherListener);
        weatherUpdateTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new WeatherUpdateTask(weatherSystem), 88L, TASK_PERIOD).getTaskId();
        regionUpdateTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new RegionUpdateTask(weatherSystem.getRegionManager()), 88L, 88L);
        if ((weatherUpdateTask == -1) || (regionUpdateTask == -1)) {
            System.out.println(ChatColor.GREEN + "[ProperWeather] FATAL ERROR: Task scheduling failed! Plugin will now shut down itself");
            getServer().getPluginManager().disablePlugin(this);
        }
        System.out.println("[ProperWeather] Permissions system: " + permissions.toString());
        Set<String> keys = weatherSettings.getKeys(false);

        for (String weather : keys) {
            if (!WeatherManager.isRegistered(weather))
                WeatherManager.registerWeather(weather, new DefinedWeatherFactory(weather));
        }
        try {
            Translator.init(config.getTranslationFilePath());
        } catch (Exception e) {
            Translator.init(new File(dataFolder, config.getTranslationFilePath()).getAbsolutePath());
        }

        weatherSystem.init();
        if (config.shouldMapBiomes()) {
            // initiate a complete scan...
            mapperManager.completeScan();
            getServer().getPluginManager().registerEvents(mapperManager, this);
        }
        System.out.println("ProperWeather " + getDescription().getVersion() + " is enabled");
    }

    public static ProperWeather instance() {
        return (ProperWeather) Bukkit.getServer().getPluginManager().getPlugin("ProperWeather");
    }

    public WeatherSystem getWeatherSystem() {
        return weatherSystem;
    }

    public static String[] getVersionInfo() {
        return new String[] { "ProperWeather v" + instance().getDescription().getVersion(), "by: Tomsik68", "Homepage:", "http://dev.bukkit.org/server-mods/properweather/" };
    }

    public void setupPermissions() {
        permissions = config.getPerms();
    }

    public void setupSpout(boolean automatic, boolean enabled) {
        if (automatic) {
            try {
                Class.forName("org.getspout.spoutapi.SpoutManager");
                System.out.println("[ProperWeather] Spout detected");
                isSpout = true;
                weatherSystem.changeControllers(isSpout);
                sm = new SpoutModule(this);
                sm.init();
            } catch (ClassNotFoundException cnfe) {
                sm = null;
                isSpout = false;
                weatherSystem.changeControllers(isSpout);
            }
            return;
        }
        isSpout = enabled;
        weatherSystem.changeControllers(isSpout);
        if (isSpout) {
            sm = new SpoutModule(this);
            sm.init();
        } else {
            sm = null;
        }
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
}