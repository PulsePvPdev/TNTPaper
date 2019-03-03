package kr.kieran.tntpaper.commands;

import kr.kieran.tntpaper.TNTPaper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TNTPaperCommand implements CommandExecutor {

    private TNTPaper plugin;

    public TNTPaperCommand(TNTPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tntpaper.commands.tntpaper")) {
            sender.sendMessage(plugin.getDataUtils().noPermission);
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(plugin.getDataUtils().invalidUsage);
            return true;
        }
        switch (args[0]) {
            case "help":
                sender.sendMessage(ChatColor.GRAY + "TNT Paper made by " + ChatColor.RED + "Kieraaaan" + ChatColor.GRAY + " version " + ChatColor.RED + plugin.getDescription().getVersion());
                for (String line : plugin.getDataUtils().helpMessage) {
                    sender.sendMessage(plugin.getDataUtils().c(line));
                }
                return true;
            case "reload":
                plugin.getConfigUtils().reloadConfigurations();
                sender.sendMessage(plugin.getDataUtils().configReloaded);
                return true;
            case "reset":
                plugin.getConfigUtils().getConfig().set("item.current-id", 0);
                plugin.getConfigUtils().saveConfigurations();
                sender.sendMessage(plugin.getDataUtils().tntIdsReset);
                return true;
            case "give":
                if (args.length < 2) {
                    sender.sendMessage(plugin.getDataUtils().invalidUsage);
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(plugin.getDataUtils().invalidPlayer.replace("%player%", args[1]));
                    return true;
                }
                if (args.length == 2) {
                    target.getInventory().addItem(plugin.getItemUtils().createTNTPaper(0, -1));
                    sender.sendMessage(plugin.getDataUtils().infiniteTntPaperSent.replace("%player%", target.getName()).replace("%tnt%", String.valueOf(0)));
                    target.sendMessage(plugin.getDataUtils().infiniteTntPaperReceived.replace("%tnt%", String.valueOf(0)));
                    return true;
                }
                if (args.length == 3) {
                    try {
                        target.getInventory().addItem(plugin.getItemUtils().createTNTPaper(Integer.valueOf(args[2]), -1));
                        sender.sendMessage(plugin.getDataUtils().infiniteTntPaperSent.replace("%player%", target.getName()).replace("%tnt%", String.valueOf(args[2])));
                        target.sendMessage(plugin.getDataUtils().infiniteTntPaperReceived.replace("%tnt%", String.valueOf(args[2])));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getDataUtils().invalidUsage);
                        return true;
                    }
                    return true;
                }
                if (args.length == 4) {
                    try {
                        target.getInventory().addItem(plugin.getItemUtils().createTNTPaper(Integer.valueOf(args[2]), Integer.valueOf(args[3])));
                        sender.sendMessage(plugin.getDataUtils().maximumTntPaperSent.replace("%player%", target.getName()).replace("%tnt%", String.valueOf(args[2])).replace("%maximum%", String.valueOf(args[3])));
                        sender.sendMessage(plugin.getDataUtils().maximumTntPaperReceived.replace("%tnt%", String.valueOf(args[2])).replace("%maximum%", String.valueOf(args[3])));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(plugin.getDataUtils().invalidUsage);
                        return true;
                    }
                    return true;
                }
            default:
                sender.sendMessage(plugin.getDataUtils().invalidUsage);
                return true;
        }
    }

}
