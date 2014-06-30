package sk.tomsik68.pw.command;

import org.bukkit.command.CommandSender;

public abstract interface ICommandHandler {
    public abstract void stopAt(CommandSender sender, String worldName, String weatherName);

    public abstract void run(CommandSender sender, String world);

    public abstract void sendWorldList(CommandSender sender);

    public abstract void sendWeatherList(CommandSender sender);

    public abstract void disable(CommandSender sender, String world);

    public abstract void sendVersionInfo(CommandSender sender);

    public abstract void sendPermissionInfo(CommandSender sender);

    public abstract void reload(CommandSender sender);

    public abstract void sendSitutation(CommandSender sender);

    public abstract void setRegionType(CommandSender sender, String world, String type);

    public abstract void sendConfigProperty(CommandSender sender, String string);

    public abstract void inGameConfig(CommandSender sender, String key, String value);

    public abstract void start(CommandSender sender, String world, String cycle);
}