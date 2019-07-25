package me.wolflie.simplehome.commands;

import me.wolflie.simplehome.SimpleHome;
import me.wolflie.simplehome.command.annotation.CommandInfo;
import me.wolflie.simplehome.command.annotation.Default;
import me.wolflie.simplehome.command.annotation.OptionalParameter;
import me.wolflie.simplehome.command.annotation.Single;
import me.wolflie.simplehome.command.annotation.sender.PlayerOnly;
import me.wolflie.simplehome.home.Home;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "sethome", usage = "&c/sethome &e[name]")
public class SetHomeCommand {

    private final SimpleHome plugin;

    public SetHomeCommand(SimpleHome plugin) {
        this.plugin = plugin;

    }

    @Default
    @PlayerOnly
    public void onExecute(CommandSender sender, @OptionalParameter @Single String name) {
        Player player = (Player) sender;
        if (name == null) {
            name = "home";
        }
        plugin.getHomeManager().setHome(player, new Home(name.toLowerCase(), player.getLocation()));
        sender.sendMessage(ChatColor.RED + "Home set.");
    }

}
