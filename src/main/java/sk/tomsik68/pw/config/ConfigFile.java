package sk.tomsik68.pw.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.RegionType;

public final class ConfigFile {
	private static final String NODE_PERMISSION_SYSTEM = "default-permissions";
	private static final String NODE_COLOR_THEME = "colors";
	private static final String NODE_NOTIFY_MV = "notify-mv";
	private static final String NODE_LOCFILE = "translation-file";
	private static final String NODE_REG_TYPE = "region-type";
	private static final String NODE_MAP_BIOMES = "use-biome-weather";
	private FileConfiguration config;

	public ConfigFile(FileConfiguration conf) {
		this.config = conf;
	}

	public EPermissions getPerms() {
		try {
			return EPermissions.valueOf(this.config.getString("default-permissions", "OP").toUpperCase());
		} catch (IllegalArgumentException iae) {
			ProperWeather.log.severe("I don't know permission system called '" + config.getString(NODE_PERMISSION_SYSTEM) + "', but I think you're trying to setup Super-Perms. Setting up for you :)");
			return EPermissions.SP;
		}
	}

	public static void generateDefaultConfig(File file) {
		try {
			file.createNewFile();
			ProperWeather.log.fine("[ProperWeather] Couldn't find configuration file. Creating a new one...");
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			pw.println("# ProperWeather default configuration file");
			pw.println("# PW version: " + ProperWeather.getVersionInfo()[0]);
			pw.println("#--------------------------------------------#");
			pw.println("#--------------------------------------------#");
			pw.println("# Default permission system ");
			pw.println("# Possible values: OP,SP,None");
			pw.println(NODE_PERMISSION_SYSTEM + ": OP");
			pw.println("#--------------------------------------------#");
			pw.println("#--------------------------------------------#");
			pw.println("# If there is Multiverse found, should ProperWeather notify Multiverse when weather changes?");
			pw.println(NODE_NOTIFY_MV + ": true");
			StringBuilder sb = new StringBuilder();
			for (ChatColor cc : ChatColor.values()) {
				sb = sb.append(",").append(cc.name().toLowerCase());
			}
			sb = sb.deleteCharAt(sb.length() - 1);
			pw.println("# Available colors: " + sb.toString());
			pw.println(NODE_COLOR_THEME + ": green;red");
			pw.println("#--------------------------------------------#");
			pw.println("# Where is the localisation file?");
			pw.println(NODE_LOCFILE + ": plugins/ProperWeather/en.txt");
			pw.println("#--------------------------------------------#");
			pw.println(NODE_MAP_BIOMES + ": false");
			pw.println("#--------------------------------------------#");
			pw.println("# Region type for every world");
			sb = new StringBuilder();
			for (RegionType rt : RegionType.values()) {
				sb = sb.append(",").append(rt.name().toLowerCase());
			}
			sb = sb.deleteCharAt(0);
			pw.println("# Possible values(must exactly match): " + sb.toString());
			List<World> worlds = Bukkit.getWorlds();
			for (World world : worlds) {
				pw.println(NODE_REG_TYPE + "." + world.getName() + ": WORLD");
			}
			pw.flush();
			pw.close();
			ProperWeather.log.fine("Configuration file created at: " + file.getAbsolutePath());
		} catch (Exception e) {
			ProperWeather.log.severe("Configuration file creation error: ");
			e.printStackTrace();
		}
	}

	public ChatColor[] getColorTheme() {
		String[] colors = this.config.getString(NODE_COLOR_THEME, "green;gray").split(";");
		if (colors.length == 2)
			return new ChatColor[] { ChatColor.valueOf(colors[0].toUpperCase()), ChatColor.valueOf(colors[1].toUpperCase()) };
		throw new IllegalArgumentException("[ProperWeather] Invalid property value: " + this.config.getString("colors"));
	}

	public boolean shouldNotifyMV() {
		return this.config.getBoolean("notify-mv");
	}

	public String getTranslationFilePath() {
		return this.config.getString("translation-file");
	}

	public void setRegionType(String worldName, RegionType type) {
		config.set("region-type." + worldName, type.name());
		try {
			this.config.save(new File(ProperWeather.instance().getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RegionType getRegionType(String worldName) {
		if (!this.config.contains("region-type." + worldName)) {
			this.config.set("region-type." + worldName, "WORLD");
			try {
				this.config.save(new File(ProperWeather.instance().getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return RegionType.valueOf(this.config.getString("region-type." + worldName, "world").toUpperCase());
	}

	public boolean shouldMapBiomes() {
		return config.getBoolean(NODE_MAP_BIOMES, false);
	}
}