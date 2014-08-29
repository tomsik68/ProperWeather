package sk.tomsik68.pw.command;

import org.bukkit.command.PluginCommand;

import sk.tomsik68.autocommand.AutoCommandContext;
import sk.tomsik68.autocommand.args.BukkitArgumentParsers;
import sk.tomsik68.autocommand.args.EnumParser;
import sk.tomsik68.autocommand.args.StringRespectingArgumentTokenizer;
import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.RegionType;

public class PW_AC {
    private AutoCommandContext context;
    private CommandHandlerTest commandHandler;

    public PW_AC(ProperWeather pw, EPermissions perms) {
        context = new AutoCommandContext(pw, perms, new StringRespectingArgumentTokenizer(), null);
        BukkitArgumentParsers.registerBukkitParsers(context.getArgumentParsers());
        context.getArgumentParsers().registerArgumentParser(RegionType.class, new EnumParser<RegionType>(RegionType.class));
    }

    public void register(PluginCommand command, WeatherSystem ws) {
        commandHandler = new CommandHandlerTest(ws);
        context.registerCommands("pw", commandHandler);
    }

}
