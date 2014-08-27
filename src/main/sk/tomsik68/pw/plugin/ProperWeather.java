package sk.tomsik68.pw.plugin;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.WeatherInfoManager;
import sk.tomsik68.pw.api.BiomeMapperManager;
import sk.tomsik68.pw.api.IServerBackend;
import sk.tomsik68.pw.api.WeatherDefaults;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.command.PW_AC;
import sk.tomsik68.pw.config.ConfigFile;
import sk.tomsik68.pw.config.WeatherDescription;
import sk.tomsik68.pw.impl.DefaultBiomeMapperManager;
import sk.tomsik68.pw.impl.DefaultWeatherSystem;
import sk.tomsik68.pw.impl.registry.ServerBackendMatcherRegistry;
import sk.tomsik68.pw.impl.registry.WeatherCycleFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherElementFactoryRegistry;
import sk.tomsik68.pw.impl.registry.WeatherFactoryRegistry;
import sk.tomsik68.pw.transl.Translator;

public class ProperWeather extends JavaPlugin implements Listener {
    public static Logger log;
    public static boolean isSpout = true;

    public static final int TASK_PERIOD = 88;

    private PWWeatherListener weatherListener;
    private WeatherSystem weatherSystem;
    private PWPlayerListener playerListener;

    public EPermissions permissions;

    private ConfigFile config;

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
    private boolean weatherSystemInitFail;
    private PW_AC commandHandler = new PW_AC();

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

        File dataFolder = getDataFolder();
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
        try {
            initFactories();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

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
        if (permissions == null)
            permissions = EPermissions.OP;
        // getCommand("pw").setExecutor(new PWCommand(new
        // CommandHandler(weatherSystem)));
        commandHandler.register(getCommand("pw"), weatherSystem);

        registerTasks();
        log.fine("Permissions system: " + permissions.toString());

        initTranslations();

        try {
            weatherSystem.init();
            weatherSystemInitFail = false;
        } catch (NoSuchElementException e) {
            weatherSystemInitFail = true;
            log.info("Waiting for other plugins to register their weathers...");
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

    @EventHandler
    void onPluginEnable(PluginEnableEvent event) {
        if (weatherSystemInitFail) {
            try {
                weatherSystem.init();
                weatherSystemInitFail = false;
                log.info("Plugins have registered their weathers! Finally :)");
            } catch (NoSuchElementException e) {
                weatherSystemInitFail = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initFactories() throws IOException {
        tryMigrateOldWeathersYML();
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

    private void tryMigrateOldWeathersYML() throws IOException {
        File oldWeathersYmlFile = new File(getDataFolder(), "weathers.yml");
        if (oldWeathersYmlFile.exists()) {
            YamlConfiguration weathersYml = YamlConfiguration.loadConfiguration(oldWeathersYmlFile);
            YamlConfiguration weatherSettings = new YamlConfiguration();
            YamlConfiguration weatherDefinitions = new YamlConfiguration();

            Set<String> keys = weathersYml.getKeys(false);
            for (String key : keys) {
                if (weathersYml.isConfigurationSection(key)) {
                    ConfigurationSection section = weathersYml.getConfigurationSection(key);
                    if (section.contains("raining")) {
                        // it's a definition
                        weatherDefinitions.createSection(key, section.getValues(true));
                    } else {
                        // it's a description
                        weatherSettings.createSection(key, section.getValues(true));
                    }
                }
            }
            weatherSettings.save(new File(getDataFolder(), "weather_settings.yml"));
            weatherDefinitions.save(new File(getDataFolder(), "weather_defs.yml"));
            oldWeathersYmlFile.renameTo(new File(getDataFolder(), "weathers.yml.old"));
            log.warning("It was detected, that you were using weathers.yml. If you changed any values there, you might want to migrate them to new format for 1.1.1. If you didn't touch the file, everything should behave as expected. Otherwise, please read plugin's homepage for more information.");
        }
    }

    private void registerTasks() {
        registerAllEvents(PWPlayerListener.class, playerListener);
        registerAllEvents(PWWeatherListener.class, weatherListener);
        registerAllEvents(this.getClass(), this);
        weatherUpdateTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new WeatherUpdateTask(weatherSystem), 88L, TASK_PERIOD)
                .getTaskId();
        regionUpdateTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new RegionUpdateTask(weatherSystem.getRegionManager()), 88L,
                88L);
        if ((weatherUpdateTask == -1) || (regionUpdateTask == -1)) {
            log.severe("FATAL ERROR: Task scheduling failed! Plugin will now shut down itself");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void initTranslations() {
        try {
            Translator.init(config.getTranslationFilePath(), getResource("en.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Translator.init(new File(getDataFolder(), config.getTranslationFilePath()).getAbsolutePath(), getResource("en.txt"));
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