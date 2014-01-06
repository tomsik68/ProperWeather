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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sk.tomsik68.pw.plugin.ProperWeather;

public class PWCommand implements CommandExecutor {
    private final Map<Pattern, CommandExecutor> executors = new HashMap<Pattern, CommandExecutor>();

    public PWCommand(final ICommandHandler handler) {

        RunnableCommand help = new RunnableCommand() {
            @Override
            public void run() {
                sendHelp(sender, ProperWeather.color);
            }
        };
        executors.put(Pattern.compile("help\\w*"), help);
        executors.put(Pattern.compile("\\?\\w*"), help);
        executors.put(Pattern.compile("list"), new RunnableCommand() {
            public void run() {
                handler.sendWorldList(sender);
            }
        });
        executors.put(Pattern.compile("wlist"), new RunnableCommand() {
            public void run() {
                handler.sendWeatherList(sender);
            }
        });
        executors.put(Pattern.compile("v"), new RunnableCommand() {
            public void run() {
                handler.sendVersionInfo(sender);
            }
        });
        executors.put(Pattern.compile("run"), new RunnableCommand() {
            public void run() {
                if (!(sender instanceof Player))
                    return;
                Player player = (Player) sender;
                handler.run(sender, player.getWorld().getName());
            }
        });
        executors.put(Pattern.compile("off"), new RunnableCommand() {
            public void run() {
                if ((sender instanceof Player))
                    handler.disable(sender, ((Player) sender).getWorld().getName());
            }
        });
        executors.put(Pattern.compile("perms"), new RunnableCommand() {
            public void run() {
                handler.sendPermissionInfo(sender);
            }
        });
        executors.put(Pattern.compile("run\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.run(sender, args[1]);
            }
        });
        executors.put(Pattern.compile("off\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.disable(sender, args[1]);
            }
        });
        executors.put(Pattern.compile("stopat\\s\\w*\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.stopAt(sender, args[2], args[1]);
            }
        });
        executors.put(Pattern.compile("start\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.start(sender, ((Player) sender).getWorld().getName(), args[1]);
            }
        });
        executors.put(Pattern.compile("start\\s\\w*\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.start(sender, args[2], args[1]);
            }
        });
        executors.put(Pattern.compile("stopat\\s\\w*"), new RunnableCommand() {
            public void run() {
                handler.stopAt(sender, ((Player) sender).getWorld().getName(), args[1]);
            }
        });
        executors.put(Pattern.compile("reload"), new RunnableCommand() {
            public void run() {
                handler.reload(sender);
            }
        });
        executors.put(Pattern.compile("sit"), new RunnableCommand() {
            public void run() {
                handler.sendSitutation(sender);
            }
        });
        executors.put(Pattern.compile("rgt\\s\\w*\\s\\w*"), new RunnableCommand() {
            @Override
            public void run() {
                handler.setRegionType(sender, args[1], args[2]);
            }
        });
        executors.put(Pattern.compile("conf\\s.*"), new RunnableCommand() {
            @Override
            public void run() {
                if (args.length == 3) {
                    sender.sendMessage("2 args");
                    handler.inGameConfig(sender, args[1], args[2]);
                } else if (args.length == 2) {
                    handler.sendConfigProperty(sender, args[1]);
                }
            }
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        StringBuilder sb = new StringBuilder();
        if ((args != null) && (args.length > 0)) {
            for (String arg : args) {
                sb = sb.append(arg).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            for (Pattern ptr : executors.keySet()) {
                if (ptr.matcher(sb.toString()).matches()) {
                    return executors.get(ptr).onCommand(sender, command, label, args);
                }
            }
        }
        sendHelp(sender, ProperWeather.color);
        return true;
    }

    private void sendHelp(CommandSender sender, ChatColor color) {
        boolean console = !(sender instanceof Player);
        sender.sendMessage(color + "ProperWeather help:");
        sender.sendMessage(formMessage("pw [help | ?]- Display general help for ProperWeather(this)", console));
        sender.sendMessage(formMessage("pw off {world}- Disables plugin usage in specified or player's world", console));
        sender.sendMessage(formMessage("pw list - Lists all worlds which are being controlled by ProperWeather", console));
        sender.sendMessage(formMessage("pw wlist - Lists all available weathers", console));
        sender.sendMessage(formMessage("pw stopat <weather> {world} - Changes weather and stops it in specified or player's world", console));
        sender.sendMessage(formMessage("pw run {world} - Starts changing weather in specified or player's world by ProperWeather's weather system.", console));
        sender.sendMessage(formMessage("pw v - Displays release information about this ProperWeather version.", console));
        sender.sendMessage(formMessage("pw sit - Displays situation in your region(player) or all regions(console)", console));
        sender.sendMessage(formMessage("pw rgt <world> <type> - Sets region type of specified world to type. You always have to type world. Possible types are: BIOME,WORLD", console));
        sender.sendMessage(formMessage("pw perms - Tells you what you can do.", console));
        sender.sendMessage(formMessage("pw start <cycle> {world} - Starts selected cycle in specified world.", console));
        sender.sendMessage(formMessage("pw reload - Reloads plugin", console));
    }

    public String formMessage(String message, boolean console) {
        if (console) {
            message = message.replace("{", "<");
            message = message.replace("}", ">");
        } else {
            message = message.replace("{", "[");
            message = message.replace("}", "]");
            message = "/".concat(message);
        }
        message = ProperWeather.color.toString().concat(message);
        return message;
    }
}