package sk.tomsik68.pw.command;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sk.tomsik68.autocommand.AutoCommand;
import sk.tomsik68.autocommand.ContextArg;
import sk.tomsik68.autocommand.context.CommandExecutionContext;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.region.RegionType;
import sk.tomsik68.pw.transl.Translator;

final class PWCommandHandler {
	private final WeatherSystem weatherSystem;

	public PWCommandHandler(WeatherSystem ws) {
		Validate.notNull(ws);
		weatherSystem = ws;
	}

	@AutoCommand(permission = "pw.stopat", usage = "<world> <weather>", name = "stopat", help = "Sets weather to <weather> in <world> and stops weather")
	public void stopAt(CommandExecutionContext context, @ContextArg World world, String weatherName) {
		try {
			weatherSystem.startCycle("stop", world.getName(), weatherName);
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.stopped", new Object[] { weatherName, world.getName() }));
		} catch (NoSuchElementException nsee) {
			context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.weather", new Object[] { weatherName }));
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			context.getSender().sendMessage(ChatColor.RED + "[ProperWeather] " + npe.getMessage());
		}
	}

	/*
	 * @AutoCommand(permission = "pw.stopat", usage = "<weather>", help =
	 * "Sets weather to <weather> in player's world and stops weather") public
	 * void stopAt(CommandExecutionContext context, String weatherName) { try {
	 * Player player = (Player) context.getSender();
	 * weatherSystem.startCycle("stop", player.getWorld().getName(),
	 * weatherName); context.getSender().sendMessage(ProperWeather.color +
	 * "[ProperWeather]" + Translator.translateString("notify.stopped", new
	 * Object[] { weatherName, player.getWorld().getName() })); } catch
	 * (NoSuchElementException nsee) {
	 * context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" +
	 * Translator.translateString("error.nofound.weather", new Object[] {
	 * weatherName })); } catch (NullPointerException npe) {
	 * npe.printStackTrace(); context.getSender().sendMessage(ChatColor.RED +
	 * "[ProperWeather] " + npe.getMessage()); } }
	 */

	@AutoCommand(permission = "pw.run", usage = "<world>", help = "Starts random weather changes of weather in <world>")
	public void run(CommandExecutionContext context, @ContextArg World world) {
		weatherSystem.startCycle("random", world.getName(), "");
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.running", new Object[] { world.getName() }));
	}

	@AutoCommand(name = "list", permission = "pw.list", help = "Lists worlds where ProperWeather is active")
	public void sendWorldList(CommandExecutionContext context) {
		List<String> worlds = weatherSystem.getWorldList();
		if (worlds.isEmpty()) {
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.noworlds"));
		} else {
			StringBuilder sb = new StringBuilder();
			for (String name : worlds) {
				sb = sb.append(ProperWeather.factColor);
				sb = sb.append(name);
				sb = sb.append(ProperWeather.color).append(",");
			}
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.worlds"));
			context.getSender().sendMessage(ProperWeather.color + sb.deleteCharAt(sb.length() - 1).toString());
		}
	}

	@AutoCommand(name = "wlist", permission = "pw.wlist", help = "Lists available weathers")
	public void sendWeatherList(CommandExecutionContext context) {
		Collection<String> weathers = getRegisteredWeathers();
		if ((weathers == null) || (weathers.isEmpty())) {
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.noweathers"));
		} else {
			StringBuilder sb = new StringBuilder();
			for (String name : weathers) {
				sb = sb.append(ProperWeather.factColor);
				sb = sb.append(name);
				sb = sb.append(ProperWeather.color).append(",");
			}
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.weathers"));
			context.getSender().sendMessage(sb.deleteCharAt(sb.length() - 1).toString());
		}
	}

	private Collection<String> getRegisteredWeathers() {
		return ProperWeather.instance().getWeathers().getRegistered();
	}

	@AutoCommand(name = "off", permission = "pw.off", help = "Disables ProperWeather in specified world")
	public void disable(CommandExecutionContext context, @ContextArg World world) {
		weatherSystem.unHook(world.getName());
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.unhooked", new Object[] { world.getName() }));
	}

	public boolean verifyPermission(CommandSender sender, String p) {
		if ((sender instanceof Player)) {
			return ProperWeather.instance().permissions.has(sender, p);
		}
		return true;
	}

	@AutoCommand(name = "v", permission = "pw.pw", help = "Shows useful information about plugin")
	public void sendVersionInfo(CommandExecutionContext context) {
		for (String s : ProperWeather.getVersionInfo())
			context.getSender().sendMessage(s);
	}

	@AutoCommand(name = "perm", permission = "pw.pw", help = "Shows which permission nodes you have allowed")
	public void sendPermissionInfo(CommandExecutionContext context) {
		context.getSender().sendMessage(ProperWeather.color + Translator.translateString("notify.permissionsys", new Object[] { ProperWeather.instance().permissions.name() }));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.*"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.pw"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.stopat"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.run"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.off"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.conf"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.sit"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.rgt"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.reload"));
		context.getSender().sendMessage(formPNMessage(context.getSender(), "pw.im"));
	}

	private String formPNMessage(CommandSender sender, String node) {
		if (verifyPermission(sender, node))
			return ChatColor.BLUE.toString().concat(node);
		return ChatColor.RED.toString().concat(node);
	}

	@AutoCommand(permission = "pw.reload", help = "Reloads the plugin")
	public void reload(CommandExecutionContext context) {
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.reload"));
		ProperWeather.instance().reload();
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.reload.finish"));
	}

	@AutoCommand(permission = "pw.sit", name = "sit", help = "Shows situation in all regions(console) or player's region(player)")
	public void sendSitutation(CommandExecutionContext context) {
		try {
			if (!(context.getSender() instanceof Player)) {
				for (Region region : weatherSystem.getRegionManager().getAllRegions()) {
					if (region.getWorld() != null && region.getWeatherData() != null && region.getWeatherData().getCurrentWeather() != null)
						context.getSender().sendMessage(region.toString() + " : " + region.getWeatherData().getCurrentWeather().getName());
				}
			} else {
				if (!weatherSystem.isHooked(((Player) context.getSender()).getWorld())) {
					context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.noregion"));
					return;
				}
				Region region = weatherSystem.getRegionManager().getRegionAt(((Player) context.getSender()).getLocation());
				if (region.getWorld() != null && region.getWeatherData() != null && region.getWeatherData().getCurrentWeather() != null)
					context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + region.toString() + " : " + region.getWeatherData().getCurrentWeather().getName() + " for next " + region.getWeatherData().getDuration() + " ticks.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.invalidsit"));
		}
	}

	@AutoCommand(name = "rgt", permission = "pw.rgt", usage = "<world> <type>", help = "Sets region type in <world> to <type>")
	public void setRegionType(CommandExecutionContext context, @ContextArg World world, RegionType type) {
		ProperWeather.instance().getConfigFile().setRegionType(world.getName(), type);
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather] " + Translator.translateString("notify.region-type", world.getName(), type));
	}

	@AutoCommand(name = "getconf", permission = "pw.conf", usage = "<property>", help = "Shows value of configuration property")
	public void sendConfigProperty(CommandExecutionContext context, String prop) {
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather] '" + prop + "': '" + ProperWeather.instance().getConfig().getString(prop) + "'");
	}

	@AutoCommand(name = "conf", permission = "pw.conf", usage = "<prop> <value>", help = "Sets value of configuration property")
	public void inGameConfig(CommandExecutionContext context, String prop, String val) {
		if (!ProperWeather.instance().getConfig().getKeys(false).contains(prop)) {
			context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("warning.noproperty", prop));
		}
		Object obj = parse(val);
		ProperWeather.instance().getConfig().set(prop, obj);
		ProperWeather.instance().saveConfig();
		context.getSender().sendMessage(ProperWeather.color + "[ProperWeather] '" + prop + "' = '" + val + "'");
	}

	private Object parse(String val) {
		if (isBoolean(val)) {
			return val.equalsIgnoreCase("true");
		} else if (isDouble(val)) {
			return Double.parseDouble(val);
		}
		return val;

	}

	private boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isBoolean(String val) {
		return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false");
	}

	@AutoCommand(permission = "pw.start", usage = "<world> <cycle>", help = "Starts <cycle> in <world>")
	public void start(CommandExecutionContext context, @ContextArg World world, String cycle) {
		try {
			weatherSystem.startCycle(cycle, world.getName(), "");
			context.getSender().sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.cycle.started", new Object[] { world.getName(), cycle }));
		} catch (NoSuchElementException nse) {
			context.getSender().sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.cycle", cycle));
		}
	}
}