package me.wolflie.simplehome.commands;

import me.wolflie.simplehome.SimpleHome;
import me.wolflie.simplehome.command.annotation.CommandInfo;
import me.wolflie.simplehome.command.annotation.Default;
import me.wolflie.simplehome.command.annotation.sender.PlayerOnly;
import me.wolflie.simplehome.home.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "deletehome", aliases = {"delhome"})
public class DeleteHomeCommand {

    private final SimpleHome plugin;
    private final HomeManager homeManager;

    public DeleteHomeCommand(SimpleHome plugin) {
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }


    @PlayerOnly
    @Default
    public void onDelete(CommandSender sender, String home) {
        Player player = (Player) sender;
        if (homeManager.getHomesOfPlayer(player).isEmpty()) {
            player.sendMessage(ChatColor.RED + "No homes to delete.");
            return;
        }
        if (homeManager.getHomeByName(player, home.toLowerCase()) != null) {
            homeManager.deleteHome(player, home.toLowerCase());
            player.sendMessage(ChatColor.RED + "Home deleted.");
            return;
        }
        player.sendMessage(ChatColor.RED + "You don't have home by that name.");
    }
}
