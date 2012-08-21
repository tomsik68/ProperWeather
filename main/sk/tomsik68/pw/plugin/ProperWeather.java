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
    private PWWeatherListener weatherListener;
    private WeatherSystem weatherSystem;
    private final PWServerListener serverListener;
    public EPermissions permissions;
    private PWPlayerListener playerListener;
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
        weatherSystem.deInit();
        getServer().getScheduler().cancelTask(weatherUpdateTask);
        getServer().getScheduler().cancelTask(regionUpdateTask);
        System.out.println("ProperWeather disabled");
    }

    public void onEnable() {
        File dataFolder = new File("plugins", getDescription().getName());
        System.out.println("Enabling ProperWeather...");
        if (!dataFolder.exists())
            dataFolder.mkdir();
        wim.init(dataFolder);

        if (!new File(dataFolder, "config.yml").exists())
            ConfigFile.generateDefaultConfig(new File(getDataFolder(), "config.yml"));
        config = new ConfigFile(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")));
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
        weatherUpdateTask = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new WeatherUpdateTask(weatherSystem), 88L, 88L);
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
        System.out.println("ProperWeather " + getDescription().getVersion() + " is enabled");
        weatherSystem.init();
        if (config.shouldMapBiomes()) {
            getServer().getPluginManager().registerEvents(mapperManager, this);
        }
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

    public void setupSpout(boolean self, boolean enabled) {
        if (self) {
            try {
                Class.forName("org.getspout.spoutapi.SpoutManager");
                System.out.println("[ProperWeather] Spout detected");
                isSpout = true;
                weatherSystem.changeControllers(isSpout);
                sm = new SpoutModule(this);
                sm.init();
            } catch (ClassNotFoundException cnfe) {
                sm = null;
                System.out.println("[ProperWeather] Spout not detected!");
                System.out.println("[ProperWeather] It's recommended that you install spout on your server if you're using custom weathers(all weathers except Rain,Storm and Clear). ALL CAN ALSO WORK WITHOUT SPOUT, BUT CERTAIN FEATURES WILL BE DISABLED.");
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