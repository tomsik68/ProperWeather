package sk.tomsik68.pw.command;

import org.bukkit.command.PluginCommand;

import sk.tomsik68.autocommand.AutoCommandInstance;
import sk.tomsik68.autocommand.args.BukkitArgumentParsers;
import sk.tomsik68.autocommand.args.EnumParser;
import sk.tomsik68.autocommand.args.StringRespectingArgumentTokenizer;
import sk.tomsik68.permsguru.EPermissions;
import sk.tomsik68.pw.api.WeatherSystem;
import sk.tomsik68.pw.plugin.ProperWeather;
import sk.tomsik68.pw.region.RegionType;

public final class PW_AC {
	private AutoCommandInstance autoCommand;
	private PWCommandHandler commandHandler;

	public PW_AC(ProperWeather pw, EPermissions perms) {
		autoCommand = new AutoCommandInstance(pw, perms, new StringRespectingArgumentTokenizer(), null);

		autoCommand.registerContextParameterProvider(new WorldFromPlayerProvider());

		BukkitArgumentParsers.registerBukkitParsers(autoCommand.getArgumentParsers());
		autoCommand.getArgumentParsers().registerArgumentParser(RegionType.class, new EnumParser<RegionType>(RegionType.class));
	}

	public void register(PluginCommand command, WeatherSystem ws) {
		commandHandler = new PWCommandHandler(ws);
		autoCommand.registerCommands("pw", commandHandler);
	}

}
