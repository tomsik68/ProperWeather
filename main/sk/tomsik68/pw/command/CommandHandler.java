package sk.tomsik68.pw.command;

import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.RegionType;
import sk.tomsik68.pw.WeatherManager;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.mv.MVInteraction;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.Region;
import sk.tomsik68.pw.transl.Translator;

public class CommandHandler implements ICommandHandler {
    private final WeatherSystem weatherSystem;

    public CommandHandler(WeatherSystem ws) {
        weatherSystem = ws;
    }

    public void stopAt(CommandSender sender, String worldName, String weatherName) {
        if ((!verifyPermission(sender, "pw.stopat")) && (!verifyPermission(sender, "pw.enable"))) {
            sender.sendMessage("[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        if (!WeatherManager.isRegistered(weatherName)) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.instance.translate("error.nofound.weather", new Object[] { weatherName }));
            return;
        }
        if (Bukkit.getServer().getWorld(worldName) == null) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.instance.translate("error.nofound.world", new Object[] { worldName }));
            return;
        }
        weatherSystem.stopAtWeather(worldName, weatherName);
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.stopped", new Object[] { weatherName, worldName }));
    }

    public void run(CommandSender sender, String worldName) {
        if ((!verifyPermission(sender, "pw.run")) && (!verifyPermission(sender, "pw.enable"))) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        if (Bukkit.getServer().getWorld(worldName) == null) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.instance.translate("error.nofound.world", new Object[] { worldName }));
            return;
        }
        weatherSystem.runWeather(worldName);
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.translateString("notify.running", new Object[] { worldName }));
    }

    public void sendWorldList(CommandSender sender) {
        if (!verifyPermission(sender, "pw.list")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
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

    public void sendWeatherList(CommandSender sender) {
        if (!verifyPermission(sender, "pw.wlist")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        Collection<String> weathers = weatherSystem.getWeatherList();
        if ((weathers == null) || (weathers.isEmpty())) {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.noweathers", new Object[] { "" }));
        } else {
            StringBuilder sb = new StringBuilder();
            for (String name : weathers) {
                sb = sb.append(ProperWeather.factColor);
                sb = sb.append(name);
                sb = sb.append(ProperWeather.color).append(",");
            }
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.weathers", new Object[] { "" }));
            sender.sendMessage(sb.deleteCharAt(sb.length() - 1).toString());
        }
    }

    public void disable(CommandSender sender, String worldName) {
        if (!verifyPermission(sender, "pw.disable"))
            return;
        weatherSystem.unHook(worldName);
        sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.unhooked", new Object[] { worldName }));
    }

    public boolean verifyPermission(CommandSender sender, String p) {
        if ((sender instanceof Player)) {
            return ProperWeather.instance().permissions.has(sender, p);
        }
        return true;
    }

    public void sendVersionInfo(CommandSender sender) {
        if (!verifyPermission(sender, "pw.pw"))
            return;
        for (String s : ProperWeather.getVersionInfo())
            sender.sendMessage(s);
    }

    public void sendPermissionInfo(CommandSender sender) {
        sender.sendMessage(ProperWeather.color + Translator.instance.translate("notify.permissionsys", new Object[] { ProperWeather.instance().permissions.name() }));
        if (verifyPermission(sender, "pw.perm.check")) {
            sender.sendMessage(formPNMessage(sender, "[ALL]"));
            sender.sendMessage(formPNMessage(sender, "/pw"));
            sender.sendMessage(formPNMessage(sender, "/pw stopat"));
            sender.sendMessage(formPNMessage(sender, "/pw run"));
            sender.sendMessage(formPNMessage(sender, "/pw off"));
            sender.sendMessage(formPNMessage(sender, "/pw perms"));
            sender.sendMessage(formPNMessage(sender, "/pw sit"));
            sender.sendMessage(formPNMessage(sender, "/pw rgt"));
        }
    }

    private String formPNMessage(CommandSender sender, String node) {
        if (verifyPermission(sender, node))
            return ChatColor.BLUE.toString().concat(node);
        return ChatColor.RED.toString().concat(node);
    }

    public void reload(CommandSender sender) {
        if (verifyPermission(sender, "pw.reload")) {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.reload", new Object[] { "" }));
            ProperWeather.instance().reload();
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.reload.finish", new Object[] { "" }));
        } else {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
        }
    }

    public void im(CommandSender sender) {
        MVInteraction mv = MVInteraction.getInstance();
        if (mv.setup(sender.getServer())) {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.mv.import", new Object[] { "" }));
            List<World> worlds = MVInteraction.getInstance().getControlledWorlds();
            for (World world : worlds) {
                boolean canEverChange = mv.isWeatherOn(world.getName());
                if (!canEverChange)
                    weatherSystem.stopAtWeather(world.getName(), "clear");
            }
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("notify.mv.import.finish", new Object[] { "" }));
        } else {
            sender.sendMessage(ProperWeather.color + "[ProperWeather]" + Translator.instance.translate("error.mv", new Object[] { "" }));
        }
    }

    public void sendSitutation(CommandSender sender) {
        if (!verifyPermission(sender, "pw.sit")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        try {
            for (Region region : weatherSystem.getRegionManager().getAllRegions())
                sender.sendMessage(region.toString() + " : " + weatherSystem.getCurrentSituation(region.getUID()).getCurrentWeather());
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.invalidsit"));
        }
    }

    @Override
    public void setRegionType(CommandSender sender, String world, String typ) {
        if (!verifyPermission(sender, "pw.rgt")) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("notify.noperm"));
            return;
        }
        RegionType type = RegionType.valueOf(typ.toUpperCase());
        if (type == null) {
            sender.sendMessage(ChatColor.RED + "[ProperWeather]" + Translator.translateString("error.invalidarg", typ, RegionType.BIOME.name() + "," + RegionType.WORLD.name()));
            return;
        }
        ProperWeather.instance().getConfigFile().setRegionType(world, type);
    }
}