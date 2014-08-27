package sk.tomsik68.pw.command;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sk.tomsik68.bukkit.autocommand.AutoCommand;
import sk.tomsik68.bukkit.autocommand.args.ArgumentParsers;
import sk.tomsik68.bukkit.autocommand.args.EnumParser;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.region.RegionType;
import sk.tomsik68.pw.transl.Translator;

public class CommandHandlerTest {
    private final WeatherSystem weatherSystem;

    public CommandHandlerTest(WeatherSystem ws) {
        Validate.notNull(ws);
        weatherSystem = ws;
        ArgumentParsers.registerArgumentParser(RegionType.class, new EnumParser<RegionType>(RegionType.class));
    }

    @AutoCommand(console = true, player = true, permission = "pw.stopat", usage = "<world> <weather>", name = "stopat")
    public void stopAt(CommandSender sender, World world, String weatherName) {
        try {
            weatherSystem.startCycle("stop", world.getName(), weatherName);
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.stopped", new Object[] {
                    weatherName, world.getName()
            }));
        } catch (NoSuchElementException nsee) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.weather", new Object[] {
                weatherName
            }));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            sender.sendMessage(ChatColor.RED + "[ProperWeather] " + npe.getMessage());
        }
    }

    @AutoCommand(console = false, player = true, permission = "pw.stopat", usage = "<weather>")
    public void stopAt(CommandSender sender, String weatherName) {
        try {
            Player player = (Player) sender;
            weatherSystem.startCycle("stop", player.getWorld().getName(), weatherName);
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.stopped", new Object[] {
                    weatherName, player.getWorld().getName()
            }));
        } catch (NoSuchElementException nsee) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.weather", new Object[] {
                weatherName
            }));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            sender.sendMessage(ChatColor.RED + "[ProperWeather] " + npe.getMessage());
        }
    }

    @AutoCommand(console = true, player = true, permission = "pw.run", usage = "<world>")
    public void run(CommandSender sender, String worldName) {
        if (Bukkit.getServer().getWorld(worldName) == null) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.world", new Object[] {
                worldName
            }));
            return;
        }
        weatherSystem.startCycle("random", worldName, "");
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.running", new Object[] {
            worldName
        }));
    }

    @AutoCommand(console = true, player = true, name = "list", permission = "pw.list")
    public void sendWorldList(CommandSender sender) {
        List<String> worlds = weatherSystem.getWorldList();
        if (worlds.isEmpty()) {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.noworlds"));
        } else {
            StringBuilder sb = new StringBuilder();
            for (String name : worlds) {
                sb = sb.append(ProperWeather.factColor);
                sb = sb.append(name);
                sb = sb.append(ProperWeather.color).append(",");
            }
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.worlds"));
            sender.sendMessage(ProperWeather.color + sb.deleteCharAt(sb.length() - 1).toString());
        }
    }

    @AutoCommand(console = true, player = true, name = "wlist", permission = "pw.wlist")
    public void sendWeatherList(CommandSender sender) {
        Collection<String> weathers = getRegisteredWeathers();
        if ((weathers == null) || (weathers.isEmpty())) {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.noweathers"));
        } else {
            StringBuilder sb = new StringBuilder();
            for (String name : weathers) {
                sb = sb.append(ProperWeather.factColor);
                sb = sb.append(name);
                sb = sb.append(ProperWeather.color).append(",");
            }
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.weathers"));
            sender.sendMessage(sb.deleteCharAt(sb.length() - 1).toString());
        }
    }

    private Collection<String> getRegisteredWeathers() {
        return ProperWeather.instance().getWeathers().getRegistered();
    }

    @AutoCommand(console = true, player = true, name = "off", permission = "pw.off")
    public void disable(CommandSender sender, String worldName) {
        weatherSystem.unHook(worldName);
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.unhooked", new Object[] {
            worldName
        }));
    }

    public boolean verifyPermission(CommandSender sender, String p) {
        if ((sender instanceof Player)) {
            return ProperWeather.instance().permissions.has(sender, p);
        }
        return true;
    }

    @AutoCommand(console = true, player = true, name = "v", permission = "pw.pw")
    public void sendVersionInfo(CommandSender sender) {
        for (String s : ProperWeather.getVersionInfo())
            sender.sendMessage(s);
    }

    @AutoCommand(console = true, player = true, name = "perm", permission = "pw.pw")
    public void sendPermissionInfo(CommandSender sender) {
        sender.sendMessage(ProperWeather.color + Translator.translateString("notify.permissionsys", new Object[] {
            ProperWeather.instance().permissions.name()
        }));
        sender.sendMessage(formPNMessage(sender, "pw.*"));
        sender.sendMessage(formPNMessage(sender, "pw.pw"));
        sender.sendMessage(formPNMessage(sender, "pw.stopat"));
        sender.sendMessage(formPNMessage(sender, "pw.run"));
        sender.sendMessage(formPNMessage(sender, "pw.off"));
        sender.sendMessage(formPNMessage(sender, "pw.conf"));
        sender.sendMessage(formPNMessage(sender, "pw.sit"));
        sender.sendMessage(formPNMessage(sender, "pw.rgt"));
        sender.sendMessage(formPNMessage(sender, "pw.reload"));
        sender.sendMessage(formPNMessage(sender, "pw.im"));
    }

    private String formPNMessage(CommandSender sender, String node) {
        if (verifyPermission(sender, node))
            return ChatColor.BLUE.toString().concat(node);
        return ChatColor.RED.toString().concat(node);
    }

    @AutoCommand(console = true, player = true, permission = "pw.reload")
    public void reload(CommandSender sender) {
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.reload"));
        ProperWeather.instance().reload();
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.reload.finish"));
    }

    @AutoCommand(console = true, player = true, permission = "pw.sit", name = "sit")
    public void sendSitutation(CommandSender sender) {
        try {
            if (!(sender instanceof Player)) {
                for (Region region : weatherSystem.getRegionManager().getAllRegions()) {
                    if (region.getWorld() != null && region.getWeatherData() != null && region.getWeatherData().getCurrentWeather() != null)
                        sender.sendMessage(region.toString() + " : " + region.getWeatherData().getCurrentWeather().getName());
                }
            } else {
                if (!weatherSystem.isHooked(((Player) sender).getWorld())) {
                    sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.noregion"));
                    return;
                }
                Region region = weatherSystem.getRegionManager().getRegionAt(((Player) sender).getLocation());
                if (region.getWorld() != null && region.getWeatherData() != null && region.getWeatherData().getCurrentWeather() != null)
                    sender.sendMessage(ProperWeather.color + "[ProperWeather]" + region.toString() + " : "
                            + region.getWeatherData().getCurrentWeather().getName() + " for next " + region.getWeatherData().getDuration()
                            + " ticks.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.invalidsit"));
        }
    }

    @AutoCommand(console = true, player = true, name = "rgt", permission = "pw.rgt")
    public void setRegionType(CommandSender sender, String world, RegionType type) {
        ProperWeather.instance().getConfigFile().setRegionType(world, type);
        sender.sendMessage(ProperWeather.color + "[ProperWeather] " + Translator.translateString("notify.region-type", world, type));
    }

    @AutoCommand(name = "getconf", console = true, player = true, permission = "pw.conf")
    public void sendConfigProperty(CommandSender sender, String prop) {
        if (!verifyPermission(sender, "pw.conf")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        sender.sendMessage(ProperWeather.color + "[ProperWeather] '" + prop + "': '" + ProperWeather.instance().getConfig().getString(prop) + "'");
    }

    @AutoCommand(console = true, player = true, name = "conf", permission = "pw.conf")
    public void inGameConfig(CommandSender sender, String prop, String val) {
        if (!verifyPermission(sender, "pw.conf")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        if (!ProperWeather.instance().getConfig().getKeys(false).contains(prop)) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("warning.noproperty", prop));
        }
        Object obj = parse(val);
        ProperWeather.instance().getConfig().set(prop, obj);
        ProperWeather.instance().saveConfig();
        sender.sendMessage(ProperWeather.color + "[ProperWeather] '" + prop + "' = '" + val + "'");
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

    @AutoCommand(console = true, player = true, permission = "pw.start")
    public void start(CommandSender sender, World world, String cycle) {
        try {
            weatherSystem.startCycle(cycle, world.getName(), "");
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.cycle.started", new Object[] {
                    world.getName(), cycle
            }));
        } catch (NoSuchElementException nse) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.nofound.cycle", cycle));
        }
    }
}