/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
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

    public abstract void im(CommandSender sender);

    public abstract void sendSitutation(CommandSender sender);

    public abstract void setRegionType(CommandSender sender, String world, String type);

    public abstract void sendConfigProperty(CommandSender sender, String string);

    public abstract void inGameConfig(CommandSender sender, String string, String string2);
}