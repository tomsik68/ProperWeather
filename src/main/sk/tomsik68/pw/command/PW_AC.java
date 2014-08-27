package sk.tomsik68.pw.command;

import org.bukkit.command.PluginCommand;

import sk.tomsik68.autocommand.AutoCommandHandler;
import sk.tomsik68.autocommand.MultipleCommands;
import sk.tomsik68.autocommand.args.BasicParsers;
import sk.tomsik68.autocommand.args.BukkitArgumentParsers;
import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.api.WeatherSystem;

public class PW_AC {
    private AutoCommandHandler handler;
    private CommandHandlerTest commandHandler;

    public PW_AC() {
        BasicParsers.registerBasicParsers();
        BukkitArgumentParsers.registerBukkitParsers();
    }

    public void register(PluginCommand command, WeatherSystem ws) {
        commandHandler = new CommandHandlerTest(ws);
        MultipleCommands pw = new MultipleCommands("Base command for ProperWeather");
        pw.registerCommands(commandHandler);
        handler = new AutoCommandHandler(pw, EPermissions.NONE);
        command.setExecutor(handler);
    }

}
